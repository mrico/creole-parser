package eu.mrico.creole.test;

import eu.mrico.creole.ASTDebugger;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.mrico.creole.Creole;
import eu.mrico.creole.ast.Document;
import eu.mrico.creole.ast.Link;
import eu.mrico.creole.ast.Paragraph;
import eu.mrico.creole.ast.Text;

/**
 * @see http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-Paragraphs
 */
public class ParagraphTest {
    
    @Test   
    public void paragraph() {
        Document is = Creole.parse("This is my text.\n\n" + 
                "This is more text.\n" +
                "I'm not a new paragraph.");
        
        Document expected = (Document) new Document()           
            .addAll(Text.asArray("This is my text."))
            .add(new Paragraph()
                .addAll(Text.asArray("This is more text. I'm not a new paragraph.")));
                
        assertEquals(expected, is);     
    }
}
