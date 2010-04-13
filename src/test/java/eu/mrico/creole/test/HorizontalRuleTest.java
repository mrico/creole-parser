package eu.mrico.creole.test;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.mrico.creole.Creole;
import eu.mrico.creole.ast.Document;
import eu.mrico.creole.ast.HorizontalRule;
import eu.mrico.creole.ast.LineBreak;
import eu.mrico.creole.ast.Link;
import eu.mrico.creole.ast.Paragraph;
import eu.mrico.creole.ast.Text;

/**
 * @see http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-LineBreaks
 */
public class HorizontalRuleTest {
    
    @Test   
    public void horizontalRule() {
        Document is = Creole.parse("----");
        
        Document expected = (Document) new Document()                       
            .add(new HorizontalRule());
                        
        assertEquals(expected, is);     
    }
}
