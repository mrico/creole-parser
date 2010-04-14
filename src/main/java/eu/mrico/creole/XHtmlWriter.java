package eu.mrico.creole;

import eu.mrico.creole.ast.Plugin;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

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

public class XHtmlWriter implements CreoleWriter, Visitor {

    private Writer writer;

    @Override
    public void write(Document document, OutputStream out)
            throws CreoleException {

        this.writer = new OutputStreamWriter(out);

        document.accept(this);

        try {
            this.writer.close();

        } catch (IOException e) {
            throw new CreoleException(e);
        }
    }

    private void writeText(String text) {
        try {
            writer.write(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeSimpleTag(String tagName) {
        try {
            writer.write("<" + tagName + "/>\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeSimpleTag(String tagName, Element elem) {
        writeSimpleTag(tagName, elem, false);
    }

    private void writeSimpleTag(String tagName, Element elem, boolean escape) {
        try {
            writer.write("<" + tagName + ">");
            if (elem instanceof Text) {
                String s = ((Text) elem).getValue();
                if (escape) {
                    s = s.replaceAll("<", "&lt;");
                    s = s.replaceAll(">", "&gt;");
                }
                writer.write(s);
            } else {
                for (Element child : elem) {
                    child.accept(this);
                }
            }
            writer.write("</" + tagName + ">\n");

        } catch (IOException e) {
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
        writeSimpleTag("hr");
    }

    @Override
    public void visit(Image i) {
        try {
            writer.write("<img src=\"" + i.getSource() + "\" alt=\""
                    + i.getText() + "\" />");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(Italic it) {
        writeSimpleTag("em", it);
    }

    @Override
    public void visit(LineBreak lb) {
        writeSimpleTag("br");
    }

    @Override
    public void visit(Link l) {
        try {
            writer.write("<a href=\"" + l.getTarget() + "\">");
            for (Element child : l) {
                child.accept(this);
            }
            writer.write("</a>");
        } catch (IOException e) {
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
