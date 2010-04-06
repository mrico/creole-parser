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
		InputStream in = getClass().getResourceAsStream("/wikicreole1.txt");
		assertNotNull(in);
				
		Document doc = Creole.parse(in);
				
		new XHtmlWriter().write(doc, new FileOutputStream("creole1.0test.html"));
	}
	
}
