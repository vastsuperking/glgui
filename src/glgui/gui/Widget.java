package glgui.gui;

import glgui.gui.StylingManager.Styler;
import glgui.input.Event;
import glgui.painter.Paint;
import glgui.painter.Painter;

public abstract class Widget extends Node {
	private Paint m_backgroundPaint = null;
	private float m_backgroundRadius = 0f;
	
	public Widget() {
		getStyler().addStyler("background", new Styler<Paint>(Paint.class) {
			@Override
			public void setStyle(String style, Paint value) {
				setBackgroundPaint(value);
			}

			@Override
			public void resetStyles() {
				setBackgroundPaint(null);
			}
		});
		getStyler().addStyler("background-radius", new Styler<Float>(Float.class) {
			@Override
			public void setStyle(String style, Float value) {
				setBackgroundRadius(value);
			}

			@Override
			public void resetStyles() {
				setBackgroundRadius(0f);
			}
		});
	}
	
	public Paint getBackgroundPaint() { return m_backgroundPaint; }
	public void setBackgroundPaint(Paint background) { m_backgroundPaint = background; }
	
	public void setBackgroundRadius(float radius) { m_backgroundRadius = radius; }
	public float getBackgroundRadius() { return m_backgroundRadius; }
		
	
	public void paintNode(Painter p) {
		if (m_backgroundPaint != null) {
			p.setPaint(m_backgroundPaint);
			if (m_backgroundRadius == 0) p.fillRect(0, 0, getWidth(), getHeight());
			else p.fillRoundedRect(0, 0, getWidth(), getHeight(), getBackgroundRadius());
			p.setPaint(null);
		}
		paintWidget(p);
	}
	
	public abstract void paintWidget(Painter p);
}
