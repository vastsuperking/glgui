package glgui.input;

import glcommon.vector.Vector2f;

public class MouseMoveEvent extends MouseEvent {
	private Vector2f m_delta;
	
	public MouseMoveEvent(int x, int y, Vector2f delta) {
		super(x, y);
		m_delta = delta;
	}
	
	public Vector2f getDelta() { return m_delta; }
	
	@Override
	public String toString() {
		return "moved to: " + getX() + " " + getY() + " delta: " + m_delta;
	}

	@Override
	public MouseMoveEvent createNew(int x, int y) {
		return new MouseMoveEvent(x, y, m_delta);
	}
}
