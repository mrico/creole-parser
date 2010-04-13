package eu.mrico.creole.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.mrico.creole.Creole;
import eu.mrico.creole.ast.Document;
import eu.mrico.creole.ast.Link;
import eu.mrico.creole.ast.Text;

/**
 * @see http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-EscapeCharacter
 */
public class EscapeCharacterTest {
    
    @Test   
    public void escapeList() {
        Document is = Creole.parse("~#1");
        
        Document expected = (Document) new Document()           
            .addAll(Text.asArray("#1"));
                        
        assertEquals(expected, is);     
    }

        @Test
    public void linkWithEscapeCharacter() {
        Document is = Creole.parse("http://www.foo.com/~bar/");

        Document expected = (Document) new Document()
            .add(new Link("http://www.foo.com/~bar/"));

        assertEquals(expected, is);
    }

        @Test
    public void escapedRawLink() {
        Document is = Creole.parse("~http://www.foo.com/");

        Document expected = (Document) new Document()
            .add(new Text("http://www.foo.com/"));

        assertEquals(expected, is);
    }
        
}
