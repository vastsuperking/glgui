package glgui.render.pipeline.gl;

import glgui.render.pipeline.PWindow;
import glgui.render.pipeline.Pipeline;
import gltools.display.Window;
import gltools.gl.lwjgl.glfw.GLFWWindow;

public class GLWindow implements PWindow {
	private GLPipeline m_pipeline;
	private Window m_window = null;
	private boolean m_initialized = false;
	
	public GLWindow() {
		m_window = new GLFWWindow();		
		m_pipeline = new GLPipeline();
	}
	
	@Override
	public int getWidth() { return m_window.getWidth(); }
	@Override
	public int getHeight() { return m_window.getHeight(); }

	
	@Override
	public Pipeline getPipeline() {
		return m_pipeline;
	}

	@Override
	public void setName(String name) {
		m_window.setTitle(name);
	}

	@Override
	public String getName() {
		return m_window.getTitle();
	}
	@Override
	public boolean isVisible(){
		return m_window.isVisible();
	}
	@Override
	public boolean isInitialized() {
		return m_window.isInitialized();
	}

	public Window getGLWindow() { return m_window; }
	
	@Override
	public void setVisible(boolean visible) {
		if (!m_initialized && visible) {
			m_window.setResizable(true);
			m_window.init();
			m_pipeline.setup(this);
			m_initialized = true;
		} else m_window.setVisible(visible);
	}
	@Override
	public void setSize(int width, int height) {
		m_window.setSize(width, height);
	}
	
	@Override
	public void dispose() {
		if (m_window != null) m_window.destroy();
		m_pipeline.dispose();
	}
	
}
