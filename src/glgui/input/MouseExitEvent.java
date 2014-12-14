package glgui.input;


public class MouseExitEvent extends MouseEvent {
	public MouseExitEvent(int x, int y) {
		super(x, y);
	}
	
	@Override
	public String toString() {
		return "mouse exited " + getX() + " " + getY();
	}

	@Override
	public MouseExitEvent createNew(int x, int y) {
		return new MouseExitEvent(x, y);
	}
}
