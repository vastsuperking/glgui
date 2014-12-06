package glgui.gui;

import java.util.ArrayList;
import java.util.List;

public abstract class Parent extends Node {
	protected List<Node> m_children = new ArrayList<Node>();

	public void removeChild(Node child) {
		child.setParent(null);
		m_children.remove(child);
	}
	
	public void addChild(Node child) {
		if (child.getParent() != null)
			throw new RuntimeException("Node is already in scene tree");
		child.setParent(this);
		m_children.add(child);
	}
}
