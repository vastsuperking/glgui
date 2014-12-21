package glgui.input;

import glcommon.input.Key;

public class KeyPressedEvent extends KeyEvent {
	public KeyPressedEvent(Key k) {
		super(k);
	}
	@Override
	public String toString() {
		return "Key pressed: " + getKey();
	}
}
