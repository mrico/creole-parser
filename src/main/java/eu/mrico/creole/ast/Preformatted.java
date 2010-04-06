package eu.mrico.creole.ast;

import eu.mrico.creole.Visitor;

public class Preformatted extends Text {
	
	public Preformatted(String value) {
		super(value);
	}
	
	@Override
	public void accept(Visitor visitor) {	
		visitor.visit(this);
	}

}
