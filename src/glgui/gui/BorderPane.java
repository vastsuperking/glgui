package glgui.gui;

public class BorderPane extends Pane {
	private Node m_top = null;
	private Node m_bottom = null;

	private Node m_left = null;
	private Node m_right = null;
	
	private Node m_center = null;
	
	public void setTop(Node top) { m_top = top; }
	public void setBottom(Node bottom) { m_top = bottom; }
	public void setCenter(Node center) { m_top = center; }
	public void setLeft(Node left) { m_top = left; }
	public void setRight(Node right) { m_top = right; }
	
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
			int h = Math.min(d.getHeight(), height >> 1);
			m_top.setBounds(x, height - h, width, h);
			height -= h;
		}
		if (m_bottom != null) {
			d = m_bottom.getPreferredSize();
			//Height here b/c we halve already subtracted top
			//from height
			int h = Math.min(d.getHeight(), height);
			m_bottom.setBounds(x, y, width, h);
			y += h;
		}
		if (m_left != null) {
			d = m_left.getPreferredSize();
			int w = Math.min(d.getWidth(), width >> 1);
			m_left.setBounds(0, y, w, height);
			x += w;
			width -= w;
		}
		if (m_right != null) {
			d = m_right.getPreferredSize();
			int w = Math.min(d.getWidth(), width >> 1);
			m_left.setBounds(getWidth() - w, y, w, height);
		}
		if (m_center != null) {
			m_center.setBounds(x, y, width, height);
		}
	}
}
