package glgui.gui;

import glgui.painter.Painter;

public class Pane extends Parent {
	@Override
	public void paintNode(Painter p) {
		for (Node w : m_children) {
			w.paint(p);
		}
	}
}
