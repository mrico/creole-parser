package eu.mrico.creole.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.mrico.creole.Creole;
import eu.mrico.creole.ast.Document;
import eu.mrico.creole.ast.HorizontalRule;
import eu.mrico.creole.ast.Image;
import eu.mrico.creole.ast.LineBreak;
import eu.mrico.creole.ast.Link;
import eu.mrico.creole.ast.Paragraph;
import eu.mrico.creole.ast.Text;

/**
 * @see http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-LineBreaks
 */
public class ImageTest {
	
	@Test	
	public void imageWithAlt() {
		Document is = Creole.parse("{{myimage.png|this is my image}}");
		
		Document expected = (Document) new Document()						
			.add(new Image("myimage.png", "this is my image"));
						
		assertEquals(expected, is);		
	}
	
	@Test	
	public void imageWithoutAlt() {
		Document is = Creole.parse("{{myimage.png}}");
		
		Document expected = (Document) new Document()						
			.add(new Image("myimage.png", "myimage.png"));
						
		assertEquals(expected, is);		
	}
}
