package glgui.painter.graphic;

import glcommon.image.Image2D;
import glgui.painter.Painter;

public class ImageGraphic implements Graphic {
	private Image2D m_image;
	
	public ImageGraphic(Image2D image) { m_image = image; }
	
	public void setImage(Image2D image) { m_image = image; }
	public Image2D getImage() { return m_image; }

	@Override
	public void paint(Painter p, float x, float y, float width, float height) {
		p.drawImage(m_image, x, y, width, height);
	}
}
