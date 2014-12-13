package glgui.input;

import gltools.input.Mouse.MouseButton;

public class MouseEvent implements Event {
	private int m_type;
	
	public MouseEvent(int type, int x, int y, MouseButton button) {
		m_type = type;
	}
	
	public int getType() { return m_type; }
}
