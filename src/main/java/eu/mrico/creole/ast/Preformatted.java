package eu.mrico.creole.ast;

import eu.mrico.creole.Visitor;

public class Preformatted extends Text {

    private final boolean inline;

    public boolean isInline() {
        return inline;
    }

    public Preformatted(String value) {
        this(false, value);
    }

    public Preformatted(boolean inline, String value) {
        super(value);
        this.inline = inline;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
