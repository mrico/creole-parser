package eu.mrico.creole.ast;

import eu.mrico.creole.Visitor;


public class Phrase extends BaseElement {

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
}
