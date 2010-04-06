package eu.mrico.creole.impl;

import java.io.IOException;

import eu.mrico.creole.CreoleException;
import eu.mrico.creole.ast.List;
import eu.mrico.creole.ast.ListItem;
import eu.mrico.creole.ast.Text;

class ListBuilder extends Builder<List> {

	private final int level;		
	private final String symbol;
	
	public ListBuilder(CharacterReader reader, String symbol, int level) throws IOException {
		super(reader);
		this.level = level;
		this.symbol = symbol;
	}
	
	@Override
	public List build() throws CreoleException {	
			
		List list = new List("#".equals(symbol) ? List.ORDERED : List.UNORDERED);		
		ListItem item = new ListItem();
		
		list.add(item);
		currentParent = item;
		
		try {
			skipWithspaces();
			while(next() != null) {			
				if(currentCharacter == '\n') {
					if(peek() == null)
						break;
					
					if(peek() == ' ')
						reader.skip(1);
					
					String s = peek(level+1);
					if(s.matches(regex(level, 1))) {
						reader.skip(level);						
						skipWithspaces();
						
						item = new ListItem();
						list.add(item);
						currentParent = item;
						
					} else if(s.matches(regex(level + 1, 0))) {
						reader.skip(level + 1);
						
						item = new ListItem();
						list.add(item);
						currentParent = item;
						
						item.add(new ListBuilder(reader, symbol, level + 1).build());
						if(peek() == null || peek() == '\n')
							break;
							
						skipWithspaces();
						
						item = new ListItem();
						list.add(item);
						currentParent = item;
						
					} else if(level > 1 && s.matches(regex(level - 1, 2))) {	
						reader.skip(level - 1);
						break;
						
					} else if(peek() == '\n') {
						break;
						
					} else {
//						currentParent.add(new LineBreak());
					}
					
				} else if(currentCharacter == '*') {
	//		TODO		handleStar();
					
				} else if(currentCharacter == '/') {
	//		TODO		handleSlash();
					
				} else {			
					currentParent.add(new Text(currentCharacter));
				}
			}
					
			return list;
			
		} catch(IOException e) {
			throw new CreoleException(e);
		}
	}
	
	private String regex(int i, int remain) {		
		String s = "\\" + symbol;					
		String regex =  s + "{" + i + "}";
		if(remain > 0)
			regex = regex + "[^" + s + "]{" + remain + "}";
				
		return regex;
	}
	
	private void skipWithspaces() throws IOException {		
		if(peek() == ' ')
			reader.skip(1);
		
		while(reader.readAhead() == ' ')
			reader.skip(1);
		
	}
}
