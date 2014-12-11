package glgui.gui;

import glcommon.Color;
import glcommon.font.Font;
import glcommon.font.JavaFontConverter;
import glgui.gui.StylingManager.Styler;
import glgui.painter.Painter;

import java.awt.Rectangle;

public class Button extends Widget {
	private static final Font s_defaultFont =
		JavaFontConverter.s_convert(new java.awt.Font("Serif", java.awt.Font.PLAIN, 100));
	
	private String m_text;
	private Color m_textColor = Color.BLACK;
	private Font m_font;
	
	{
		//Add a textColor styler
		m_styler.addStyler("text-color", new Styler<Color>() {
			@Override
			public void setStyle(String style, Color value) {
				setTextColor(value);
			}
			@Override
			public void resetStyles() {
				setTextColor(Color.BLACK);
			}
		});
	}
	
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
	public void paintWidget(Painter p) {
		p.setFont(m_font);
		p.setColor(m_textColor);
		Rectangle rect = m_font.getBounds(m_text);
		p.drawString(m_text, getWidth() / 2f - (float) rect.getWidth() / 2, 
				             getHeight() / 2f - (float) rect.getHeight() / 2, 1);
		p.setFont(null);
	}
}
