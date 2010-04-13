package eu.mrico.creole.ast;

import eu.mrico.creole.Visitor;

public class Link extends BaseElement {

    private String target;

    public String getTarget() {
        return target;
    }

    public Link(String target) {
        this(target, target);
    }

    public Link(String label, String target) {
        super();
        this.target = target;
        if (label == null || "".equals(label)) {
            label = target;
        }

        add(new Text(label));
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((target == null) ? 0 : target.hashCode());
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
        Link other = (Link) obj;
        if (target == null) {
            if (other.target != null) {
                return false;
            }
        } else if (!target.equals(other.target)) {
            return false;
        }
        return true;
    }
}
