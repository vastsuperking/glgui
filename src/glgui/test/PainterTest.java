package glgui.test;

import glcommon.Color;
import glcommon.image.Image2D;
import glcommon.image.ImageIO;
import glcommon.util.ResourceLocator.ClasspathResourceLocator;
import glgui.painter.ImagePaint;
import glgui.painter.LinearGradientPaint;
import glgui.painter.Painter;
import glgui.painter.graphic.Gradient.GradientDirection;
import glgui.render.pipeline.Pipeline;
import glgui.render.pipeline.gl.GLWindow;

import java.io.IOException;
import java.io.InputStream;

public class PainterTest {
	//public static final String IMAGE = "Test/Textures/test_colors.jpg";
	public static final String IMAGE = "Test/Textures/image.png";
	public static void main(String[] args) throws IOException {
		GLWindow window = new GLWindow();
		window.setVisible(true);
		window.setName("Painter Test");
		window.setSize(1024, 1024);
		
		Pipeline pipeline = window.getPipeline();
		pipeline.init();
		
		
		InputStream in = new ClasspathResourceLocator().getResource(IMAGE);
		Image2D image = ImageIO.s_read(in);
		while (!window.getGLWindow().closeRequested()) {
			pipeline.startRendering();
			Painter painter = pipeline.getPainter();
			painter.setPaint(new ImagePaint(image));

			painter.fillRect(0, 0, 1024, 1024);
			
			painter.setPaint(new LinearGradientPaint(GradientDirection.VERTICAL, Color.BLACK, Color.RED, Color.YELLOW));
			painter.pushTransform();
			painter.scale(3, 3);
			painter.popTransform();
			

			//painter.setPaint(new SolidPaint(Color.BLUE));
			//painter.setPaint(new ImagePaint(image));			
			//g.paint(painter, 100, 100, 1000, 1000);
			//painter.fillRoundedRect(0, 0, 100, 100, 1000, 100);
			//painter.fillArc(500, 500, 500, 500, 
			//				0, (float) -(0.5 * Math.PI));
			//painter.fillElipse(500, 500, 500, 500, 100, -0.5f, 0.5f, 0.5f, 0.5f);
			//painter.fillRoundedRect(0, 0, 1000, 1000, 100, 10, 0, 0, 1, 1);
			painter.fillRoundedRect(0, 0, 1100, 1100, 50, 400);
			painter.scale(2, 2);
			painter.translate(-50, -50);
			//painter.drawImage(image, 0, 0, 100, 100);
			
			
			pipeline.stopRendering();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		image.destroyInstances();
		
		window.dispose();
	}
}
