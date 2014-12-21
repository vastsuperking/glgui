package glgui.gui;

import glgui.painter.Painter;

public class Pane extends Parent {
	private WidgetStyler m_widgetStyler = new WidgetStyler(this);
	
	@Override
	public void paintNode(Painter p) {
		m_widgetStyler.paint(p);
		synchronized(m_children) {
			for (Node w : m_children) {
				w.paint(p);
			}
		}
	}
}
