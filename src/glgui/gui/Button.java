package glgui.gui;

import glcommon.Color;
import glcommon.font.Font;
import glcommon.font.JavaFontConverter;
import glgui.painter.Painter;

public class Button extends Node {
	private static final Font s_defaultFont =
		JavaFontConverter.s_convert(new java.awt.Font("Serif", java.awt.Font.PLAIN, 100));
	
	private String m_text;
	private Color m_textColor = Color.BLACK;
	private Font m_font;
	
	public Button(String text) {
		m_text = text;
		m_font = s_defaultFont;
	}
	public Button(String text, Font font) {
		m_text = text;
		m_font = font;
	}
	
	public void setTextColor(Color color) {
		m_textColor = color;
	}
	
	public void setText(String text) {
		m_text = text;
	}
	
	@Override
	public void paintNode(Painter p) {
		p.setFont(m_font);
		p.setColor(Color.WHITE);
		p.drawString(m_text, 0, -10, 1);
		p.setFont(null);
	}
}
