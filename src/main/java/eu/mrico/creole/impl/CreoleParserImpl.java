package eu.mrico.creole.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import eu.mrico.creole.CreoleException;
import eu.mrico.creole.CreoleParser;
import eu.mrico.creole.ast.Bold;
import eu.mrico.creole.ast.Document;
import eu.mrico.creole.ast.Element;
import eu.mrico.creole.ast.Heading;
import eu.mrico.creole.ast.HorizontalRule;
import eu.mrico.creole.ast.Image;
import eu.mrico.creole.ast.Italic;
import eu.mrico.creole.ast.LineBreak;
import eu.mrico.creole.ast.Link;
import eu.mrico.creole.ast.Paragraph;
import eu.mrico.creole.ast.Preformatted;
import eu.mrico.creole.ast.Text;

class CreoleParserImpl implements CreoleParser {

	private CharacterReader reader;
		
	private Document document;
	private Element currentParent;
	private Character currentCharacter;
	
	@Override
	public Document parse(InputStream in) throws CreoleException {
		return parse(new InputStreamReader(in));
	}
	
	@Override
	public Document parse(Reader reader) throws CreoleException {		
		this.document = new Document();
		
		this.reader = new CharacterReader(reader);		
		this.currentParent = document;
		
		try {
			while(next() != null) {				
				switch(currentCharacter) {				
				case '=':
					handleEqual();
					break;
				case '#':
					currentParent.add(new ListBuilder(this.reader, "#", 1).build());
					break;
				case '*':
					handleStar();
					break;
				case '/':
					handleSlash();
					break;
				case '[':
					handleLeftBracket();
					break;
				case '-':
					handleMinus();
					break;								
				case '{':
					handleCurlyBracket();
					break;
				case '|':
					currentParent.add(new TableBuilder(this.reader).build());
					break;
				case '\\':						
					if(peek() == '\\') {
						this.reader.skip(1);
						this.currentParent.add(new LineBreak());
						break;
					}											
				case 'h':					
					if(isExternalLink()) {
						parseExternalLink();						
					} else {
						this.currentParent.add(new Text(currentCharacter));
					}
					break;
				case '\n':
					handleNewLine();
					break;
				default:					
					this.currentParent.add(new Text(currentCharacter));					
				}
			}
		} catch(IOException e) {
			throw new CreoleException(e);
		}
		
		return document;
	}

	private void handleCurlyBracket() throws IOException {
		if(currentCharacter != '{')
			throw new IllegalStateException();
		
		String s = reader.peek(2);
		if("{{".equals(s)) {			
			reader.skip(2);
		
			StringBuffer sb = new StringBuffer();
			while(next() != null) {
				if(currentCharacter == '}') {			
					s = reader.peek(3);
					if("}}\n".equals(s)) {
						reader.skip(3);
						break;
					} 
				} 
				sb.append(currentCharacter);			
			}
			
			currentParent.add(new Preformatted(sb.toString()));
		} else if('{' == s.charAt(0)) {
			reader.skip(1);
			parseImage();
		}
	}

	private void parseImage() throws IOException {
		String alt = null;
		String src = null;
		
		StringBuffer sb = new StringBuffer();
		
		while(next() != null) {
			switch (currentCharacter) {
			case '|':
				src = sb.toString();
				sb = new StringBuffer();
				break;
			case '}':
				Character nextChar = peek();
				if(nextChar == '}') {
					reader.skip(1);
					if(src == null)
						src = sb.toString();
					if(alt == null && sb.length() > 0)
						alt = sb.toString();
					else {
						alt = src;
					}
					currentParent.add(new Image(src, alt));
					return;
				}
			default:
				sb.append(currentCharacter);
			}
		}
		
		throw new IllegalStateException();
	}

	private void handleMinus() throws IOException {
		if(currentCharacter != '-')
			throw new IllegalStateException();
		
		String s = reader.peek(4);
		if(s.matches("---\n?$")) {
			reader.skip(4);
			currentParent.add(new HorizontalRule());
		}
	}

