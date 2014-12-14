package glgui.input;

public class MouseWheelEvent extends MouseEvent {
	private float m_dx;
	private float m_dy;
	
	public MouseWheelEvent(int x, int y, float dx, float dy) {
		super(x, y);
		m_dx = dx;
		m_dy = dy;
	}
	public float getDX() { return m_dx; }
	public float getDY() { return m_dy; }
	
	@Override
	public String toString() {
		return "mouse wheel " + m_dx + " " + m_dy;
	}
	@Override
	public MouseEvent createNew(int x, int y) {
		return new MouseWheelEvent(x, y, m_dx, m_dy);
	}
}
