package glgui.css;

import java.util.HashMap;

public class Rule {
	private SelectorSet m_selectors;
	
	private HashMap<String, String> m_parameters = new HashMap<String, String>();
	
	public Rule() {
		m_selectors = new SelectorSet();
	}
	public Rule(SelectorSet set) {
		m_selectors = set;
	}
	public SelectorSet getSelectors() { return m_selectors; }
	public HashMap<String, String> getParameters() { return m_parameters; }
	
	public void setParameter(String param, String value) {
		m_parameters.put(param, value);
	}
	public void setAllParameters(Rule rule) {
		m_parameters.putAll(rule.getParameters());
	}
}
