package eu.mrico.creole.impl;

import java.io.IOException;

import eu.mrico.creole.CreoleException;
import eu.mrico.creole.ast.Cell;
import eu.mrico.creole.ast.Element;
import eu.mrico.creole.ast.List;
import eu.mrico.creole.ast.ListItem;
import eu.mrico.creole.ast.Row;
import eu.mrico.creole.ast.Table;
import eu.mrico.creole.ast.Text;

class TableBuilder extends Builder<Table> {
	
	public TableBuilder(CharacterReader reader) throws IOException {
		super(reader);
	}
	
	@Override
	public Table build() throws CreoleException {	
			
		Table table = new Table();
		Row row = new Row();	
		Cell cell = new Cell();
		row.add(cell);
		
		table.add(row);
		
		try {
			
			if(peek() == '=') {
				cell.setHeading(true);
				reader.skip(1);
			}
			
			
			while(next() != null) {
				if(currentCharacter == '|') {		
					
					if(peek() == null)
						break;
					
					if(peek() == '\n') {
						reader.skip(1);
						
						if(peek() == null || peek() != '|')
							break;
						
						row = new Row();
						table.add(row);						
						
					} else {
						cell.trim();
						
						cell = new Cell();
						row.add(cell);
						
						if(peek() == '=') {
							cell.setHeading(true);
							reader.skip(1);
						}
					}
									
				} else {
					cell.add(new Text(currentCharacter));
				}
			}
			cell.trim();
			
			return table;
		
		} catch(IOException e) {
			throw new CreoleException(e);
		}
	}
	
}
