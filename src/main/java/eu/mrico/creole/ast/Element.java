package eu.mrico.creole.ast;

import java.util.Collection;

import eu.mrico.creole.Visitor;


public interface Element extends Iterable<Element> {
	Element getParent();
	Element add(Element elem);
	Element addAll(Element[] elements);
	Element addAll(Collection<? extends Element> elements);
	
	void accept(Visitor visitor);
		
}
