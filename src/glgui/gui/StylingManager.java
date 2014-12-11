package glgui.gui;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StylingManager {
	private static final Logger logger = LoggerFactory.getLogger(StylingManager.class);
	
	private HashMap<String, Styler<?>> m_stylers = new HashMap<String, Styler<?>>();
	
	public void addStyler(String style, Styler<?> styler) {
		m_stylers.put(style, styler);
	}
	
	public <T> void setStyle(String style, T value) {
		if (m_stylers.containsKey(style)) {
			@SuppressWarnings("unchecked")
			Styler<T> styler = (Styler<T>) m_stylers.get(style);
			//TODO: Fix canStyle always being true bug(b/c casting generics)
			if (!styler.canStyle(value))
				throw new RuntimeException("Styler for: " + style + " cannot style " + value);
			styler.setStyle(style, value);
		} else logger.warn("Could not find styler for: " + style);
	}
	//Will clear all styling
	public void resetStyles() {
		for (Styler<?> s : m_stylers.values()) {
			s.resetStyles();
		}
	}
	public static abstract class Styler<T> {
		public boolean canStyle(Object o) {
			try {
				@SuppressWarnings({ "unchecked", "unused" })
				T test = (T) o;
				return true;
			} catch (ClassCastException e) { return false; }
		}
		
		public abstract void setStyle(String style, T value);
		public abstract void resetStyles();
	}
}
