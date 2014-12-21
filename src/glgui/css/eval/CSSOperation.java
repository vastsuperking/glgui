package glgui.css.eval;

import glcommon.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Represents a kind of operation in css
 */
public interface CSSOperation {
	public boolean matches(String value);
	public Object process(String value, CSSValueEvaluator eval);
	
	//Will execute a function with args
	public static class CSSFunctionOperation implements CSSOperation {
		@Override
		public boolean matches(String value) {
			return (value.indexOf('(') > 0 && value.lastIndexOf(')') > 0);
		}

		@Override
		public Object process(String value, CSSValueEvaluator eval) {
			if (!matches(value)) throw new RuntimeException(value + " is not a function call");
			int firstParenth = value.indexOf('(');
			int lastParenth = value.lastIndexOf(')');
			if (lastParenth != value.length() - 1) throw new RuntimeException(") is not the last char in " + value);
			
			String name = value.substring(0, firstParenth);
			String contents = value.substring(firstParenth + 1, lastParenth);
			
			List<String> args = new ArrayList<String>();
			
			//TODO: Find a better way:
			//So that we don't split functions called in arguments
			char[] chrs = contents.toCharArray();
			StringBuilder currentArg = new StringBuilder();
			int functionLevel = 0;
			for (char c : chrs) {
				if (c == '(') functionLevel++;
				else if (c == ')') functionLevel--;
				else if (c == ',' && functionLevel == 0) {
					args.add(currentArg.toString().trim());
					currentArg = new StringBuilder();
					//Skip to next char so we don't append it
					continue;
				}
				currentArg.append(c);
			}
			if (currentArg.length() != 0) args.add(currentArg.toString().trim());
			
			//Now evaluate the args
			List<Object> argValues = new ArrayList<Object>();
			List<Class<?>> argTypes = new ArrayList<Class<?>>();
			
			for (String arg : args) {
				Object val = eval.evaluate(arg);
				argValues.add(val);
				argTypes.add(val.getClass());
			}
			
			String signature = CSSFunction.s_createSignature(name, argTypes);
			CSSFunction func = eval.getFunctions().get(signature);
			
			if (func == null) throw new RuntimeException("Could not find function matching: " + signature);
			
			return func.call(argValues);
		}	
	}
	//Will parse a color from
	// #RRGGBB (in hex)
	// rgb(255, 255, 255) (0 -> 255)
	// rgb(100%, 100%, 100%) (0 -> 100)
	// or for RGBA:
	// #RRGGBBAA (in hex)
	// rgba(255, 255, 255, 255) (0 -> 255)
	// rgba(100%, 100%, 100%, 100%) (0 -> 100)
	public static class CSSColorOperation implements CSSOperation {
		private static HashMap<String, Color> s_colors = new HashMap<String, Color>();
		
		static {
			//TODO: Move to config  file
			s_colors.put("white", new Color(1, 1, 1));
			s_colors.put("black", new Color(0, 0, 0));
			s_colors.put("red", new Color(1, 0, 0));
			s_colors.put("green", new Color(0, 1, 0));
			s_colors.put("blue", new Color(0, 0, 1));
		}
		
		@Override
		public boolean matches(String value) {
			if (s_colors.containsKey(value.toLowerCase())) return true;
			
			int parts = value.split(",").length;
			return ((value.startsWith("rgb(") && parts == 3 || 
						value.startsWith("rgba(") && parts == 4) && value.endsWith(")") ||
							value.startsWith("#") && (value.length() == 7 || value.length() == 8));
		}

		@Override
		public Object process(String value, CSSValueEvaluator eval) {
			if (s_colors.containsKey(value.toLowerCase())) return new Color(s_colors.get(value));
			
			String[] parts = value.split(",");
			if (value.startsWith("rgb(") && value.endsWith(")")) {
				float r = s_parseIntRange(parts[0]);
				float g = s_parseIntRange(parts[1]);
				float b = s_parseFloatRange(parts[2]);
				return new Color(r, g, b);
			} else if (value.startsWith("rgba(") && value.endsWith(")")) {
				float r = s_parseIntRange(parts[0]);
				float g = s_parseIntRange(parts[1]);
				float b = s_parseIntRange(parts[2]);				
				float a = s_parseFloatRange(parts[3]);
				return new Color(r, g, b);
			} else if (value.startsWith("#")) {
				value = value.substring(1, value.length());
				if (!(value.length() == 6 || value.length() == 8))
					throw new RuntimeException("Invalid length of chars: " + value);
				if (value.length() == 6) {
					float r = Integer.parseInt(value.substring(0, 2), 16) / 255f;
					float g = Integer.parseInt(value.substring(2, 4), 16) / 255f;
					float b = Integer.parseInt(value.substring(4, 6), 16) / 255f;
					return new Color(r, g, b);
				} else if (value.length() == 8) {
					float r = Integer.parseInt(value.substring(0, 2), 16) / 255f;
					float g = Integer.parseInt(value.substring(2, 4), 16) / 255f;
					float b = Integer.parseInt(value.substring(4, 6), 16) / 255f;
					float a = Integer.parseInt(value.substring(6, 8), 16) / 255f;
					return new Color(r, g, b, a);
				}
			}
			throw new RuntimeException("Unable to parse:" + value);
		}
		//Will parse an int or percent
		public float s_parseIntRange(String value) {
			if (value.endsWith("%")) {
				return Integer.parseInt(value.substring(0, value.length() - 1)) / 100f;
			} else return Integer.parseInt(value) / 255f;
		}
		public float s_parseFloatRange(String value) {
			if (value.endsWith("%")) {
				return Float.parseFloat(value.substring(0, value.length() - 1)) / 100f;
			} else return Float.parseFloat(value);
		}
		
	}
	
	
	//A simple operation on a string that returns an int
	//Perhaps rename operation?
	public static class CSSIntegerOperation implements CSSOperation {
		@Override
		public boolean matches(String value) {
			try {
				Integer.parseInt(value);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		@Override
		public Object process(String value, CSSValueEvaluator eval) {
			if (!matches(value)) throw new RuntimeException(value + " is not an int!");
			else return Integer.parseInt(value);
		}
	}
	//A simple operation on a string that returns an float
	//Perhaps rename operation?
	public static class CSSFloatOperation implements CSSOperation {
		@Override
		public boolean matches(String value) {
			try {
				Float.parseFloat(value);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		@Override
		public Object process(String value, CSSValueEvaluator eval) {
			if (!matches(value)) throw new RuntimeException(value + " is not an float!");
			else return Float.parseFloat(value);
		}
	}
	//A simple operation on a string that turns it into a boolean
	//Perhaps rename to something else?
	public static class CSSBooleanOperation implements CSSOperation {
		@Override
		public boolean matches(String value) {
			String lc = value.toLowerCase();
			return (lc.equals("true") || lc.equals("false"));
		}

		@Override
		public Object process(String value, CSSValueEvaluator eval) {
			String lc = value.toLowerCase();
			if (lc.equals("true")) return true;
			else if (lc.equals("false")) return false;
			else throw new RuntimeException("Unknown boolean: " + value);
		}
	}
	//A simple operation on a literal string and removes the quotes
	//Perhaps rename to something else?
	public static class CSSStringLiteralOperation implements CSSOperation {
		@Override
		public boolean matches(String value) {
			int quoteCount = value.length() - value.replaceAll("\"", "").length();
			return value.startsWith("\"") && quoteCount <= 2;
		}

		@Override
		public Object process(String value, CSSValueEvaluator eval) {
			return value.substring(1, value.length() - 1);
		}
	}
}
