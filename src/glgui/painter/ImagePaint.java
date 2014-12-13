package glgui.painter;

import glcommon.image.Image2D;

public class ImagePaint extends Paint {
	private Image2D m_image;
	private float m_rx = 1f;
	private float m_ry = 1f;
	
	public ImagePaint(Image2D image) { m_image = image; }
	
	public Image2D getImage() { return m_image; }
	
	public float getRepeatX() { return m_rx; }
	public float getRepeatY() { return m_ry; }
}
