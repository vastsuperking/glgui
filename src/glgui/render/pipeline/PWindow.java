package glgui.render.pipeline;


public interface PWindow extends PDisplay {
	public void setName(String name);
	public String getName();
	public boolean isVisible();
	public boolean isInitialized();
	
	public void setVisible(boolean visible);
	public void setSize(int width, int height);	
}
