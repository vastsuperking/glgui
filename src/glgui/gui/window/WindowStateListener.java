package glgui.gui.window;

public interface WindowStateListener {
	public void windowClosed();
	public void windowRefresh();
	public void windowFocused();
	public void windowUnfocused();
	public void windowMinimized();
	public void windowMaximized();
}
