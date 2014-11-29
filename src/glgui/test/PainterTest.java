package glgui.test;

import glcommon.Color;
import glcommon.image.ImageIO;
import glcommon.util.ResourceLocator.ClasspathResourceLocator;
import glgui.painter.Painter;
import glgui.painter.Texture;
import glgui.render.pipeline.Pipeline;
import glgui.render.pipeline.gl.GLWindow;

import java.io.IOException;
import java.io.InputStream;

public class PainterTest {
	public static final String IMAGE = "Test/Textures/image.png";
	public static void main(String[] args) throws IOException {
		GLWindow window = new GLWindow();
		window.setVisible(true);
		//window.setSize(100, 100);
		
		Pipeline pipeline = window.getPipeline();
		pipeline.init();
		
		InputStream in = new ClasspathResourceLocator().getResource(IMAGE);
		Texture t = new Texture(ImageIO.s_read(in));
		
		while (!window.getGLWindow().closeRequested()) {
			pipeline.startRendering();
			Painter painter = pipeline.getPainter();
			painter.setColor(Color.BLUE);
			painter.fillRect(0, 0, 100, 100);
			painter.drawTexture(t, 100, 100, 100, 100);
			pipeline.stopRendering();
		}
		t.delete();
		
		window.dispose();
	}
}
