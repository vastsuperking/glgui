package glgui.gui;

import java.util.ArrayList;
import java.util.List;

public abstract class Parent extends Node {
	protected List<Node> m_children = new ArrayList<Node>();

	public void addChild(Node child) {
		m_children.add(child);
	}
}
