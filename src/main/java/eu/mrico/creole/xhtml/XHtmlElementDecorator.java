package eu.mrico.creole.xhtml;

import eu.mrico.creole.ast.Element;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;


public interface XHtmlElementDecorator<T extends Element> {

    void before(T element, XMLStreamWriter writer) throws XMLStreamException;

    void after(T element, XMLStreamWriter writer) throws XMLStreamException;

}
