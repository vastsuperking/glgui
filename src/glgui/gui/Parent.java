package glgui.gui;

import glgui.input.Event;
import glgui.input.MouseButtonEvent;
import glgui.input.MouseEnterEvent;
import glgui.input.MouseEvent;
import glgui.input.MouseExitEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class Parent extends Node {
	protected List<Node> m_children = new ArrayList<Node>();

	@Override
	public void style() {
		super.style();
		for (Node n : m_children) n.style();
	}
	
	@Override
	public void validate() {
		super.validate();
		for (Node n : m_children) n.validate();
	}
	@Override
	public void onEvent(Event e) {
		super.onEvent(e);
		for (Node n : m_children) {
			if (e instanceof MouseEvent) {
				MouseEvent me = (MouseEvent) e;
				int nx = me.getX() - n.getX();
				int ny = me.getY() - n.getY();
				if (me instanceof MouseEnterEvent) {
				} else if (me instanceof MouseExitEvent) {
					if (n.isHovered()) {
						n.onEvent(me.createNew(nx, ny));
					}
				} else if (nx >= 0 && ny >= 0 && 
						   nx <= n.getWidth() && ny <= n.getHeight()) {
					n.onEvent(me);
					if (!n.isHovered()) {
						n.onEvent(new MouseEnterEvent(nx, ny));
					}
				} else if (e instanceof MouseButtonEvent && n.m_waitingMouseRelease) {
					n.onEvent(me.createNew(nx, ny));
				} else if (n.isHovered()) {
					n.onEvent(new MouseExitEvent(nx, ny));
				}
			} else n.onEvent(e);
		}
	}
	
	public void removeChild(Node child) {
		child.setParent(null);
		m_children.remove(child);
		child.style();
	}
	
	public void addChild(Node child) {
		if (child.getParent() != null)
			throw new RuntimeException("Node is already in scene tree");
		child.setParent(this);
		m_children.add(child);
		//Restyle the child
		child.style();
	}
}
