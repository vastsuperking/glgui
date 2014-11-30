package glgui.test;

import glgui.gui.window.Window;
import glgui.render.pipeline.gl.GLWindowProvider;

import java.io.IOException;

public class WindowsTest {
	public static final String IMAGE = "Test/Textures/image.png";
	public static void main(String[] args) throws IOException {
		GLWindowProvider.s_use();
		
		Window window = new Window();
		window.setVisible(true);
		window.setName("Window");
		window.setSize(1024, 1024);
		
		Window window2 = new Window();
		window2.setVisible(true);
		window2.setName("Window2");
		window2.setSize(100, 100);
		
		//window.dispose();
		//window2.dispose();
	}
}
