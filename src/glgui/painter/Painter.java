package glgui.painter;

import glcommon.font.Font;
import glcommon.vector.Matrix3f;

public interface Painter {
	public Matrix3f getTransform();
	public void setTransform(Matrix3f mat);

	public void setPaint(Paint p);
	public Paint getPaint();
	
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
	public void fillRect(float x, float y, float width, float height,
						 float tx, float ty, float tw, float th);
	
	public void fillPolygon(float x[], float y[]);
	public void fillRoundedRect(float x, float y, float width, float height, 
			float radius);
	public void fillRoundedRect(float x, float y, float width, float height, 
			float radius, int resolution);
	public void fillRoundedRect(float x, float y, float width, float height, 
			float radius, int resolution,
			float tx, float ty, float tw, float th);
	
	public void fillArc(float x, float y, float radius, float smRadius, 
						float startRads, float rads);
	
	public void fillArc(float x, float y, float radius, float smRadius, 
						float startRads, float rads, int resolution);
	
	public void fillArc(float x, float y, float radius, float smRadius, 
						float startRads, float rads, int resolution,
						float tx, float ty, float tw, float th);

	public void fillElipse(float x, float y, float radius, float smRadius);
	public void fillElipse(float x, float y, float radius, float smRadius, int resolution);
	public void fillElipse(float x, float y, float radius, float smRadius, int resolution,
						   float tx, float ty, float tw, float th);
	//Will draw a gradient
	/*public void fillGradient(Gradient g, float x, float y, float width, float height);

	public void drawImage(Image2D t, float x, float y, float width, float height);
	public void drawImage(Image2D t, float x, float y, float width, float height,
									   float rx, float ry);*/
	
	
	public void drawString(String string, Font f, float x, float y, float scale);
	
	public void dispose();
}
