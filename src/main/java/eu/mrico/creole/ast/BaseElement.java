package eu.mrico.creole.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class BaseElement implements Element {
	
	protected Element parent;
	protected List<Element> children = new ArrayList<Element>();
		
	@Override
	public Element getParent() {
		return parent;
	}
			
	public Element add(Element elem) {		
		this.children.add(elem);
		((BaseElement) elem).parent = this;
		
		return this;
	}
	
	public Element addAll(Element[] elements) {		
		return addAll(Arrays.asList(elements));
	}
	
	public Element addAll(Collection<? extends Element> elements) {		
		this.children.addAll(elements);
		for(Element elem : elements)
			((BaseElement) elem).parent = this;
		
		return this;
	}
	
	public void removeElement(Element elem) {
		this.children.remove(elem);
	}
	
	@Override
	public Iterator<Element> iterator() {				
		return children.listIterator();
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((children == null) ? 0 : children.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseElement other = (BaseElement) obj;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		}
		return true;		
	}

        @Override
        public void clean() {
            Iterator<Element> it = children.iterator();
            while(it.hasNext()) {
                Element elem = it.next();
                
                if(elem.canClean()) {
                    it.remove();
                } else {
                    elem.clean();
                }
            }
        }

        @Override
        public boolean canClean() {
            return false;
        }

	@Override
	public String toString() {
		String name = getClass().getName();
		return name + " { " + children.toString() + " }"; 
	}
	
}
