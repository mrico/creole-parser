package eu.mrico.creole.ast;

import eu.mrico.creole.Visitor;

public class Italic extends BaseElement {

    public Italic() {
        // TODO Auto-generated constructor stub
    }

    public Italic(Element elem) {
        add(elem);
    }

    public Italic(String text) {
        add(new Text(text));
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
