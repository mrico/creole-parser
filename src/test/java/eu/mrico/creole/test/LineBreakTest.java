package eu.mrico.creole.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.mrico.creole.Creole;
import eu.mrico.creole.ast.Document;
import eu.mrico.creole.ast.LineBreak;
import eu.mrico.creole.ast.Link;
import eu.mrico.creole.ast.Paragraph;
import eu.mrico.creole.ast.Text;

/**
 * @see http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-LineBreaks
 */
public class LineBreakTest {
	
	@Test	
	public void lineBreak() {
		Document is = Creole.parse("This is the first line,\\\\and this is the second.");
		
		Document expected = (Document) new Document()			
			.addAll(Text.asArray("This is the first line,"))
			.add(new LineBreak())
			.addAll(Text.asArray("and this is the second."));
			
		assertEquals(expected, is);		
	}
}
