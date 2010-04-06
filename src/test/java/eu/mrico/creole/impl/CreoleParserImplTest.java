package eu.mrico.creole.impl;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Arrays;

import org.junit.Test;

import eu.mrico.creole.CreoleException;
import eu.mrico.creole.CreoleParser;
import eu.mrico.creole.ast.Bold;
import eu.mrico.creole.ast.Document;
import eu.mrico.creole.ast.Italic;
import eu.mrico.creole.ast.LineBreak;
import eu.mrico.creole.ast.List;
import eu.mrico.creole.ast.ListItem;
import eu.mrico.creole.ast.Paragraph;
import eu.mrico.creole.ast.Text;

public class CreoleParserImplTest {
	
	@Test
	public void creoleTestCase() throws CreoleException {
		InputStream in = getClass().getResourceAsStream("/creole1.0test.txt");
		assertNotNull(in);
		
		CreoleParser parser = new CreoleParserImpl();
		Document doc = parser.parse(in);
		
//		new ASTDebugger().visit(doc);
	}
	
	
	@Test
	public void creoleItalicAndBold() throws CreoleException {
		final String s = "**//bold and italich//**Normal.\\\\This should be printed in a new line.";
				
		CreoleParser parser = new CreoleParserImpl();
		Document doc = parser.parse(new StringReader(s));
		
		Document expectedDocument = new Document();
		expectedDocument
			.add(new Bold()
				.add(new Italic()					
					.addAll(Arrays.asList(Text.asArray("bold and italich")))
				))
			.addAll(Arrays.asList(Text.asArray("Normal.")))
			.add(new LineBreak())			
			.addAll(Arrays.asList(Text.asArray("This should be printed in a new line.")));
		
		
		assertEquals(expectedDocument, doc);
		
//		PrintWriter writer = new PrintWriter(System.out);
//		new ASTDebugger(writer).visit(doc);
//		
//		writer.close();
	}
	
	@Test
	public void creoleList() throws CreoleException {
		final String s = "* Item 1\n" +
				"* Item 2\n" +
				"* Item 3\n" +
				"with multiple line\n" +
				"* Item 4\n\n" +
				"Not in list:";
				
		CreoleParser parser = new CreoleParserImpl();
		Document doc = parser.parse(new StringReader(s));
		
		Document expectedDocument = (Document) new Document()
			.add(new List()
				.add(new ListItem().addAll(Arrays.asList(Text.asArray("Item 1"))))
				.add(new ListItem().addAll(Arrays.asList(Text.asArray("Item 2"))))
				.add(new ListItem().addAll(Arrays.asList(Text.asArray("Item 3with multiple line"))))
				.add(new ListItem().addAll(Arrays.asList(Text.asArray("Item 4"))))
			)
			.addAll(Arrays.asList(Text.asArray(" Not in list:"))					
			);
		
//		new ASTDebugger().visit(doc);
		
		assertEquals(expectedDocument, doc);
	}
	
}
