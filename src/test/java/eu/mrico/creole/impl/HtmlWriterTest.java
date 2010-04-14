package eu.mrico.creole.impl;

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.junit.Test;

import eu.mrico.creole.CreoleException;
import eu.mrico.creole.Creole;
import eu.mrico.creole.XHtmlWriter;
import eu.mrico.creole.ast.Document;

public class HtmlWriterTest {

    @Test
    public void htmlWrtier() throws CreoleException, FileNotFoundException {
        InputStream in = getClass().getResourceAsStream("/creole1.0test.txt");
        assertNotNull(in);

        Document doc = Creole.parse(in);

        XHtmlWriter writer = new XHtmlWriter();
        writer.addCssClass("p", new String[] { "creole", "test" });
        writer.addCssClass("pre", "code");
        
        writer.write(doc, new FileOutputStream("target/creole1.0test.html"));
    }
}
