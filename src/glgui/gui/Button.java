package glgui.gui;

import glcommon.Color;
import glcommon.font.Font;
import glcommon.font.JavaFontConverter;
import glgui.gui.StylingManager.Styler;
import glgui.input.Event;
import glgui.input.MouseButtonEvent;
import glgui.painter.Painter;
import glgui.painter.SolidPaint;

import java.awt.Rectangle;

public class Button extends Node {
	public static final Font s_defaultFont =
		JavaFontConverter.s_convert(new java.awt.Font("Serif", java.awt.Font.PLAIN, 100));
	
	private String m_text;
	private Color m_textColor = Color.BLACK;
	private Font m_font;
	
	private boolean m_pressed = false;;
	
	private WidgetStyler m_widgetStyler = new WidgetStyler(this);
	
	{
		//Add a textColor styler
		m_styler.addStyler("text-color", new Styler<Color>(Color.class) {
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
	
	public boolean isPressed() { return m_pressed; }
	
	public void setPressed(boolean pressed) {
		if (m_pressed != pressed) {
			m_pressed = pressed;
			//Restyle this widget
			style();
		}
	}
	
	public void setTextColor(Color color) {
		m_textColor = color;
	}
	
	public void setText(String text) {
		m_text = text;
	}
	
	@Override
	public Dimension getPreferredSize() {
		if (m_preferredSize.getWidth() != 0 && m_preferredSize.getHeight() != 0) return m_preferredSize;
		Rectangle rect = m_font.getBounds(m_text);
		return new Dimension((int) (rect.getX() + rect.getWidth()), (int) (rect.getY() + rect.getHeight()));
	}
	
	@Override
	public boolean inState(String state) {
		switch(state) {
		case "pressed": return isPressed();
		default: return super.inState(state);
		}
	}
	
	@Override
	public void paintNode(Painter p) {
		m_widgetStyler.paint(p);
		
		Rectangle rect = m_font.getBounds(m_text);
		p.setPaint(new SolidPaint(Color.WHITE));
		p.setClip(0, 0, getWidth(), getHeight());
		p.setPaint(new SolidPaint(m_textColor));
		p.drawString(m_text, m_font, getWidth() / 2f - (float) rect.getWidth() / 2, 
				             getHeight() / 2f - (float) rect.getHeight() / 2, 1);
		p.clearClip();
	}
	
	@Override
	public void onEvent(Event e) {
		super.onEvent(e);
		if (e instanceof MouseButtonEvent) {
			MouseButtonEvent be = (MouseButtonEvent) e;
			setPressed(be.isPressed());
		}
	}
}
