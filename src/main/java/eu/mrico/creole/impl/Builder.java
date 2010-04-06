package eu.mrico.creole.impl;

import java.io.IOException;

import eu.mrico.creole.CreoleException;
import eu.mrico.creole.ast.Element;

abstract class Builder<T> {
	
	protected CharacterReader reader;
	protected Element currentParent;
	
	protected Character currentCharacter;
		
	public Builder(CharacterReader reader) throws IOException {
		super();
		this.reader = reader;		
		this.currentCharacter = reader.peek();
	}

	public abstract T build() throws CreoleException;
	
	protected Character next() throws IOException {
		this.currentCharacter = reader.next();
		return currentCharacter;
	}
	
	protected Character peek() throws IOException {
		return reader.peek();
	}
	
	protected String peek(int c) throws IOException {
		return reader.peek(c);
	}

}
