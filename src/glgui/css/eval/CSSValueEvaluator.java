package glgui.css.eval;

import glgui.css.eval.CSSOperation.CSSBooleanOperation;
import glgui.css.eval.CSSOperation.CSSColorOperation;
import glgui.css.eval.CSSOperation.CSSFunctionOperation;
import glgui.css.eval.CSSOperation.CSSIntegerOperation;
import glgui.css.eval.CSSOperation.CSSStringLiteralOperation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Will evaluate the value part of a css property
 * @author Daniel Pfrommer
 *
 */
public class CSSValueEvaluator {
	private HashMap<String, CSSFunction> m_functions = new HashMap<String, CSSFunction>();

	private ArrayList<CSSOperation> m_operations = new ArrayList<CSSOperation>();
	
	public HashMap<String, CSSFunction> getFunctions() { return m_functions; }
	public ArrayList<CSSOperation> getOperations() { return m_operations; }
	
	public CSSValueEvaluator() {
		addOperation(new CSSFunctionOperation());
		
		addOperation(new CSSColorOperation());
		addOperation(new CSSIntegerOperation());
		addOperation(new CSSBooleanOperation());
		
		addOperation(new CSSStringLiteralOperation());
	}
	public void addFunction(CSSFunction function) {
		m_functions.put(function.getSignature(), function);
	}
	
	public void addOperation(CSSOperation operation) {
		m_operations.add(operation);
	}
	
	public Object evaluate(String value) {
		for (CSSOperation o : m_operations) {
			if (o.matches(value)) {
				return o.process(value.trim(), this);
			}
		}
		//Could not be processed
		throw new RuntimeException("Could not evaluate: " + value);
	}
}
