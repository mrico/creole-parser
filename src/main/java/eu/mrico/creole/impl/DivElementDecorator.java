package eu.mrico.creole.impl;

import eu.mrico.creole.XHtmlElementDecorator;
import eu.mrico.creole.ast.Element;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class DivElementDecorator implements XHtmlElementDecorator<Element> {

    private final String cssClass;

    public DivElementDecorator() {
        this(null);
    }

    public DivElementDecorator(String cssClass) {
        this.cssClass = cssClass;
    }

    @Override
    public void before(Element element, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("div");
        if(cssClass != null)
            writer.writeAttribute("class", cssClass);
    }

    @Override
    public void after(Element element, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeEndElement();
    }

}
