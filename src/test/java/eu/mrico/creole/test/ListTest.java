package eu.mrico.creole.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.mrico.creole.ASTDebugger;
import eu.mrico.creole.Creole;
import eu.mrico.creole.ast.Document;
import eu.mrico.creole.ast.Link;
import eu.mrico.creole.ast.List;
import eu.mrico.creole.ast.ListItem;
import eu.mrico.creole.ast.Text;

/**
 * @see http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-Lists
 */
public class ListTest {
	
	@Test	
	public void unorderedList() {
		Document is = Creole.parse("* Item 1\n" + 
								       "** Item 1.1\n" + 
								       "* Item 2\n");
	
		Document expected = (Document) new Document()
			.add(new List(List.UNORDERED)
				.add(new ListItem().addAll(Text.asArray("Item 1"))				
					.add(new List()
						.add(new ListItem().addAll(Text.asArray("Item 1.1")))))
				.add(new ListItem().addAll(Text.asArray("Item 2"))));
		
//		new ASTDebugger().visit(is);
		
		assertEquals(expected, is);
	}
	
	@Test	
	public void orderedList() {
		Document is = Creole.parse("# Item 1\n" + 
								       "## Item 1.1\n" + 
								       "# Item 2\n");
	
		Document expected = (Document) new Document()
			.add(new List(List.ORDERED)
				.add(new ListItem().addAll(Text.asArray("Item 1"))				
					.add(new List(List.ORDERED)
						.add(new ListItem().addAll(Text.asArray("Item 1.1")))))
				.add(new ListItem().addAll(Text.asArray("Item 2"))));
		
//		new ASTDebugger().visit(is);
		
		assertEquals(expected, is);
	}
	
}

