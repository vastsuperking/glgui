package glgui.render.pipeline;

import glgui.gui.window.ResizeListener;

public interface PDisplay {
	public Pipeline getPipeline();
	
	public int getWidth();
	public int getHeight();
	
	public void addResizedListener(ResizeListener l);
	
	public void dispose();
}
