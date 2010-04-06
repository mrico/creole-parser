package eu.mrico.creole.ast;

import eu.mrico.creole.Visitor;


public class List extends BaseElement {
	
	public static final int UNORDERED = 0;
	public static final int ORDERED = 1;
	
	private static final Class<?> VALID_ELEMENTS[] = new Class<?>[] {		
		ListItem.class
	};
	
	private final int style;
	
	public int getStyle() {
		return style;
	}
	
	public List() {
		this(UNORDERED);
	}
	
	public List(int style) {
		this.style = style;
	}
	
	@Override
	public Element add(Element elem) {	
		checkElement(elem);
		return super.add(elem);
	}
	
	private void checkElement(Element elem) {
		for(Class<?> type : VALID_ELEMENTS) {
			if(elem.getClass() == type)
				return;
		}
		throw new IllegalArgumentException("Illegal child");
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + style;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		List other = (List) obj;
		if (style != other.style)
			return false;
		return true;
	}

	
	
}
