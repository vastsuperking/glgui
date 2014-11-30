package glgui.gui;

import glgui.painter.Painter;

public abstract class Widget extends Node {
	@Override
	public void paintNode(Painter p) {
		paintWidget(p);
	}
	
	public abstract void paintWidget(Painter p);
}
