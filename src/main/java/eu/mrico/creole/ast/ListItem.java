package eu.mrico.creole.ast;

import eu.mrico.creole.Visitor;

public class ListItem extends BaseElement {

    public ListItem() { /* default constructor */ }

    public ListItem(String text) {
        add(new Text(text));
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
