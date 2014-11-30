package glgui.render.pipeline.gl;

import glgui.gui.window.FileDropListener;
import glgui.gui.window.MoveListener;
import glgui.gui.window.ResizeListener;
import glgui.gui.window.WindowStateListener;
import glgui.render.pipeline.PWindow;
import glgui.render.pipeline.Pipeline;
import gltools.display.Window;
import gltools.gl.lwjgl.glfw.GLFWWindow;

import java.io.File;
import java.util.HashSet;

public class GLWindow implements PWindow {
	private GLPipeline m_pipeline;
	private Window m_window = null;
	private boolean m_initialized = false;
	
	private HashSet<ResizeListener> m_resizeListeners = new HashSet<ResizeListener>();
	private HashSet<FileDropListener> m_fileListeners = new HashSet<FileDropListener>();
	private HashSet<MoveListener> m_moveListeners = new HashSet<MoveListener>();
	private HashSet<WindowStateListener> m_stateListeners = new HashSet<WindowStateListener>();
	
	public GLWindow() {
		m_window = new GLFWWindow();		
		m_pipeline = new GLPipeline();
		m_window.addResizedListener(new gltools.display.ResizeListener() {
			@Override
			public void onResize(int width, int height) {
				synchronized(m_resizeListeners) {
					for (ResizeListener rl : m_resizeListeners) {
						rl.onResize(width, height);
					}
				}
			}
		});
		m_window.addFileDropListener(new gltools.display.FileDropListener() {
			@Override
			public void filesDropped(Window window, File[] files) {
				synchronized(m_fileListeners) {
					for (FileDropListener l : m_fileListeners) {
						l.filesDropped(files);
					}
				}
			}
		});
		m_window.addMoveListener(new gltools.display.MoveListener() {
			@Override
			public void windowMoved(Window window, int x, int y) {
				synchronized(m_moveListeners) {
					for (MoveListener l : m_moveListeners) {
						l.windowMoved(x, y);
					}
				}
			}
		});
		m_window.addStateListener(new gltools.display.WindowStateListener() {
			@Override
			public void windowClosed(Window window) {
				synchronized(m_stateListeners) {
					for (WindowStateListener l : m_stateListeners) {
						l.windowClosed();
					}
				}
			}
			@Override
			public void windowRefresh(Window window) {
				synchronized(m_stateListeners) {
					for (WindowStateListener l : m_stateListeners) {
						l.windowRefresh();
					}
				}
			}
			@Override
			public void windowFocused(Window window) {
				synchronized(m_stateListeners) {
					for (WindowStateListener l : m_stateListeners) {
						l.windowFocused();
					}
				}
			}
			@Override
			public void windowUnfocused(Window window) {
				synchronized(m_stateListeners) {
					for (WindowStateListener l : m_stateListeners) {
						l.windowUnfocused();
					}
				}
			}
			@Override
			public void windowMinimized(Window window) {
				synchronized(m_stateListeners) {
					for (WindowStateListener l : m_stateListeners) {
						l.windowMinimized();
					}
				}
			}
			@Override
			public void windowMaximized(Window window) {
				synchronized(m_stateListeners) {
					for (WindowStateListener l : m_stateListeners) {
						l.windowMaximized();
					}
				}
			}
		});
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
	public void addResizedListener(ResizeListener l) {
		synchronized(m_resizeListeners) {
			m_resizeListeners.add(l);
		}
	}
	@Override
	public void addFileDropListener(FileDropListener l) {
		synchronized(m_fileListeners) {
			m_fileListeners.add(l);
		}
	}
	@Override
	public void addMoveListener(MoveListener l) {
		synchronized(m_moveListeners) {
			m_moveListeners.add(l);
		}
	}
	@Override
	public void addStateListener(WindowStateListener l) {
		synchronized(m_stateListeners) {
			m_stateListeners.add(l);
		}
	}
	
	@Override
	public void dispose() {
		if (m_window != null) m_window.destroy();
		m_pipeline.dispose();
	}	
}
