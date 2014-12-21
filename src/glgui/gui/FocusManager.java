package glgui.gui;

public class FocusManager {
	private static FocusManager s_instance = null;
	
	public Node m_focusedNode = null;
	
	public Node getFocused() { return m_focusedNode; }
	public boolean isFocused(Node n) { return n == m_focusedNode; } 

	public void resetFocus() { m_focusedNode = null; }
	
	public void focus(Node n) {
		//Inform the previous node
		//that it has been unfocused
		if (m_focusedNode != null && m_focusedNode != n) m_focusedNode.unfocus();
		m_focusedNode = n;
	}
	
	public static FocusManager getInstance() {
		if (s_instance == null) {
			s_instance = new FocusManager();
		}
		return s_instance;
	}
}
