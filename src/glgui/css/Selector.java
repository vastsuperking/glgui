package glgui.css;

import java.util.Objects;

public class Selector {
	public enum SelectorType {
		ID,
		CLASS,
		DEFAULT //No prefix infront of selector
	}
	
	private SelectorType m_type;
	private String m_name;
	
	/**
	 * Will parse the css selector(eg. ".foo" - type = CLASS and name = "foo")
	 * @param cssSelector the CSS Selector to parse
	 */
	public Selector(String cssSelector) {
		if (cssSelector.startsWith(".")) {
			m_type = SelectorType.CLASS;
			m_name = cssSelector.substring(1);
		} else if (cssSelector.startsWith("#")) {
			m_type = SelectorType.ID;
			m_name = cssSelector.substring(1);
		} else {
			m_type = SelectorType.DEFAULT;
			m_name = cssSelector;
		}
	}
	
	public Selector(SelectorType type, String name) {
		m_type = type;
		m_name = name;
	}
	
	public String getName() { return m_name; }
	public SelectorType getType() { return m_type; }
	
	public String toString() {
		return getType() + " " + getName();
	}
	
	public boolean equals(Object o) {
		if (o instanceof Selector) {
			Selector s = (Selector) o;
			return s.getName().equals(m_name) && s.getType() == getType();
		}
		return false;
	}
	
	public int hashCode() {
		return Objects.hash(m_type, m_name);
	}
}
