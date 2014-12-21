package glgui.gui;

import glgui.css.CSSDeclaration;
import glgui.css.CSSNode;
import glgui.css.StyleSheet;
import glgui.input.Event;
import glgui.input.MouseButtonEvent;
import glgui.input.MouseEnterEvent;
import glgui.input.MouseExitEvent;
import glgui.painter.Painter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public abstract class Node implements Stylable, CSSNode {
	private int m_x = 0;
	private int m_y = 0;
	
	private String m_id = null;
	private HashSet<String> m_classes = new HashSet<String>();
	private ArrayList<StyleSheet> m_stylesheets = new ArrayList<StyleSheet>();
	
	protected StylingManager m_styler = new StylingManager();
	
	private Parent m_parent = null;
	
	private int m_width = 0;
	private int m_height = 0;
	
	protected Dimension m_preferredSize = new Dimension(0, 0);
	protected Dimension m_minimumSize = new Dimension(0, 0);
	protected Dimension m_maximumSize = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	
	private boolean m_hovered = false;
	
	protected boolean m_waitingMouseRelease = false;
	
	public Node() {}
	
	public int getX() { return m_x; }
	public int getY() { return m_y; }
	public int getWidth() { return m_width; }
	public int getHeight() { return m_height; }
	
	public Dimension getPreferredSize() {
		return new Dimension(Math.max(m_preferredSize.getWidth(), m_minimumSize.getWidth()),
							 Math.max(m_preferredSize.getHeight(), m_minimumSize.getHeight()));
	}
	public Dimension getMinimumSize() { return m_minimumSize; }
	public Dimension getMaximumSize() { return m_maximumSize; }
	
	public void setPreferrredSize(Dimension d) { m_preferredSize = d; }
	public void setMinimumSize(Dimension d) { m_minimumSize = d; }
	public void setMaximumSize(Dimension d) { m_maximumSize = d; }
	
	public Parent getParent() { return m_parent; } 
	public String getTag() { return getClass().getSimpleName(); }
	public String getID() { return m_id == null ? "" : m_id; }
	public Set<String> getClasses() { return m_classes; }
	public StylingManager getStyler() { return m_styler; }
	public List<StyleSheet> getStyleSheets() { return m_stylesheets; }
	
	public List<StyleSheet> getAllStyleSheets() {
		List<StyleSheet> sheets = new ArrayList<StyleSheet>();
		sheets.addAll(m_stylesheets);
		//First add parent's stylesheets
		if (m_parent != null) sheets.addAll(m_parent.getAllStyleSheets());
		return sheets;
	}
	
	public int getAbsoluteX() {
		if (m_parent != null) return m_x + m_parent.getAbsoluteX();
		else return m_x;
	}
	public int getAbsoluteY() {
		if (m_parent != null) return m_y + m_parent.getAbsoluteX();
		else return m_y;
		
	}
	
	public boolean isHovered() { return m_hovered; }
	public boolean isFocused() { return FocusManager.getInstance().isFocused(this); }
	
	public void setX(int x) { m_x = x; }
	public void setY(int y) { m_y = y; }
	public void setWidth(int w) { m_width = Math.max(w, m_minimumSize.getWidth()); }
	public void setHeight(int h) { m_height = Math.max(h, m_minimumSize.getHeight()); }
	
	public void setParent(Parent parent) { 
		m_parent = parent;
	}
	
	public void setBounds(int x, int y, int width, int height) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}
	
	public void setID(String id) {
		m_id = id;
	}
	
	public void addClass(String clazz) {
		m_classes.add(clazz);
	}
	
	public void addStyleSheet(StyleSheet sheet) {
		m_stylesheets.add(sheet);
		style();
	}
	
	public void setStyle(String style, Object value) {
		m_styler.setStyle(style, value);
	}
	
	public boolean hasFocusedChild() {
		return false;
	}
	
	public boolean inState(String state) {
		switch(state) {
		case "hovered": return isHovered();
		case "focused": return isFocused();
		default: return false;
		}
	}
	
	public void focus() {
		FocusManager.getInstance().focus(this);
		style();
	}
	public void unfocus() {
		FocusManager.getInstance().resetFocus();
		style();
	}
	
	public void style() {
		m_styler.resetStyles();
		LinkedHashMap<String, CSSDeclaration> declarations = new LinkedHashMap<String, CSSDeclaration>();
		for (StyleSheet s : m_stylesheets) {
			declarations.putAll(s.getDeclarations(this));
		}
		List<StyleSheet> sheets = getAllStyleSheets();
		for (StyleSheet s : sheets) declarations.putAll(s.getDeclarations(this));
		
		for (Entry<String, CSSDeclaration> entry : declarations.entrySet()) {
			m_styler.setStyle(entry.getKey(), entry.getValue().getObjectValue());
		}
	}
	
	/**
	 * Will transform and paint the widget
	 * by transforming and then calling
	 * <code>
	 * paintWidget(p);
	 * </code>
	 */
	public void paint(Painter p) {
		p.pushTransform();
		
		p.translate(m_x, m_y);
		
		paintNode(p);
		
		p.popTransform();
	}
	
	/**
	 * Will lay out all a node's (possible) components
	 * non-parent nodes will usually not do anything
	 */
	public void validate() {}
	
	public void revalidate() {
		validate();
	}
	
	public void onEvent(Event e) {
		if (e instanceof MouseEnterEvent) {
			m_hovered = true;
			style();
		} else if (e instanceof MouseExitEvent) {
			m_hovered = false;
			style();
		} else if (e instanceof MouseButtonEvent) {
			if (((MouseButtonEvent) e).isPressed() && !isFocused()) focus();
			m_waitingMouseRelease = ((MouseButtonEvent) e).isPressed();
		}
	}
	
	public abstract void paintNode(Painter p);
}
