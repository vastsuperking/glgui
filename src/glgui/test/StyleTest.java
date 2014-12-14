package glgui.test;

import glcommon.Color;
import glcommon.util.ResourceLocator.ClasspathResourceLocator;
import glgui.css.StyleSheet;
import glgui.css.eval.CSSFunction;
import glgui.gui.BorderPane;
import glgui.gui.Button;
import glgui.gui.Dimension;
import glgui.gui.window.Window;
import glgui.gui.window.WindowStateListener;
import glgui.painter.LinearGradientPaint;
import glgui.painter.LinearGradientPaint.Direction;
import glgui.render.pipeline.gl.GLWindowProvider;

import java.io.IOException;

public class StyleTest {
	public static final String STYLESHEET = "Test/Stylesheets/test.css";
	public static final String IMAGE = "Test/Textures/image.png";
	
	public static void main(String[] args) throws IOException {
		GLWindowProvider.s_use();
		
		StyleSheet ss = new StyleSheet();
		ss.getParser().getEvaluator().addFunction(new CSSFunction("vertical-gradient", Color.class, Color.class) {
			@Override
			public Object execute(Object... args) {
				Color a = (Color) args[0];
				Color b = (Color) args[1];
				return new LinearGradientPaint(Direction.VERTICAL, a, b);
			}			
		});
		ss.getParser().getEvaluator().addFunction(new CSSFunction("horizontal-gradient", Color.class, Color.class) {
			@Override
			public Object execute(Object... args) {
				Color a = (Color) args[0];
				Color b = (Color) args[1];
				return new LinearGradientPaint(Direction.HORIZONTAL, a, b);
			}			
		});
		try {
			ss.load(STYLESHEET, new ClasspathResourceLocator());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Window window = new Window();
		window.setName("Window");
		window.setVisible(true);
		window.setSize(1024, 1024);
		System.out.println("Window initialized");

		
		window.getContentPane().addStyleSheet(ss);
		
		BorderPane content = window.getContentPane();
		
		Button buttonA = new Button("Foo");
		buttonA.setID("foo");
		
		Button buttonB = new Button("Bar");
		buttonB.setMinimumSize(new Dimension(300, 300));
		
		content.setCenter(buttonA);
		content.setLeft(buttonB);
		
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
		
		content.revalidate();
	}
}