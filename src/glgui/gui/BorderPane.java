package glgui.gui;

public class BorderPane extends Pane {
	private Node m_top = null;
	private Node m_bottom = null;

	private Node m_left = null;
	private Node m_right = null;
	
	private Node m_center = null;
	
	public void setTop(Node top) {
		if (m_top != null) removeChild(m_top);
		m_top = top;
		addChild(top);
	}
	public void setBottom(Node bottom) {
		if (m_bottom != null) removeChild(m_bottom);
		m_bottom = bottom;
		addChild(bottom);
	}
	public void setCenter(Node center) {
		if (m_center != null) removeChild(m_center);
		m_center = center;
		addChild(m_center);
	}
	public void setLeft(Node left) {
		if (m_left != null) removeChild(m_left);
		m_left = left;
		addChild(m_left);
	}
	public void setRight(Node right) {
		if (m_right != null) removeChild(m_right);
		m_right = right;	
		addChild(m_right);
	}
	
	public void validate() {
		//Vars for the size of the center
		//node
		int x = 0;
		int y = 0;
		int width = getWidth();
		int height = getHeight();
		
		Dimension d = null;

		if (m_top != null) {
			d = m_top.getPreferredSize();
			int h = d.getHeight();
			m_top.setBounds(x, height - h, width, h);
			height -= h;
		}
		if (m_bottom != null) {
			d = m_bottom.getPreferredSize();
			//Height here b/c we halve already subtracted top
			//from height
			int h = d.getHeight();
			m_bottom.setBounds(x, y, width, h);
			y += h;
		}
		if (m_left != null) {
			d = m_left.getPreferredSize();
			int w = d.getWidth();
			m_left.setBounds(0, y, w, height);
			x += w;
			width -= w;
		}
		if (m_right != null) {
			d = m_right.getPreferredSize();
			int w = d.getWidth();
			m_right.setBounds(getWidth() - w, y, w, height);
			width -= w;
		}
		if (m_center != null) {
			m_center.setBounds(x, y, width, height);
		}
		//Now validate each of the children
		super.validate();
	}
}
