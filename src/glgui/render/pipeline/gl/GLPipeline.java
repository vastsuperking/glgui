package glgui.render.pipeline.gl;

import glgui.painter.Painter;
import glgui.render.pipeline.PDisplay;
import glgui.render.pipeline.Pipeline;
import gltools.display.ResizeListener;
import gltools.gl.GL;

public class GLPipeline implements Pipeline {
	private GLWindow m_window;
	private GLPainter m_painter;
	private GL m_gl;
	
	public GLPipeline(GLWindow window) {
		m_window = window;
		m_gl = window.getGLWindow().getGL();
		m_painter = new GLPainter(m_gl, this);
		
		m_painter.updateProjection(m_window.getGLWindow().getWidth(), m_window.getGLWindow().getHeight());

		m_window.getGLWindow().addResizedListener(new ResizeListener() {
			@Override
			public void onResize(int width, int height) {
				m_painter.getGL().glViewport(0, 0, width, height);
				m_painter.updateProjection(width, height);
			}
		});
	}
	
	@Override
	public PDisplay getDisplay() {
		return m_window;
	}

	@Override
	public Painter getPainter() {
		return m_painter;
	}

	@Override
	public void init() {}

	@Override
	public void dispose() {
		m_gl.destroy();
	}

	@Override
	public void startRendering() {
		m_painter.start();
	}

	@Override
	public void stopRendering() {
		m_window.getGLWindow().update();
		m_painter.stop();
	}
}
