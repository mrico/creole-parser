package eu.mrico.creole.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.Deque;

class CharacterReader {

	private Reader reader;
		
	private Deque<Character> stack = new ArrayDeque<Character>();
	
	public CharacterReader(InputStream in) {
		this.reader = new InputStreamReader(in);
	}
	
	public CharacterReader(Reader reader) {
		this.reader = reader;
	}
	
	public String peek(int c) throws IOException {
		StringBuffer sb = new StringBuffer();
		for(int i=0; i < c; i++) {
			Character cc = next(true);
			if(cc == null)
				break;
			
			sb.append(cc);
		}
				
		for(int i=0; i < sb.length(); i++)
			stack.addLast(sb.charAt(i));
				
		return sb.toString();
	}
	
	public Character peek() throws IOException {
		Character c = next(true);
		if(c == null)
			return null;
		
		stack.addFirst(c);
			
		return c;		
	}
	
	public Character readAhead() throws IOException {
		Character c = next(false);
		if(c == null)
			return null;
		
		stack.addLast(c);
		
		return c;		
	}
	
	public void skip(int c) throws IOException {		
		for(int i=0; i < c; i++)
			next();			
	}

        public void push(char c) throws IOException {
            stack.addLast(c);
        }
		
	public Character next() throws IOException {
		return next(true);
	}
	
	public Character next(boolean withStack) throws IOException {
		if(withStack && ! stack.isEmpty()) {
			return stack.pop();
		}
			
		int c = reader.read();
		if(c == -1)
			return null;
		if(c == '\r')
			return next();
		
		return new Character((char) c);			
	}
	
}
