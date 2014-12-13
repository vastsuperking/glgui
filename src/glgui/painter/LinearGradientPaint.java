package glgui.painter;

import glcommon.BufferUtils;
import glcommon.Color;
import glcommon.image.Image2D;
import glcommon.image.ImageFilterMode;
import glcommon.image.ImageFormat;
import glcommon.image.ImageWrapMode;
import glgui.painter.graphic.Gradient.GradientDirection;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LinearGradientPaint extends Paint {
	private GradientDirection m_direction;
	
	private List<Color> m_colors = new ArrayList<Color>();
	
	//An image representing the colors
	private Image2D m_image;
	
	public LinearGradientPaint(GradientDirection dir, Color... colors) {
		m_direction = dir;
		m_colors.addAll(Arrays.asList(colors));
		generateImage();
	}
	public List<Color> getColors() { return m_colors; }
	public GradientDirection getDirection() { return m_direction; }
	
	public Image2D getImage() { return m_image; }
	
	private void generateImage() {
		int resolution = m_colors.size();
		//Create bytebuffer with two pixels(4 bytes per pixel)
		//The byte buffer is ordered row by row(bottom up)
		ByteBuffer buffer = BufferUtils.createByteBuffer(m_colors.size() * 4);
		for (Color c : m_colors) {
			s_putColor(buffer, c);
		}

		buffer.flip();

		if (m_direction == GradientDirection.HORIZONTAL) {
			m_image = new Image2D( m_colors.size(), 1, ImageFormat.RGBA, buffer);
		} else {
			m_image = new Image2D(1, m_colors.size(), ImageFormat.RGBA, buffer);
		}
		//Interpolate
		m_image.setMaxFilterMode(ImageFilterMode.LINEAR);
		//Clamp to edge, not border
		m_image.setSWrapMode(ImageWrapMode.CLAMP_TO_EDGE);
		m_image.setTWrapMode(ImageWrapMode.CLAMP_TO_EDGE);
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
	}
}
