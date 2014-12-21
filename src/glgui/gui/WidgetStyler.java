package glgui.gui;

import glcommon.Color;
import glgui.gui.StylingManager.Styler;
import glgui.painter.Paint;
import glgui.painter.Painter;
import glgui.painter.SolidPaint;

public class WidgetStyler {
	private Paint m_backgroundPaint = null;
	private float m_backgroundRadius = 0f;
	private Node m_node;
	public WidgetStyler(Node n) {
		m_node = n;
		n.getStyler().addStyler("background", new Styler<Paint>(Paint.class) {
			@Override
			public void setStyle(String style, Paint value) {
				setBackgroundPaint(value);
			}

			@Override
			public void resetStyles() {
				setBackgroundPaint(null);
			}
		});
		n.getStyler().addStyler("background", new Styler<Color>(Color.class) {
			@Override
			public void setStyle(String style, Color value) {
				System.out.println("Setting background to: " + value);
				setBackgroundPaint(new SolidPaint(value));
			}

			@Override
			public void resetStyles() {
				setBackgroundPaint(null);
			}
		});
		n.getStyler().addStyler("background-radius", new Styler<Float>(Float.class) {
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
		
	
	public void paint(Painter p) {
		if (m_backgroundPaint != null) {
			//System.out.println("Painting: " + m_backgroundPaint + " " + m_node.getWidth() + " " + m_node.getHeight());
			p.setPaint(m_backgroundPaint);
			if (m_backgroundRadius == 0) p.fillRect(0, 0, m_node.getWidth(), m_node.getHeight());
			else p.fillRoundedRect(0, 0, m_node.getWidth(), m_node.getHeight(), getBackgroundRadius());
			p.setPaint(null);
		}
	}
}
