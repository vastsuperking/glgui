package glgui.css.eval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class CSSFunction {
	private String m_name;
	private List<Class<?>> m_argTypes = new ArrayList<Class<?>>();
	
	public CSSFunction(String name, Class<?>... args) {
		m_name = name;
		m_argTypes.addAll(Arrays.asList(args));
	}
	
	public List<Class<?>> getArgumentType() { return m_argTypes; }
	public String getName() { return m_name; }
	
	public String getSignature() {
		return s_createSignature(m_name, m_argTypes);
	}
	public Object call(List<Object> args) {
		//TODO: Check argument types
		return execute(args.toArray(new Object[0]));
	}
	public Object call(Object... args) {
		//TODO: Check argument types
		return execute(args);
	}
	
	public abstract Object execute(Object... args);
	
	public static String s_createSignature(String name, List<Class<?>> args) {
		StringBuilder s = new StringBuilder();
		s.append(name).append('(');
		for (int i = 0; i < args.size(); i++) {
			Class<?> type = args.get(i);
			s.append(s_getTypeShortName(type));
			if (i != args.size() - 1) {
				s.append(',');
			}
		}
		s.append(')');
		return s.toString();
	}
	private static String s_getTypeShortName(Class<?> type) {
		if (type == Boolean.class) return "boolean";
		else if (type == Short.class) return "short";
		else if (type == Integer.class) return "int";
		else if (type == Long.class) return "long";
		else if (type == Float.class) return "float";
		else if (type == Double.class) return "double";
		else return type.getName();
	}
}
