package glgui.painter;

import glcommon.Color;
import glgui.render.pipeline.Pipeline;

public interface Painter {
	//Will return NULL if no pipeline(eg. drawing to an image)
	public Pipeline getPipeline();

	public void setColor(Color color);
	public Color getColor();
	
	public void drawLine(float x1, float y1, float x2, float y2);
	public void drawPolygon(float x[], float y[]);
	public void drawRect(float x, float y, float width, float height);
	
	public void fillRect(float x, float y, float width, float height);
	public void fillPolygon(float x[], float y[]);

	public void drawTexture(Texture t, float x, float y, float width, float height);
	public void drawTexture(Texture t, float x, float y, float width, float height,
									   float rx, float ry);
	
	public void dispose();
}
