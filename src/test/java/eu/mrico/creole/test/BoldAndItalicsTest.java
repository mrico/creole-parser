package eu.mrico.creole.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.mrico.creole.Creole;
import eu.mrico.creole.ast.Bold;
import eu.mrico.creole.ast.Document;
import eu.mrico.creole.ast.Italic;
import eu.mrico.creole.ast.Paragraph;
import eu.mrico.creole.ast.Text;

/**
 * @see http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-BoldAndItalics
 */
public class BoldAndItalicsTest {
	
	@Test
	public void bold() {
		Document is = Creole.parse("**bold**");
		Document expected = (Document) new Document()
			.add(new Bold()
				.addAll(Text.asArray("bold")
			));
		
		assertEquals(expected, is);
	}
	
	@Test
	public void boldWithMultiline() {
		Document is = Creole.parse("Bold should **be\n" +
				"able** to cross lines.");
		
		Document expected = (Document) new Document()
			.addAll(Text.asArray("Bold should "))
			.add(new Bold()
				.addAll(Text.asArray("be able")))
			.addAll(Text.asArray(" to cross lines.")
			);
				
		assertEquals(expected, is);
	}
	
	@Test
	public void boldAndNewParagraph() {
		Document is = Creole.parse("But, should **not be...\n\n" +
				"...able** to cross paragraphs.");
		
		Document expected = (Document) new Document()
			.addAll(Text.asArray("But, should "))
			.add(new Bold()
				.addAll(Text.asArray("not be...")))
			.add(new Paragraph()
				.addAll(Text.asArray("...able"))
				.add(new Bold()
					.addAll(Text.asArray(" to cross paragraphs.")))
			);
						
		assertEquals(expected, is);
	}
	
	@Test
	public void italics() {
		Document is = Creole.parse("//italics//");
		Document expected = (Document) new Document()
			.add(new Italic()
				.addAll(Text.asArray("italics")
			));
		
		assertEquals(expected, is);
	}

	@Test
	public void italicsWithMultiline() {
		Document is = Creole.parse("Italics should //be\n" +
				"able// to cross lines.");
		
		Document expected = (Document) new Document()
			.addAll(Text.asArray("Italics should "))
			.add(new Italic()
				.addAll(Text.asArray("be able")))
			.addAll(Text.asArray(" to cross lines.")
			);
				
		assertEquals(expected, is);
	}
	
	@Test
	public void italicsAndNewParagraph() {
		Document is = Creole.parse("But, should //not be...\n\n" +
				"...able// to cross paragraphs.");
		
		Document expected = (Document) new Document()
			.addAll(Text.asArray("But, should "))
			.add(new Italic()
				.addAll(Text.asArray("not be...")))
			.add(new Paragraph()
				.addAll(Text.asArray("...able"))
				.add(new Italic()
					.addAll(Text.asArray(" to cross paragraphs.")))
			);
						
		assertEquals(expected, is);
	}

	@Test
	public void boldAndItalics() {
		Document is = Creole.parse("**//bold italics//**");
		Document expected = (Document) new Document()
			.add(new Bold().add(new Italic()				
				.addAll(Text.asArray("bold italics")
			)));
		
		assertEquals(expected, is);
	}
	
	@Test
	public void italicsAndBold() {
		Document is = Creole.parse("//**bold italics**//");
		Document expected = (Document) new Document()
			.add(new Italic().add(new Bold()				
				.addAll(Text.asArray("bold italics")
			)));
		
		assertEquals(expected, is);
	}		
}
