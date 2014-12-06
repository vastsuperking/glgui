package glgui.gui.window;

import java.util.HashSet;
import java.util.Iterator;

public class RenderThread implements Runnable {
	private static RenderThread s_instance;
	
	private HashSet<Window> m_windows = new HashSet<Window>();
	private Thread m_thread;
	
	public RenderThread() {
		m_thread = new Thread(this);
		m_thread.setName("glgui-render");
	}
	public void addWindow(Window window) {
		synchronized(m_windows) {
			m_windows.add(window);
			//If the thread is not alive, start it
			if (m_windows.size() > 0 && !m_thread.isAlive()) {
				m_thread.start();
			}
		}
	}
	public void removeWindow(Window window) {
		synchronized(m_windows) {
			if (m_windows.contains(window)) {
				m_windows.remove(window);
			}
			
			if (m_windows.size() < 1 && m_thread.isAlive()) {
				m_thread.interrupt();
			}
		}
	}
	
	public void run() {
		while (true) {
			synchronized(m_windows) {
				Iterator<Window> it = m_windows.iterator();
				while (it.hasNext()) {
					Window w = it.next();
					if (w.isInitialized()) {
						if (w.getImp().closeRequested()) {
							//Remove it from the list before it has a chance to
							it.remove();
							w.dispose();
						} else {
							w.paint();
						}
					}
				}
			}
			if (Thread.interrupted()) break;
			else
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					break;
				}
		}
	}
	
	public static RenderThread getInstance() {
		if (s_instance == null) s_instance = new RenderThread();
		return s_instance;
	}
}
