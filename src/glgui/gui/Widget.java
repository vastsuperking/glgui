package glgui.gui;

import glgui.gui.StylingManager.Styler;
import glgui.painter.Painter;
import glgui.painter.graphic.Graphic;

public abstract class Widget extends Node {
	private Graphic m_background = null;
	
	public Widget() {
		getStyler().addStyler("background", new Styler<Graphic>() {
			@Override
			public void setStyle(String style, Graphic value) {
				setBackground(value);
			}

			@Override
			public void resetStyles() {
				setBackground(null);
			}
		});
	}
	
	public Graphic getBackground() { return m_background; }
	public void setBackground(Graphic background) { m_background = background; }
	
	public void paintNode(Painter p) {
		if (m_background != null) {
			m_background.paint(p, 0, 0, getWidth(), getHeight());
		}
		paintWidget(p);
	}
	
	public abstract void paintWidget(Painter p);
}
