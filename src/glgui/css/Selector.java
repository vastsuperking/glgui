package glgui.css;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Selector {
	public enum SelectorType {
		ID,
		CLASS,
		TAG //No prefix infront of selector
	}
	
	private SelectorType m_type;
	private String m_name;
	private Set<String> m_states = new HashSet<String>();
	
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
			m_type = SelectorType.TAG;
			m_name = cssSelector;
		}
	}
	
	public Selector(SelectorType type, String name) {
		m_type = type;
		m_name = name;
	}
	
	public String getName() { return m_name; }
	public Set<String> getStates() { return m_states; }
	public SelectorType getType() { return m_type; }
	
	public void addState(String state) {
		m_states.add(state);
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(getType() + " " + getName());
		for (String state : m_states) {
			s.append(Constants.COLON).append(state);
		}
		return s.toString();
	}
	
	public boolean equals(Object o) {
		if (o instanceof Selector) {
			Selector s = (Selector) o;
			return s.getName().equals(m_name) && s.getType() == getType() && s.getStates().equals(m_states);
		}
		return false;
	}
	public boolean appliesToWithoutStates(CSSNode n) {
		switch(m_type) {
		case TAG: return n.getTag().equals(m_name);
		case CLASS: return n.getClasses().contains(m_name);
		case ID: return n.getID().equals(m_name);
		default: throw new RuntimeException("Unknown: " + m_type);
		}		
	}
	public boolean appliesTo(CSSNode n) {
		//First make sure all states apply
		for (String state : m_states) {
			if (!n.inState(state)) return false;
		}
		switch(m_type) {
		case TAG: return n.getTag().equals(m_name);
		case CLASS: return n.getClasses().contains(m_name);
		case ID: return n.getID().equals(m_name);
		default: throw new RuntimeException("Unknown: " + m_type);
		}
	}
	
	public int hashCode() {
		return Objects.hash(m_type, m_name, m_states);
	}
}
