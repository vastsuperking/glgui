package glgui.render.pipeline;

import glgui.painter.Painter;

public interface Pipeline {
	public PDisplay getDisplay();

	public Painter getPainter();

	public void init();	
	public void dispose();

	public void startRendering();
	public void stopRendering();	
}
