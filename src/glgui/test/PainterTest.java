package glgui.test;

import glcommon.Color;
import glcommon.image.Image2D;
import glcommon.image.ImageIO;
import glcommon.util.ResourceLocator.ClasspathResourceLocator;
import glgui.painter.Painter;
import glgui.painter.graphic.Gradient;
import glgui.painter.graphic.Gradient.GradientDirection;
import glgui.render.pipeline.Pipeline;
import glgui.render.pipeline.gl.GLWindow;

import java.io.IOException;
import java.io.InputStream;

public class PainterTest {
	public static final String IMAGE = "Test/Textures/image.png";
	public static void main(String[] args) throws IOException {
		GLWindow window = new GLWindow();
		window.setVisible(true);
		window.setName("Painter Test");
		window.setSize(1024, 1024);
		
		Gradient g = new Gradient(GradientDirection.VERTICAL, Color.BLUE, Color.RED);
		
		Pipeline pipeline = window.getPipeline();
		pipeline.init();
		
		
		InputStream in = new ClasspathResourceLocator().getResource(IMAGE);
		Image2D image = ImageIO.s_read(in);
		while (!window.getGLWindow().closeRequested()) {
			pipeline.startRendering();
			Painter painter = pipeline.getPainter();
			painter.setColor(Color.BLUE);
			
			painter.pushTransform();
			painter.scale(3, 3);
			painter.fillRect(0, 0, 100, 100);
			painter.popTransform();
			
			
			painter.setColor(Color.WHITE);
			g.paint(painter, 100, 100, 1000, 1000);
			
			painter.scale(2, 2);
			painter.translate(-50, -50);
			painter.drawImage(image, 0, 0, 100, 100);
			
			
			pipeline.stopRendering();
		}
		image.destroyInstances();
		
		window.dispose();
	}
}
