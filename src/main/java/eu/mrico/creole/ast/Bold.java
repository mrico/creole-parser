package eu.mrico.creole.ast;

import eu.mrico.creole.Visitor;

public class Bold extends BaseElement {

    public Bold() {
    }

    public Bold(Element c) {
        add(c);
    }

    public Bold(String text) {
        add(new Text(text));
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
