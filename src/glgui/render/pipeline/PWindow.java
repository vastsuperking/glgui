package glgui.render.pipeline;

import glcommon.input.Keyboard;
import glcommon.input.Mouse;
import glgui.gui.window.FileDropListener;
import glgui.gui.window.MoveListener;
import glgui.gui.window.WindowStateListener;

public interface PWindow extends PDisplay {
	public void setName(String name);
	public String getName();
	public boolean isVisible();
	public boolean isInitialized();
	public boolean closeRequested();
	
	public Mouse getMouse();
	public Keyboard getKeyboard();
	
	public void setVisible(boolean visible);
	public void setSize(int width, int height);	
	
	public void addStateListener(WindowStateListener l);
	public void addMoveListener(MoveListener l);
	public void addFileDropListener(FileDropListener l);
}
