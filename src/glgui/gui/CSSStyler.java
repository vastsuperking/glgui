package glgui.gui;

import glcommon.Color;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSSStyler {
	private static final Logger logger = LoggerFactory.getLogger(CSSStyler.class);
	
	private HashMap<String, ColorStyler> m_colorStyles = new HashMap<String, ColorStyler>();
	private HashMap<String, IntStyler> m_intStyles = new HashMap<String, IntStyler>();
	private HashMap<String, FloatStyler> m_floatStyles = new HashMap<String, FloatStyler>();
	private HashMap<String, StringStyler> m_stringStyles = new HashMap<String, StringStyler>();
	
	public void addColorStyler(String style, ColorStyler styler) {
		m_colorStyles.put(style, styler);
	}
	public void addIntStyler(String style, IntStyler styler) {
		m_intStyles.put(style, styler);
	}
	public void addFloatStyler(String style, FloatStyler styler) {
		m_floatStyles.put(style, styler);
	}
	public void addStringStyler(String style, StringStyler styler) {
		m_stringStyles.put(style, styler);
	}
	
	public void setStyle(String style, String value) {
		if (m_colorStyles.containsKey(style)) {
			processColorStyle(style, value);
		} else if (m_intStyles.containsKey(style)) {
			processIntStyle(style, value);
		} else if (m_floatStyles.containsKey(style)) {
			processFloatStyle(style, value);
		} else if (m_stringStyles.containsKey(style)) {
			processStringStyle(style, value);
		} else logger.warn("Could not find styler for: " + style);
	}
	
	private void processColorStyle(String style, String value) {
		value = value.trim();
		
		float r = 0;
		float g = 0;
		float b = 0;
		if (value.startsWith("rgb(")) {
			String commaSeparated = value.substring(4).split(")")[0];
			String[] parts = commaSeparated.split(",");
			if (parts.length != 3) throw new RuntimeException("Not three values!");
			r = Integer.parseInt(parts[0]) / 255.0f;
			g = Integer.parseInt(parts[1]) / 255.0f;
			b = Integer.parseInt(parts[2]) / 255.0f;
		} else if (value.startsWith("#")) {
			String values = value.substring(1);
			r = Integer.parseInt(values.substring(0, 2), 16) / 255f;
			g = Integer.parseInt(values.substring(2, 4), 16) / 255f;
			b = Integer.parseInt(values.substring(4, 6), 16) / 255f;
		}
		m_colorStyles.get(style).setStyle(style, new Color(r, g, b));
	}
	
	public void processIntStyle(String style, String value) {
		m_intStyles.get(style).setStyle(style, Integer.parseInt(value));
	}
	public void processFloatStyle(String style, String value) {
		m_floatStyles.get(style).setStyle(style, Float.parseFloat(value));
	}
	public void processStringStyle(String style, String value) {
		m_stringStyles.get(style).setStyle(style, value);
	}
	
	public interface ColorStyler {
		public void setStyle(String style, Color value);
	}
	public interface IntStyler {
		public void setStyle(String style, int value);
	}
	public interface FloatStyler {
		public void setStyle(String style, float value);
	}
	public interface StringStyler {
		public void setStyle(String style, String value);
	}
}
