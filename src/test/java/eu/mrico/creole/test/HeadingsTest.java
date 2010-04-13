package eu.mrico.creole.test;

import org.junit.Test;

import static org.junit.Assert.*;

import eu.mrico.creole.Creole;
import eu.mrico.creole.ast.Document;
import eu.mrico.creole.ast.Heading;
import eu.mrico.creole.ast.Paragraph;
import eu.mrico.creole.ast.Text;

/**
 * @see http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-Headings
 */
public class HeadingsTest {
    
    @Test   
    public void level1WithClosing() {
        Document is = Creole.parse("= Level 1 (largest) =");
        
        Document expected = (Document) new Document()
            .add(new Heading(1, "Level 1 (largest)"));
        
        assertEquals(expected, is);
    }
    
    @Test
    public void level2WithoutClosing() {
        Document is = Creole.parse("== Level 2\n" +
                "Test.");
        
        Document expected = (Document) new Document()
            .add(new Heading(2, "Level 2"))
                        .add(new Paragraph()
                            .addAll(Text.asArray("Test.")));
        
        assertEquals(expected, is);
    }
}
