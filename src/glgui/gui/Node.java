package glgui.gui;

import glgui.painter.Painter;

public abstract class Node {
	private int m_x = 0;
	private int m_y = 0;
	
	private int m_width = 0;
	private int m_height = 0;
	
	private Dimension m_preferredSize = new Dimension(0, 0);
	private Dimension m_minimumSize = new Dimension(0, 0);
	private Dimension m_maximumSize = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	
	public Node() {}
	
	public int getX() { return m_x; }
	public int getY() { return m_y; }
	public int getWidth() { return m_width; }
	public int getHeight() { return m_height; }
	
	public Dimension getPreferredSize() { return m_preferredSize; }
	public Dimension getMinimumSize() { return m_minimumSize; }
	public Dimension getMaximumSize() { return m_maximumSize; }
	
	public void setBounds(int x, int y, int width, int height) {
		m_x = x;
		m_y = y;
		m_width = width;
		m_height = height;
	}
	
	/**
	 * Will transform and paint the widget
	 * by transforming and then calling
	 * <code>
	 * paintWidget(p);
	 * </code>
	 */
	public void paint(Painter p) {
		p.pushTransform();
		
		p.translate(m_x, m_y);
		
		paintNode(p);
		
		p.popTransform();
	}
	
	/**
	 * Will lay out all a node's (possible) components
	 * non-parent nodes will usually not do anything
	 */
	public void validate() {}
	
	public void revalidate() {
		validate();
	}
	
	public abstract void paintNode(Painter p);
}
