package eu.mrico.creole.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.mrico.creole.Creole;
import eu.mrico.creole.ast.Document;
import eu.mrico.creole.ast.LineBreak;
import eu.mrico.creole.ast.Link;
import eu.mrico.creole.ast.Paragraph;
import eu.mrico.creole.ast.Preformatted;
import eu.mrico.creole.ast.Text;

/**
 * @see http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-NowikiPreformatted
 */
public class PreformattedTest {
	
	@Test	
	public void simpleNoWiki() {
		Document is = Creole.parse("{{{\n" + 
						"//This// does **not** get [[formatted]]\n" +  
						"}}}\n");
		
		Document expected = (Document) new Document()			
			.add(new Preformatted("\n//This// does **not** get [[formatted]]\n"));
			
		assertEquals(expected, is);		
	}
	
	@Test	
	public void closingBracesInNoWiki() {
		Document is = Creole.parse("{{{\n"+
				"if (x != NULL) {\n"+
				"  for (i = 0; i < size; i++) {\n"+
				"    if (x[i] > 0) {\n"+
				"      x[i]--;\n"+
				"  }}} \n"+
				"}}}\n");
		
		Document expected = (Document) new Document()			
			.add(new Preformatted("\nif (x != NULL) {\n"+
					"  for (i = 0; i < size; i++) {\n"+
					"    if (x[i] > 0) {\n"+
					"      x[i]--;\n"+
					"  }}} \n"));
			
		assertEquals(expected, is);		
	}
	
	@Test
	public void inlineNoWiki() {
		Document is = Creole.parse("Some examples of markup are: {{{** <i>this</i> ** }}}");

		Document expected = (Document) new Document()			
			.addAll(Text.asArray("Some examples of markup are: "))
			.add(new Preformatted("** <i>this</i> ** "));
			
		assertEquals(expected, is);		
	}
}
