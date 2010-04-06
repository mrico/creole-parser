package eu.mrico.creole.ast;

import eu.mrico.creole.Visitor;


public class Table extends BaseElement {

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
}
