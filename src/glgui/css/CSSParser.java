package glgui.css;

import static glgui.css.Constants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class CSSParser {
	private List<Rule> m_rules = new ArrayList<Rule>();
	private HashMap<SelectorSet, Rule> m_ruleMap = new HashMap<SelectorSet, Rule>();
	
	private List<String> m_previousTokens = new ArrayList<String>();
	private CSSTokenizer m_tokenizer = new CSSTokenizer();

	
	//The stack of current selector sets (a new set for each rule)
	private Stack<SelectorSet> m_selectors = new Stack<SelectorSet>();
	//The stack of rules
	private Stack<Rule> m_ruleStack = new Stack<Rule>();
	
	private Rule m_currentRule = new Rule();
	private String m_paramName;
	private boolean m_inComment = false;
	
	public List<Rule> getRules() { return m_rules; }
	public HashMap<SelectorSet, Rule> getRuleMap() { return m_ruleMap; }
	
	public void process(char c) {
		m_tokenizer.process(c);
		while (m_tokenizer.isTokenAvailable()) {
			String token = m_tokenizer.getToken();
			processToken(token);
		}
	}
	
	public void processToken(String token) {
		//Ignore space tokens
		if (token.trim().length() == 0 || token.trim().equals("\n")) return;
		
		//If in comment, do not process the token
		if (m_inComment) {
			if (token.equals(END_BLOCK_COMMENT)) m_inComment = false;
			return;
		} else if (token.equals(START_BLOCK_COMMENT)) {
			m_inComment = true;
			return;
		}
		
		if (token.equals(Character.toString(BRACES_BEG))) {
			//Start a new rule using previous tokens
			SelectorSet newSelectors = new SelectorSet();
			for (String s : m_previousTokens) {
				newSelectors.add(new Selector(s));
			}
			m_selectors.push(newSelectors);
			
			//Compose big set of all selectors
			SelectorSet selectors = new SelectorSet();
			for (SelectorSet s : m_selectors) {
				selectors.addAll(s);
			}
			
			//Flush previous tokens
			flushTokens();
			
			//Push the old rule onto the stack
			if (m_currentRule != null) m_ruleStack.push(m_currentRule);
			
			if (m_ruleMap.containsKey(selectors)) {
				//If a rule for this already exists, get that rule
				m_currentRule = m_ruleMap.get(selectors);
			} else {
				//Start a new rule
				m_currentRule = new Rule(selectors);
				m_ruleMap.put(selectors, m_currentRule);
				m_rules.add(m_currentRule);
			}
			
			//Find all rules the new rule can inherit parameters from
			nextRule:
			for (Rule r : m_rules) {
				for (Selector s : r.getSelectors()) {
					if (!m_currentRule.getSelectors().contains(s)) {
						//continue to next rule
						continue nextRule;
					}
				}
				//Inherit from rule
				m_currentRule.setAllParameters(r);
			}
		} else if (token.equals(Character.toString(BRACES_END))) {
			//Add any parameters to previous rules which should inherit from the current
			//Find all rules the new rule can inherit parameters from
			nextRule:
			for (Rule r : m_rules) {
				for (Selector s : m_currentRule.getSelectors()) {
					if (!r.getSelectors().contains(s)) {
						//continue to next rule
						continue nextRule;
					}
				}
				//Inherit from rule
				r.setAllParameters(m_currentRule);
			}
			
			//Pop the previous selector set
			m_selectors.pop();
			if (!m_ruleStack.isEmpty()) m_currentRule = m_ruleStack.pop();
		} else if (token.equals(Character.toString(COLON))){
			//Use the last token to initialize the paramName
			m_paramName = m_previousTokens.get(m_previousTokens.size() - 1);
			flushTokens();
		} else if (token.equals(Character.toString(SEMICOLON))) {
			//Use the paramName var to create a declaration
			String value = m_previousTokens.get(m_previousTokens.size() - 1);
			flushTokens();
			
			m_currentRule.setParameter(m_paramName, value);
		} else m_previousTokens.add(token);
	}
	
	public void flushTokens() {
		m_previousTokens.clear();
	}
}
