package glgui.render.pipeline;

public interface PDisplay {
	public Pipeline getPipeline();
	
	public int getWidth();
	public int getHeight();
	
	public void dispose();
}
