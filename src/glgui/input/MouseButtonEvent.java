package glgui.input;

import glcommon.input.Mouse.MouseButton;

public class MouseButtonEvent extends MouseEvent {
	private MouseButton m_button;
	private boolean m_pressed;
	
	public MouseButtonEvent(int x, int y, MouseButton mb, boolean pressed) {
		super(x, y);
		m_button = mb;
		m_pressed = pressed;
	}
	public MouseButton getButton() { return m_button; }
	public boolean isPressed() { return m_pressed; }
	
	@Override
	public String toString() {
		return m_button.getName() + " was " + (m_pressed ? "pressed" : "released") 
					+ " at " + getX() + " " + getY(); 
	}
	
	@Override
	public MouseButtonEvent createNew(int x, int y) {
		return new MouseButtonEvent(x, y, m_button, m_pressed);
	}
}