	private void handleNewLine() throws IOException {
		if(currentCharacter != '\n')
			throw new IllegalStateException();
		
		Character nextChar = peek();
		if(nextChar == '\n') {
			reader.skip(1);
			
			currentParent = document;
						
			Paragraph p = new Paragraph();
			this.currentParent.add(p);
			this.currentParent = p;
		} else {
			currentParent.add(new Text(" "));
		}
	}

	private void handleEqual() throws IOException {
		if(currentCharacter != '=')
			throw new IllegalStateException();
				
		int level;
		String s = reader.peek(7);
		for(level = 1;s.charAt(level-1) == '='; level++);

		reader.skip(level-1);
				
		StringBuffer sb = new StringBuffer();
		while(next() != null && currentCharacter != '\n') {
			switch(currentCharacter) {			
			case '=':
				break;
			default:		
				sb.append(currentCharacter);
			}
		}
		
		if(currentParent instanceof Paragraph)
			currentParent = currentParent.getParent();
		
		currentParent.add(new Heading(level, sb.toString().trim()));		
	}

	private boolean isExternalLink() throws IOException {
		if(currentCharacter != 'h')
			return false;
		
		String s = reader.peek(6);		
		return "ttp://".equals(s);			
	}
	
	private void parseExternalLink() throws IOException {
		StringBuffer sb = new StringBuffer("h");
		
		while(next() != null) {
			final char c = currentCharacter;
			if(c == ',' || c == '!' || c == ' ')
				break;
			
			sb.append(currentCharacter);			
		}
		
		String s = sb.toString();
		s = s.replace("[,\\.\\?\\!:']$", "");
		currentParent.add(new Link(s, s));
		
		if(currentCharacter != null)
			currentParent.add(new Text(currentCharacter));
	}

	private void handleLeftBracket() throws IOException {
		if(currentCharacter != '[')
			throw new IllegalStateException();
		
		Character nextChar = peek();
		if(nextChar == '[') {
			reader.skip(1);
			currentParent.add(parseLink());
		}
	}

	private Link parseLink() throws IOException {	
		
		String label = null;
		String target = null;
		
		StringBuffer sb = new StringBuffer();
		
		while(next() != null) {
			switch (currentCharacter) {
			case '|':
				target = sb.toString();
				sb = new StringBuffer();
				break;
			case ']':
				Character nextChar = peek();
				if(nextChar == ']') {
					reader.skip(1);
					if(target == null)
						target = sb.toString();
					if(label == null && sb.length() > 0)
						label = sb.toString();
					else {
						label = target;
					}
					return new Link(label, target);
				}
			default:
				sb.append(currentCharacter);
			}
		}
		
		throw new IllegalStateException();
	}

	private void handleSlash() throws IOException {
		if(currentCharacter != '/')
			throw new IllegalStateException();
				
		Character nextChar = peek();		
		if(nextChar == '/') {
			reader.skip(1);
			if(currentParent instanceof Italic) {
				// end italic text
				this.currentParent = this.currentParent.getParent(); 
			} else {
				Italic italic = new Italic();
				currentParent.add(italic);
				this.currentParent = italic;
			}
		}
		
	}

	private void handleStar() throws IOException, CreoleException {
		if(currentCharacter != '*')
			throw new IllegalStateException();
				
		Character nextChar = peek();
		if(nextChar == '*') {
			reader.skip(1);
			if(currentParent instanceof Bold) {
				// end bold text
				this.currentParent = this.currentParent.getParent(); 
			} else {
				Bold bold = new Bold();
				currentParent.add(bold);
				this.currentParent = bold;
			}						
		} else {
			currentParent.add(new ListBuilder(reader, "*", 1).build());
		}
		
	}

	private Character next() throws IOException {
		this.currentCharacter = reader.next();		
		return this.currentCharacter;
	}
	
	private Character peek() throws IOException {
		return reader.peek();
	}
	
}
