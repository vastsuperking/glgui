package glgui.painter.graphic;

import glcommon.Color;
import glgui.painter.Painter;

public class Gradient implements Graphic {
	public enum GradientDirection { HORIZONTAL, VERTICAL };
	
	private GradientDirection m_direction;
	
	private Color m_colorA;
	private Color m_colorB;
	
	//An image representing the colors
	//private Image2D m_image;
	
	public Gradient(GradientDirection dir, Color a, Color b) {
		m_direction = dir;
		m_colorA = a;
		m_colorB = b;
		//generateImage();
	}
	
	public Color getColorA() { return m_colorA; }
	public Color getColorB() { return m_colorB; }
	public GradientDirection getDirection() { return m_direction; }

	@Override
	public void paint(Painter p, float x, float y, float width, float height) {
		//System.out.println("Painting " + m_image);
		//p.drawImage(m_image, x, y, width, height);
		
		p.fillGradient(this, x, y, width, height);
	}
	
	/*private void generateImage() {
		int pixelsPerColor = 1;
		//Create bytebuffer with two pixels(4 bytes per pixel)
		//The byte buffer is ordered row by row(bottom up)
		ByteBuffer buffer = BufferUtils.createByteBuffer(4 * pixelsPerColor * 2);
		for (int i = 0; i < pixelsPerColor; i++) {
			s_putColor(buffer, m_colorA);
		}
		for (int i = 0; i < pixelsPerColor; i++) {
			s_putColor(buffer, m_colorB);
		}
		buffer.flip();

		if (m_direction == GradientDirection.HORIZONTAL) {
			m_image = new Image2D(pixelsPerColor * 2, 1, ImageFormat.RGBA, buffer);
		} else {
			m_image = new Image2D(1, pixelsPerColor * 2, ImageFormat.RGBA, buffer);
		}
		//Interpolate
		m_image.setMaxFilterMode(ImageFilterMode.LINEAR);
		//Clamp to edge, not border
		m_image.setSWrapMode(ImageWrapMode.CLAMP_TO_EDGE);
		m_image.setTWrapMode(ImageWrapMode.CLAMP_TO_BORDER);
	}
	private static void s_putColor(ByteBuffer buffer, Color color ) {
		byte red = ((byte) (color.getRed() * 255));
		byte green = ((byte) (color.getGreen() * 255));
		byte blue = ((byte)  (color.getBlue() * 255));
		byte alpha = ((byte) (color.getAlpha() * 255));
		buffer.put(red);     // Red component
		buffer.put(green);   // Green component
		buffer.put(blue);    // Blue component
		buffer.put(alpha);   // Alpha component. Only for RGBA
	}*/
}
