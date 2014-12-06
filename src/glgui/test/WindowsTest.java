package glgui.test;

import glcommon.font.Font;
import glcommon.font.JavaFontConverter;
import glcommon.util.ResourceLocator.ClasspathResourceLocator;
import glgui.gui.BorderPane;
import glgui.gui.Button;
import glgui.gui.CSSStyleSheet;
import glgui.gui.Pane;
import glgui.gui.window.Window;
import glgui.gui.window.WindowStateListener;
import glgui.painter.Painter;
import glgui.render.pipeline.gl.GLWindowProvider;

import java.io.IOException;
import java.util.ArrayList;

public class WindowsTest {
	public static final String IMAGE = "Test/Textures/image.png";
	public static void main(String[] args) throws IOException {
		GLWindowProvider.s_use();
		
		CSSStyleSheet sheet = new CSSStyleSheet();
		try {
			sheet.load("Test/Stylesheets/test.css", new ClasspathResourceLocator());
			ArrayList<String> classes = new ArrayList<String>();
			classes.add("bar");
			System.out.println(sheet.getProperties(classes, "foo"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Window window = new Window();
		window.setVisible(true);
		window.setName("Window");
		window.setSize(1024, 1024);
		
		Window window2 = new Window();
		window2.setVisible(true);
		window2.setName("Window2");
		window2.setSize(100, 100);
		BorderPane content = window.getContentPane();
		content.setCenter(new Pane() {
			@Override
			public void paintNode(Painter p) {
				p.fillRect(10, 10, 100, 100);
			}
		});
		BorderPane content2 = window2.getContentPane();
		Font font = JavaFontConverter.s_convert(new java.awt.Font("Serif", java.awt.Font.PLAIN, 100));
		content2.setCenter(new Button("Foo", font));
		/*content2.setCenter(new Pane() {
			@Override
			public void paintNode(Painter p) {
				p.drawRect(10, 10, 100, 100);
			}
		});*/
		
		window.addStateListener(new WindowStateListener() {
			@Override
			public void windowClosed() {
				System.out.println("Window closed");
			}

			@Override
			public void windowRefresh() {
				System.out.println("Window refreshed");
			}

			@Override
			public void windowFocused() {
				System.out.println("Window focused");
			}

			@Override
			public void windowUnfocused() {
				System.out.println("Window unfocused");
			}

			@Override
			public void windowMinimized() {
				System.out.println("Window minimized");
			}

			@Override
			public void windowMaximized() {
				System.out.println("Window maximized");
			}});
		//window.dispose();
		//window2.dispose();
		
		content.revalidate();
		content2.revalidate();
	}
}