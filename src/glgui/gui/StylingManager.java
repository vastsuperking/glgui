package glgui.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StylingManager {
	private static final Logger logger = LoggerFactory.getLogger(StylingManager.class);
	
	private HashMap<String, List<Styler<?>>> m_stylers = new HashMap<String, List<Styler<?>>>();
	
	public void addStyler(String style, Styler<?> styler) {
		if (!m_stylers.containsKey(style))
			m_stylers.put(style, new ArrayList<Styler<?>>());
		m_stylers.get(style).add(styler);
	}
	
	@SuppressWarnings("unchecked")
	public <T> void setStyle(String style, T value) {
		if (m_stylers.containsKey(style)) {
			List<Styler<?>> stylers = m_stylers.get(style);
			for (Styler<?> s : stylers) {
				if (s.canStyle(value)) {
					Styler<T> styler = (Styler<T>) s;
					if (isIntegerType(value.getClass()) && isFloatType(styler.getType())) {
						styler.setStyle(style, (T) castNumeric(value, styler.getType()));
					} else styler.setStyle(style, value);
					return;
				}
			}
			throw new RuntimeException("Could not find styler for: " + style + "=" + value);
		} else logger.warn("Could not find styler for: " + style);
	}
	//Will clear all styling
	public void resetStyles() {
		for (List<Styler<?>> s : m_stylers.values()) {
			//Take the first styler in each list
			s.get(0).resetStyles();
		}
	}
	public static abstract class Styler<T> {
		private Class<? extends T> m_type;
		
		public Styler(Class<? extends T> type) { m_type = type; }
		
		public Class<? extends T> getType() { return m_type; }
		
		public boolean canStyle(Object o) {
			if (m_type.equals(Float.class) && o.getClass().equals(Integer.class) || o.getClass().equals(Float.class)) {
				return true;
			}
			return m_type.isInstance(o);
		}
		
		public abstract void setStyle(String style, T value);
		public abstract void resetStyles();
	}
	
	public static boolean isIntegerType(Class<?> type) {
		return type.equals(Integer.class) || type.equals(Long.class) || type.equals(Short.class);
	}
	public static boolean isFloatType(Class<?> type) {
		return type.equals(Float.class) || type.equals(Double.class);
	}
	public static Object castNumeric(Object a, Class<?> type) {
		if (type.equals(Float.class)) {
			if (a.getClass().equals(Double.class)) return (float) (double) a;
			if (a.getClass().equals(Short.class)) return (float) (short) a;
			if (a.getClass().equals(Integer.class)) return (float) (int) a;
			if (a.getClass().equals(Long.class)) return (float) (long) a;
		} else if (type.equals(Double.class)) {
			if (a.getClass().equals(Float.class)) return (double) (float) a;
			if (a.getClass().equals(Short.class)) return (double) (short) a;
			if (a.getClass().equals(Integer.class)) return (double) (int) a;
			if (a.getClass().equals(Long.class)) return (double) (long) a;
		} else if (type.equals(Long.class)) {
			if (a.getClass().equals(Float.class)) return (long) (float) a;
			if (a.getClass().equals(Double.class)) return (long) (double) a;
			if (a.getClass().equals(Short.class)) return (long) (short) a;
			if (a.getClass().equals(Integer.class)) return (long) (int) a;
		} else if (type.equals(Integer.class)) {
			if (a.getClass().equals(Float.class)) return (int) (float) a;
			if (a.getClass().equals(Double.class)) return (int) (double) a;
			if (a.getClass().equals(Short.class)) return (int) (short) a;
			if (a.getClass().equals(Long.class)) return (int) (long) a;
		} else if (type.equals(Short.class)) {
			if (a.getClass().equals(Float.class)) return (short) (float) a;
			if (a.getClass().equals(Double.class)) return (short) (double) a;
			if (a.getClass().equals(Integer.class)) return (short) (int) a;
			if (a.getClass().equals(Long.class)) return (short) (long) a;
		}
		throw new RuntimeException("Cannot cast " + a + " to " + type);
	}
}
