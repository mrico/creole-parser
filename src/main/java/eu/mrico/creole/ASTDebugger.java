package eu.mrico.creole;

import java.io.PrintWriter;

import eu.mrico.creole.ast.Bold;
import eu.mrico.creole.ast.Cell;
import eu.mrico.creole.ast.Document;
import eu.mrico.creole.ast.Element;
import eu.mrico.creole.ast.Heading;
import eu.mrico.creole.ast.HorizontalRule;
import eu.mrico.creole.ast.Image;
import eu.mrico.creole.ast.Italic;
import eu.mrico.creole.ast.LineBreak;
import eu.mrico.creole.ast.Link;
import eu.mrico.creole.ast.List;
import eu.mrico.creole.ast.ListItem;
import eu.mrico.creole.ast.Paragraph;
import eu.mrico.creole.ast.Phrase;
import eu.mrico.creole.ast.Preformatted;
import eu.mrico.creole.ast.Row;
import eu.mrico.creole.ast.Table;
import eu.mrico.creole.ast.Text;

public class ASTDebugger implements Visitor {

    private int indent = 0;
    private PrintWriter writer;

    public ASTDebugger() {
        this(new PrintWriter(System.out));
    }

    public ASTDebugger(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public void visit(Paragraph p) {
        print(p);
    }

    @Override
    public void visit(Document doc) {
        print(doc);
        writer.close();
    }

    private int getIndent(int start, Element elem) {
        if (elem == null || elem.getParent() == null) {
            return start;
        }
        if (elem.getParent() instanceof Document) {
            return start;
        }

        return getIndent(start + 3, elem.getParent());
    }

    @Override
    public void visit(Heading h) {
        print(h);
    }

    @Override
    public void visit(Bold b) {
        print(b);
    }

    @Override
    public void visit(HorizontalRule hr) {
        print(hr);
    }

    @Override
    public void visit(Image i) {
        print(i);
    }

    @Override
    public void visit(Italic it) {
        print(it);
    }

    @Override
    public void visit(LineBreak lb) {
        print(lb);
    }

    @Override
    public void visit(Link l) {
        print(l);
    }

    @Override
    public void visit(List l) {
        print(l);
    }

    @Override
    public void visit(ListItem li) {
        print(li);
    }

    @Override
    public void visit(Phrase p) {
        print(p);
    }

    @Override
    public void visit(Row row) {
        print(row);
    }

    @Override
    public void visit(Table table) {
        print(table);
    }

    @Override
    public void visit(Text text) {
        print(text);
    }

    @Override
    public void visit(Cell cell) {
        print(cell);
    }

    @Override
    public void vist(Cell c) {
        print(c);
    }

    @Override
    public void visit(Preformatted preformatted) {
        print(preformatted);
    }

    private void print(Element elem) {
        String spaces = "";
        for (int i = 0; i < getIndent(indent, elem); i++) {
            spaces += " ";
        }

        if (elem instanceof Text) {
            writer.println(spaces + elem.getClass().getName() + "[" + ((Text) elem).getValue() + "]");
        } else {
            writer.println(spaces + elem.getClass().getName());
        }

        for (Element child : elem) {
            child.accept(this);
        }
    }
}
