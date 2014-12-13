package glgui.painter;

import glcommon.image.Image2D;

public class ImagePaint extends Paint {
	private Image2D m_image;
	
	public ImagePaint(Image2D image) { m_image = image; }
	
	public Image2D getImage() { return m_image; }
}
