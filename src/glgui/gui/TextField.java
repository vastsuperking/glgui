package glgui.gui;

import glcommon.Color;
import glcommon.font.Font;
import glcommon.font.JavaFontConverter;
import glcommon.input.Key;
import glcommon.input.TypingModel;
import glcommon.input.TypingModel.TypingListener;
import glgui.gui.StylingManager.Styler;
import glgui.input.Event;
import glgui.input.KeyPressedEvent;
import glgui.input.KeyReleasedEvent;
import glgui.painter.Painter;
import glgui.painter.SolidPaint;

import java.awt.Rectangle;
import java.util.List;

public class TextField extends Node implements TypingListener {
	public static final Font s_defaultFont =
		JavaFontConverter.s_convert(new java.awt.Font("Serif", java.awt.Font.PLAIN, 100));
	
	private StringBuilder m_text = new StringBuilder();
	private Color m_textColor = Color.BLACK;
	private Font m_font;
	
	private TypingModel m_typer = new TypingModel();
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
		
		m_typer.addListener(this);
	}
	
	public TextField(int width) {
		this(width, s_defaultFont);
	}
	public TextField(int width, Font font) {
		m_font = font;
		setPreferrredSize(new Dimension(width, m_font.getAscent() + m_font.getDescent()));
	}

	public void setTextColor(Color color) {
		m_textColor = color;
	}

	@Override
	public void paintNode(Painter p) {
		m_widgetStyler.paint(p);
		p.setPaint(new SolidPaint(Color.WHITE));
		p.setClip(0, 0, getWidth(), getHeight());
		
		Rectangle rect = m_font.getBounds(m_text.toString());
		p.setPaint(new SolidPaint(m_textColor));
		p.drawString(m_text.toString(), m_font, 0, 
				             getHeight() / 2f - (float) rect.getHeight() / 2, 1);
		p.clearClip();
	}
		
	@Override
	public void onEvent(Event e) {
		super.onEvent(e);
		
		if (e instanceof KeyPressedEvent) {
			m_typer.press(((KeyPressedEvent) e).getKey());
			
			//m_text.append(((KeyPressedEvent) e).getKey().getChar());
		} else if (e instanceof KeyReleasedEvent) {
			m_typer.release(((KeyReleasedEvent) e).getKey());
		}
		//System.out.println("Textfield received: " + e);
	}
	
	//Typing listener functions
	
	@Override
	public void charTyped(char c) {
		m_text.append(c);
	}
	@Override
	public void comboTyped(List<Key> keys) {}
	
	@Override
	public void specialKeyTyped(Key k) {}
	@Override
	public void backspace() {
		if (m_text.length() > 0) m_text.deleteCharAt(m_text.length() - 1);
	}
	@Override
	public void newline() {}
}
