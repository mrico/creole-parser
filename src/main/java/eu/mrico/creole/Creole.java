package eu.mrico.creole;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import eu.mrico.creole.ast.Document;

public class Creole {
	
	public static Document parse(String s) {		
		try {
			CreoleParser parser = CreoleParserFactory.newInstance().newParser();
			return parser.parse(new StringReader(s));
			
		} catch (CreoleException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Document parse(InputStream in) {		
		try {
			CreoleParser parser = CreoleParserFactory.newInstance().newParser();
			return parser.parse(new InputStreamReader(in));
			
		} catch (CreoleException e) {
			throw new RuntimeException(e);
		}
	}
		
}
