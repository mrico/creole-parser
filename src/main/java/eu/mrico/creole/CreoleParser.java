package eu.mrico.creole;

import java.io.InputStream;
import java.io.Reader;

import eu.mrico.creole.ast.Document;

public interface CreoleParser {

	Document parse(Reader reader) throws CreoleException;
	
	Document parse(InputStream in) throws CreoleException;
	
}
