package glgui.painter;

import glcommon.Color;
import glcommon.font.Font;
import glcommon.image.Image2D;
import glcommon.vector.Matrix3f;

public interface Painter {
	public void setColor(Color color);
	public Color getColor();
	
	public void setFont(Font font);
	public Font getFont();

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

	public void drawImage(Image2D t, float x, float y, float width, float height);
	public void drawImage(Image2D t, float x, float y, float width, float height,
									   float rx, float ry);
	
	public void drawString(String string, float x, float y, float scale);
	
	public void dispose();
}
