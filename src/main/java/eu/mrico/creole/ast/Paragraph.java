package eu.mrico.creole.ast;

import eu.mrico.creole.Visitor;


public class Paragraph extends BaseElement {

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
}
