package glgui.css;

import java.util.LinkedHashMap;

public class Rule {
	private Selectors m_selectors;
	
	private LinkedHashMap<String, CSSDeclaration> m_declarations = new LinkedHashMap<String, CSSDeclaration>();
	
	public Rule() {
		m_selectors = new Selectors();
	}
	public Rule(Selectors set) {
		m_selectors = set;
	}
	public Selectors getSelectors() { return m_selectors; }
	public LinkedHashMap<String, CSSDeclaration> getDeclarations() { return m_declarations; }
	
	public void addDeclaration(CSSDeclaration declaration) {
		m_declarations.put(declaration.getName(), declaration);
	}
	
	public void inheritFrom(Rule rule) {
		m_declarations.putAll(rule.getDeclarations());
	}
	
	public boolean canInheritFrom(Rule rule) {
		return m_selectors.canInheritFrom(rule.getSelectors());
	}
	
	public boolean appliesTo(CSSNode node) {
		return m_selectors.appliesTo(node);
	}
	public boolean appliesToWithoutStates(CSSNode node) {
		return m_selectors.appliesToWithoutStates(node);
	}
}
