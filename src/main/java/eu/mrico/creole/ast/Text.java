package eu.mrico.creole.ast;

import eu.mrico.creole.Visitor;


public class Text extends BaseElement {
	
	private String value;
	
	public Text(Character c) {
		this.value = c.toString();
	}
	
	public Text(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static Text[] asArray(String s) {
		char[] cs = s.toCharArray();
		Text[] arr = new Text[cs.length];
		for(int i=0; i < cs.length; i++)
			arr[i] = new Text(cs[i]);
		
		return arr;
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Text other = (Text) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Text[" + value + "]";
	}
	
}
