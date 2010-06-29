package eu.mrico.creole.xhtml;

import eu.mrico.creole.CreoleException;
import eu.mrico.creole.CreoleWriter;
import eu.mrico.creole.Visitor;
import java.io.OutputStream;

import eu.mrico.creole.ast.Plugin;
import eu.mrico.creole.ast.Bold;
import eu.mrico.creole.ast.Cell;
import eu.mrico.creole.ast.Document;
import eu.mrico.creole.ast.Element;
import eu.mrico.creole.ast.Heading;
import eu.mrico.creole.ast.HorizontalRule;
import eu.mrico.creole.ast.Image;
import eu.mrico.creole.ast.Italic;
import eu.mrico.creole.ast.LineBreak;
import eu.mrico.creole.ast.Link;
import eu.mrico.creole.ast.List;
import eu.mrico.creole.ast.ListItem;
import eu.mrico.creole.ast.Paragraph;
import eu.mrico.creole.ast.Phrase;
import eu.mrico.creole.ast.Preformatted;
import eu.mrico.creole.ast.Row;
import eu.mrico.creole.ast.Table;
import eu.mrico.creole.ast.Text;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class XHtmlWriter implements CreoleWriter, Visitor {

    private XMLStreamWriter writer;

    private Map<String, Collection<String>> cssClassesMap =
            new HashMap<String, Collection<String>>();

    private Map<Class<? extends Element>, XHtmlElementDecorator> decoratorsMap =
            new HashMap<Class<? extends Element>, XHtmlElementDecorator>();

    public void addCssClass(String tagName, String[] cssClass) {
        for(String cls : cssClass)
            addCssClass(tagName, cls);
    }

    public void addCssClass(String tagName, String cssClass) {
        Collection<String> cssClasses = cssClassesMap.get(tagName);

        if(cssClasses == null) {
            cssClasses = new ArrayList<String>();
            cssClassesMap.put(tagName, cssClasses);
        }

        cssClasses.add(cssClass);
    }

    public void setDecorator(Class<? extends Element> type, XHtmlElementDecorator decorator) {
        decoratorsMap.put(type, decorator);
    }


    @Override
    public void write(Document document, OutputStream out)
            throws CreoleException {

        try {
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            this.writer = factory.createXMLStreamWriter(out);

            write(document, writer);

        } catch (XMLStreamException ex) {
            throw new CreoleException(ex);
        }
    }

    public void write(Document document, XMLStreamWriter writer) throws CreoleException {
        try {
            this.writer = writer;
            document.accept(this);
            this.writer.close();

        } catch(XMLStreamException e) {
            throw new CreoleException(e);
        }
    }

    private void writeText(String text) {
        try {
            writer.writeCharacters(text);

        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }


    private void writeStartElement(String tagName) {
        try {
            writer.writeStartElement(tagName);

            Collection<String> cssClasses = cssClassesMap.get(tagName);

            if(cssClasses != null && ! cssClasses.isEmpty()) {
                StringBuffer sb = new StringBuffer();

                for(String cls : cssClasses) {
                    sb.append(cls).append(" ");
                }

                writer.writeAttribute("class", sb.toString().trim());
            }

        } catch (XMLStreamException ex) {
            throw new RuntimeException(ex);
        }
    }


    private void writeSimpleTag(String tagName, Element elem) {
        writeSimpleTag(tagName, elem, false);
    }

    private void writeSimpleTag(String tagName, Element elem, boolean escape) {
        try {

            XHtmlElementDecorator decorator = decoratorsMap.get(elem.getClass());
            if(decorator != null)
                decorator.before(elem, writer);

            writeStartElement(tagName);

            if (elem instanceof Text) {
                String s = ((Text) elem).getValue();
                if (escape) {
                    s = s.replaceAll("<", "&lt;");
                    s = s.replaceAll(">", "&gt;");
                }
                writer.writeCharacters(s);
            } else {
                for (Element child : elem) {
                    child.accept(this);
                }
            }

            writer.writeEndElement();

            if(decorator != null)
                decorator.after(elem, writer);

        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(Document doc) {
        for (Element elem : doc) {
            elem.accept(this);
        }
    }

    @Override
    public void visit(Paragraph p) {
        writeSimpleTag("p", p);
    }

    @Override
    public void visit(Heading h) {
        writeSimpleTag("h" + h.getLevel(), h);
    }

    @Override
    public void visit(Bold b) {
        writeSimpleTag("b", b);
    }

    @Override
    public void visit(HorizontalRule hr) {
        try {
            writer.writeEmptyElement("hr");
        } catch (XMLStreamException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void visit(Image i) {
        try {
            XHtmlElementDecorator decorator = decoratorsMap.get(Image.class);
            if(decorator != null)
                decorator.before(i, writer);

            writeStartElement("img");
            writer.writeAttribute("src", i.getSource());
            writer.writeAttribute("alt", i.getText());
            writer.writeEndElement();

            if(decorator != null)
                decorator.after(i, writer);

        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(Italic it) {
        writeSimpleTag("em", it);
    }

    @Override
    public void visit(LineBreak lb) {
        try {
            writer.writeEmptyElement("br");
        } catch (XMLStreamException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void visit(Link l) {
        try {
            XHtmlElementDecorator decorator = decoratorsMap.get(Link.class);
            if(decorator != null)
                decorator.before(l, writer);

            writeStartElement("a");
            writer.writeAttribute("href", l.getTarget());

            for(Element child : l)
                child.accept(this);

            writer.writeEndElement();

            if(decorator != null)
                decorator.before(l, writer);

        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(List l) {
        String tagName = l.getStyle() == List.ORDERED ? "ol" : "ul";
        writeSimpleTag(tagName, l);

    }

    @Override
    public void visit(ListItem li) {
        writeSimpleTag("li", li);
    }

    @Override
    public void visit(Phrase p) {
        for (Element child : p) {
            child.accept(this);
        }
    }

    @Override
    public void visit(Row row) {
        writeSimpleTag("tr", row);
    }

    @Override
    public void visit(Table table) {
        writeSimpleTag("table", table);
    }

    @Override
    public void visit(Text text) {
        writeText(text.getValue());
    }

    @Override
    public void visit(Cell cell) {
        String tagName = cell.isHeading() ? "th" : "td";
        writeSimpleTag(tagName, cell);
    }

    @Override
    public void visit(Preformatted preformatted) {
        String tagName = preformatted.isInline() ? "tt" : "pre";
        writeSimpleTag(tagName, preformatted, true);
    }

    @Override
    public void vist(Cell c) {
        // TODO Auto-generated method stub
    }

    @Override
	public void visit(Plugin plugin) {
		CreolePluginHandler handler = Creole.getPlugin(plugin.getName());
		if(handler == null) {
			writeSimpleTag("strong", new Text("Unkown plugin: " + plugin.getName()));
		} else {
			Element elem = handler.execute(null);
			elem.accept(this);
		}
    }
}
