package glgui.gui.window;

import glgui.gui.BorderPane;
import glgui.gui.Pane;
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
