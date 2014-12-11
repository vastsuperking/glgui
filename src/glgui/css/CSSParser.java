package glgui.css;

import static glgui.css.Constants.*;
import glgui.css.eval.CSSValueEvaluator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class CSSParser {
	private List<Rule> m_rules = new ArrayList<Rule>();
	private HashMap<Selectors, Rule> m_ruleMap = new HashMap<Selectors, Rule>();
	
	private List<String> m_previousTokens = new ArrayList<String>();
	//Includes spaces too, but not new lines
	private List<String> m_allPreviousTokens = new ArrayList<String>();
	
	private CSSTokenizer m_tokenizer = new CSSTokenizer();

	private CSSValueEvaluator m_evaluator = new CSSValueEvaluator();
	
	//The stack of current selector sets (a new set for each rule)
	private Stack<Selectors> m_selectors = new Stack<Selectors>();
	//The stack of rules
	private Stack<Rule> m_ruleStack = new Stack<Rule>();
	
	private Rule m_currentRule = new Rule();
	private boolean m_inComment = false;
	
	public CSSValueEvaluator getEvaluator() { return m_evaluator; }
	public List<Rule> getRules() { return m_rules; }
	public HashMap<Selectors, Rule> getRuleMap() { return m_ruleMap; }
	
	public void parse(String s) {
		char[] chars = s.toCharArray();
		for (char c : chars) process(c);
	}
	
	public void process(char c) {
		m_tokenizer.process(c);
		while (m_tokenizer.isTokenAvailable()) {
			String token = m_tokenizer.getToken();
			processToken(token);
		}
	}
	
	public void processToken(String token) {
		//Ignore space tokens
		if (token.trim().length() == 0) { 
			m_allPreviousTokens.add(token);
			return;
		} else if (token.trim().equals(Character.toString(NEW_LINE))) return;
		
		//If in comment, do not process the token
		if (m_inComment) {
			if (token.equals(END_BLOCK_COMMENT)) m_inComment = false;
			flushTokens();
			return;
		} else if (token.equals(START_BLOCK_COMMENT)) {
			m_inComment = true;
			flushTokens();
			return;
		}
		
		if (token.equals(Character.toString(BRACES_BEG))) {
			//Start a new rule using previous tokens
			Selectors newSelectors = s_parseSelector(m_previousTokens);
			
			m_selectors.push(newSelectors);
			
			//Compose big set of all selectors
			Selectors selectors = new Selectors();
			for (Selectors s : m_selectors) {
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
		} else if (token.equals(Character.toString(BRACES_END))) {		
			//Pop the previous selector set
			m_selectors.pop();
			if (!m_ruleStack.isEmpty()) m_currentRule = m_ruleStack.pop();
		} else if (token.equals(Character.toString(SEMICOLON))) {
			StringBuilder name = new StringBuilder();
			StringBuilder value = new StringBuilder();
			
			int colonIndex = 0;
			//Go backwards and concatenate tokens until the : to get the value
			for (int i = m_allPreviousTokens.size() - 1; i >= 0; i--) {
				String t = m_allPreviousTokens.get(i);
				if (t.equals(Character.toString(COLON))) {
					colonIndex = i;
					break;
				}
				//Insert at the beginning
				value.insert(0, m_allPreviousTokens.get(i));
			}
			//Now go backwards to find the name
			for (int i = colonIndex - 1; i >= 0; i--) {
				String t = m_allPreviousTokens.get(i);
				if (t.trim().length() != 0) name.append(t);
			}
			String val = value.toString().trim();
			
			CSSDeclaration declaration = new CSSDeclaration();
			declaration.setName(name.toString());
			declaration.setValue(val);
			declaration.setObjectValue(m_evaluator.evaluate(val));
			m_currentRule.addDeclaration(declaration);
			flushTokens();
		} else {
			m_allPreviousTokens.add(token);
			m_previousTokens.add(token);
		}
	}
	public void flushTokens() {
		m_previousTokens.clear();
		m_allPreviousTokens.clear();
	}
	
	public static Selectors s_parseSelector(List<String> tokens) {
		Selectors selectors = new Selectors();
		//If we encounter a state selector (after :, eg .foo:hovered)
		Selector previousSelector = null;
		boolean attachStateSelector = false; 
		for (String s : tokens) {
			if (attachStateSelector && previousSelector != null) {
				previousSelector.addState(s);
				continue;
			}
			if (s.equals(Character.toString(COLON))) {
				attachStateSelector = true;
				continue;
			}
			Selector sel = new Selector(s);
			selectors.add(sel);
			previousSelector = sel;
		}
		return selectors;
	}
}
