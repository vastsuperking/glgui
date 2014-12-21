package glgui.input;

import glcommon.input.Key;

public class KeyReleasedEvent extends KeyEvent {
	public KeyReleasedEvent(Key k) {
		super(k);
	}
	
	@Override
	public String toString() {
		return "Key released: " + getKey();
	}
}
