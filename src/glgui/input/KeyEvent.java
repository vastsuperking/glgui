package glgui.input;

import glcommon.input.Key;

public class KeyEvent implements Event {
	private Key m_key;
	
	public KeyEvent(Key k) {
		m_key = k;
	}
	public Key getKey() { return m_key; }
}
