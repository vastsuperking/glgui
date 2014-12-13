package glgui.painter;

import glcommon.BufferUtils;
import glcommon.Color;
import glcommon.image.Image2D;
import glcommon.image.ImageFilterMode;
import glcommon.image.ImageFormat;
import glcommon.image.ImageWrapMode;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LinearGradientPaint extends Paint {
	public enum Direction {
		HORIZONTAL, VERTICAL
	}
	
	private Direction m_direction;
	
	private List<ColorStop> m_colors = new ArrayList<ColorStop>();
	
	//An image representing the colors
	private Image2D m_image;
	
	public LinearGradientPaint(Direction dir, ColorStop... colors) {
		m_direction = dir;
		m_colors.addAll(Arrays.asList(colors));
		generateImage();
	}
	public LinearGradientPaint(Direction dir, Color... colors) {
		m_direction = dir;
		float percent = 1f / colors.length;
		for (Color c : colors) {
			m_colors.add(new ColorStop(c, percent));
		}
		generateImage();
	}
	public List<ColorStop> getColors() { return m_colors; }
	public Direction getDirection() { return m_direction; }
	
	public Image2D getImage() { return m_image; }
	
	private void generateImage() {
		int resolution = m_colors.size();
		//Calculate the resolution needed
		float percentSum = 0;
		for (ColorStop s : m_colors) {
			percentSum += s.getPercent();
			int res = (int) (1 / s.getPercent());
			if (res > resolution) resolution = res;
		}
		if (Math.abs(percentSum - 1) > 0.01f) throw new RuntimeException("Color stops do not add up to 100%");
		
		//Create bytebuffer with two pixels(4 bytes per pixel)
		//The byte buffer is ordered row by row(bottom up)
		ByteBuffer buffer = BufferUtils.createByteBuffer(resolution * 4);
		
		if (m_direction == Direction.VERTICAL) {
			for (int i = m_colors.size() - 1; i >= 0; i--) {
				//Num of pixels this color should get
				ColorStop c = m_colors.get(i);
				int pixels = (int) (c.getPercent() * resolution);
				for (int p = 0; p < pixels; p++) {
					s_putColor(buffer, c.getColor());
				}
			}
		} else {
			for (int i = 0;i < m_colors.size(); i++) {
				//Num of pixels this color should get
				ColorStop c = m_colors.get(i);
				int pixels = (int) (c.getPercent() * resolution);
				for (int p = 0; p < pixels; p++) {
					s_putColor(buffer, c.getColor());
				}
			}
		}

		buffer.flip();

		if (m_direction == Direction.HORIZONTAL) {
			m_image = new Image2D(resolution, 1, ImageFormat.RGBA, buffer);
		} else {
			m_image = new Image2D(1, resolution, ImageFormat.RGBA, buffer);
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
	
	public static class ColorStop {
		private Color m_color;
		private float m_percent;
		
		public ColorStop(Color c, float percent) { m_color = c; m_percent = percent; }
		
		public Color getColor() { return m_color; }
		public float getPercent() { return m_percent; }
	}
}
