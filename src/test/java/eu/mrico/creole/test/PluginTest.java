package eu.mrico.creole.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.mrico.creole.Creole;
import eu.mrico.creole.ast.Document;
import eu.mrico.creole.ast.Plugin;

/**
 * @see http://www.wikicreole.org/wiki/CreoleAdditions#section-CreoleAdditions-PlugInExtension
 */
public class PluginTest {
    
    @Test   
    public void simplePluginWithoutArgs() {
        Document is = Creole.parse("<<SimplePlugin>>");
        
        Document expected = (Document) new Document()                       
            .add(new Plugin("SimplePlugin"));
            
        assertEquals(expected, is);     
    }
}
