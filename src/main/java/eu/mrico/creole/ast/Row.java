package eu.mrico.creole.ast;

import eu.mrico.creole.Visitor;

public class Row extends BaseElement {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
