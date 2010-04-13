package eu.mrico.creole.ast;

import eu.mrico.creole.Visitor;

public class Heading extends Text {

    private int level;

    public int getLevel() {
        return level;
    }

    public Heading(int level, String text) {
        super(text);
        this.level = level;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + level;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Heading other = (Heading) obj;
        if (level != other.level) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Heading" + level + "[" + getValue() + "]";
    }
}
