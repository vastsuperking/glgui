package glgui.input;


public class MouseEnterEvent extends MouseEvent {
	public MouseEnterEvent(int x, int y) {
		super(x, y);
	}
	
	@Override
	public String toString() {
		return "mouse entered " + getX() + " " + getY();
	}

	@Override
	public MouseEnterEvent createNew(int x, int y) {
		return new MouseEnterEvent(x, y);
	}
}
