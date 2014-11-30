package glgui.painter;

import glcommon.Color;
import glcommon.vector.Matrix3f;
import glgui.render.pipeline.Pipeline;

public interface Painter {
	//Will return NULL if no pipeline(eg. drawing to an image)
	public Pipeline getPipeline();

	public void setColor(Color color);
	public Color getColor();

	public Matrix3f getTransform();
	public void setTransform(Matrix3f mat);

	public void pushTransform();
	public void popTransform();
	
	public void translate(float x, float y);
	//Will rotate in radians
	public void rotate(float theta);
	public void scale(float x, float y);
	
	
	public void drawLine(float x1, float y1, float x2, float y2);
	public void drawRect(float x, float y, float width, float height);
	public void drawPolygon(float x[], float y[]);
	
	public void fillRect(float x, float y, float width, float height);
	public void fillPolygon(float x[], float y[]);

	public void drawTexture(Texture t, float x, float y, float width, float height);
	public void drawTexture(Texture t, float x, float y, float width, float height,
									   float rx, float ry);
	
	public void dispose();
}
