package eu.mrico.creole.ast;

import eu.mrico.creole.Visitor;

public class Cell extends BaseElement {

    private boolean heading = false;

    public boolean isHeading() {
        return heading;
    }

    public void setHeading(boolean heading) {
        this.heading = heading;
    }

    public Cell() {
        // TODO Auto-generated constructor stub
    }

    public Cell(String text) {
        this(false, text);
    }

    public Cell(boolean heading, String text) {
        this.heading = heading;
        add(new Text(text));
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void trim() {

        for (int i = children.size() - 1; i >= 0; i--) {

            Element elem = children.get(i);

            if (!elem.getClass().isAssignableFrom(Text.class)) {
                break;
            }

            Text txt = (Text) elem;

            if (!" ".equals(txt.getValue())) {
                break;
            }


            children.remove(i);
        }

    }
}
