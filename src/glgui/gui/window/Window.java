package glgui.gui.window;

import glcommon.input.Key;
import glcommon.input.KeyListener;
import glcommon.input.Keyboard;
import glcommon.input.Mouse;
import glcommon.input.Mouse.MouseButton;
import glcommon.input.MouseListener;
import glcommon.vector.Vector2f;
import glgui.gui.BorderPane;
import glgui.input.KeyPressedEvent;
import glgui.input.KeyReleasedEvent;
import glgui.input.MouseButtonEvent;
import glgui.input.MouseEnterEvent;
import glgui.input.MouseExitEvent;
import glgui.input.MouseMoveEvent;
import glgui.input.MouseWheelEvent;
import glgui.painter.Painter;
import glgui.render.pipeline.PWindow;
import glgui.render.pipeline.PWindowProvider;
import glgui.render.pipeline.Pipeline;

public class Window {
	private PWindow m_window;
	private boolean m_initialized;
	
	private BorderPane m_contentPane = new BorderPane();
	
	public Window() {
		m_window = PWindowProvider.getDefaultProvider().create();
		m_window.addResizedListener(new ResizeListener() {
			@Override
			public void onResize(int width, int height) {
				m_contentPane.setWidth(width);
				m_contentPane.setHeight(height);
				m_contentPane.revalidate();
			}
		});
		m_window.getMouse().addListener(new MouseListener() {
			@Override
			public void mouseMoved(Mouse m, int x, int y, Vector2f delta) {
				m_contentPane.onEvent(new MouseMoveEvent(x, y, delta));
			}
			@Override
			public void mouseScroll(Mouse m, float dx, float dy) {
				m_contentPane.onEvent(new MouseWheelEvent(m.getX(), m.getY(), dx, dy));
			}
			@Override
			public void mouseDelta(Mouse m, int dx, int dy) {}
			
			@Override
			public void mouseButtonPressed(Mouse m, MouseButton button) {
				m_contentPane.onEvent(new MouseButtonEvent(m.getX(), m.getY(), button, true));
			}
			@Override
			public void mouseButtonReleased(Mouse m, MouseButton button) {
				m_contentPane.onEvent(new MouseButtonEvent(m.getX(), m.getY(), button, false));
			}
			@Override
			public void mouseEntered(Mouse m) {
				m_contentPane.onEvent(new MouseEnterEvent(m.getX(), m.getY()));
			}
			@Override
			public void mouseExited(Mouse m) {
				m_contentPane.onEvent(new MouseExitEvent(m.getX(), m.getY()));
			}
		});
		m_window.getKeyboard().addListener(new KeyListener() {
			@Override
			public void keyPressed(Keyboard k, Key key) {
				m_contentPane.onEvent(new KeyPressedEvent(key));
			}
			@Override
			public void keyReleased(Keyboard k, Key key) {
				m_contentPane.onEvent(new KeyReleasedEvent(key));
			}
		});
		m_window.addStateListener(new WindowStateListener() {
			@Override
			public void windowClosed() {}
			@Override
			public void windowRefresh() {}

			@Override
			public void windowFocused() {}

			@Override
			public void windowUnfocused() {
				m_contentPane.unfocus();
			}

			@Override
			public void windowMinimized() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowMaximized() {
				// TODO Auto-generated method stub
				
			}
		});
		
		m_contentPane.setID("contentPane");
	}
	protected PWindow getImp() {
		return m_window;
	}
	public String getName() {
		checkValid();
		return m_window.getName();
	}
	public int getWidth() {
		return m_window.getWidth();
	}
	public int getHeight() {
		return m_window.getHeight();
	}
	public BorderPane getContentPane() {
		return m_contentPane;
	}
	
	public boolean isVisible() {
		return m_window.isVisible();
	}
	public boolean isInitialized() {
		return m_window.isInitialized();
	}
	
	public void setName(String name) {
		checkValid();
		m_window.setName(name);
	}
	public void setSize(int width, int height) {
		checkValid();
		m_window.setSize(width, height);
	}
	public void setVisible(boolean visible) {
		checkValid();
		boolean wasVisible = m_window.isVisible();
		m_window.setVisible(visible);
		if (visible && !m_initialized) { m_initialized = true; }
		if (!wasVisible && visible) RenderThread.getInstance().addWindow(this);
		else if (wasVisible && !visible) RenderThread.getInstance().removeWindow(this);
	}
	
	public void addStateListener(WindowStateListener l) {
		m_window.addStateListener(l);
	}
	public void addMoveListener(MoveListener l) {
		m_window.addMoveListener(l);
	}
	public void addFileDropListener(FileDropListener l) {
		m_window.addFileDropListener(l);
	}
	public void addResizedListener(ResizeListener l) {
		m_window.addResizedListener(l);
	}
	
	public void paint() {
		Pipeline pipeline = m_window.getPipeline();
		if (!m_initialized) {
			pipeline.init();
		}
		pipeline.startRendering();
		
		Painter painter = pipeline.getPainter();
		m_contentPane.paint(painter);
		
		pipeline.stopRendering();
	}
	
	public void dispose() {
		if (isVisible()) RenderThread.getInstance().removeWindow(this);
		m_window.getPipeline().dispose();
		m_window.dispose();
		m_window = null;
	}
	
	private void checkValid() {
		if (m_window == null) throw new RuntimeException("Already disposed()!");
	}
}
