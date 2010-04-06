package eu.mrico.creole;

import eu.mrico.creole.ast.Bold;
import eu.mrico.creole.ast.Cell;
import eu.mrico.creole.ast.Document;
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

public interface Visitor {
	
	void visit(Document doc);
	
	void visit(Paragraph p);
	
	void visit(Heading h);
	
	void visit(Bold b);
	
	void vist(Cell c);
	
	void visit(HorizontalRule hr);
	
	void visit(Image i);
	
	void visit(Italic it);
	
	void visit(LineBreak lb);
	
	void visit(Link l);
	
	void visit(List l);
	
	void visit(ListItem li);
	
	void visit(Phrase p);
	
	void visit(Row row);
	
	void visit(Table table);
	
	void visit(Text text);
	
	void visit(Cell cell);
	
	void visit(Preformatted preformatted);
}
