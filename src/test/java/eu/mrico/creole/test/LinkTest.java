package eu.mrico.creole.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.mrico.creole.Creole;
import eu.mrico.creole.ast.Document;
import eu.mrico.creole.ast.Link;
import eu.mrico.creole.ast.Text;

/**
 * @see http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-LinksInternalExternalAndInterwiki
 */
public class LinkTest {
	
	@Test	
	public void simpleInternalLink() {
		Document is = Creole.parse("[[link]]");
		
		Document expected = (Document) new Document()
			.add(new Link("link", "link"));
		
		assertEquals(expected, is);
	}
	
	@Test	
	public void labeledInternalLink() {
		Document is = Creole.parse("[[MyBigPage|Go to my page]]");
		
		Document expected = (Document) new Document()
			.add(new Link("Go to my page", "MyBigPage"));
		
		assertEquals(expected, is);
	}
	
	@Test	
	public void externalLink() {
		Document is = Creole.parse("[[http://www.wikicreole.org/]]");
		
		Document expected = (Document) new Document()
			.add(new Link("http://www.wikicreole.org/", "http://www.wikicreole.org/"));
		
		assertEquals(expected, is);
	}
	
	@Test	
	public void labeledExternalLink() {
		Document is = Creole.parse("[[http://www.wikicreole.org/|Visit the WikiCreole website]]");
		
		Document expected = (Document) new Document()
			.add(new Link("Visit the WikiCreole website", "http://www.wikicreole.org/"));
		
		assertEquals(expected, is);
	}
	
	@Test	
	public void rawLinks() {
		Document is = Creole.parse("http://www.rawlink.org/, http://www.another.rawlink.org");
		
		Document expected = (Document) new Document()
			.add(new Link("http://www.rawlink.org/", "http://www.rawlink.org/"))
			.addAll(Text.asArray(", "))
			.add(new Link("http://www.another.rawlink.org", "http://www.another.rawlink.org"));
		
		assertEquals(expected, is);		
	}
}
