package eu.mrico.creole.ast;

import eu.mrico.creole.Visitor;

public class Document extends BaseElement {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Document{ " + children + " }";
    }
}
