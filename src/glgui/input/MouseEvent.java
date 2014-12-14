package glgui.input;

public abstract class MouseEvent implements Event {
	private int m_x = 0, m_y = 0;
	
	public MouseEvent(int x, int y) {
		m_x = x;
		m_y = y;
	}
	
	public int getX() { return m_x; }
	public int getY() { return m_y; }
	
	public abstract MouseEvent createNew(int x, int y);
}
