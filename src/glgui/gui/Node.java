package glgui.gui;

import glgui.css.CSSDeclaration;
import glgui.css.CSSNode;
import glgui.css.StyleSheet;
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
	
	private Dimension m_preferredSize = new Dimension(0, 0);
	private Dimension m_minimumSize = new Dimension(0, 0);
	private Dimension m_maximumSize = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	
	public Node() {}
	
	public int getX() { return m_x; }
	public int getY() { return m_y; }
	public int getWidth() { return m_width; }
	public int getHeight() { return m_height; }
	
	public Dimension getPreferredSize() { return m_preferredSize; }
	public Dimension getMinimumSize() { return m_minimumSize; }
	public Dimension getMaximumSize() { return m_maximumSize; }
	
	public Parent getParent() { return m_parent; } 
	public String getTag() { return getClass().getSimpleName(); }
	public String getID() { return m_id; }
	public Set<String> getClasses() { return m_classes; }
	public boolean inState(String state) { return false; }
	public StylingManager getStyler() { return m_styler; }
	public List<StyleSheet> getStyleSheets() { return m_stylesheets; }
	
	public List<StyleSheet> getAllStyleSheets() {
		List<StyleSheet> sheets = new ArrayList<StyleSheet>();
		sheets.addAll(m_stylesheets);
		//First add parent's stylesheets
		if (m_parent != null) sheets.addAll(m_parent.getAllStyleSheets());
		return sheets;
	}
	
	public void setX(int x) { m_x = x; }
	public void setY(int y) { m_y = y; }
	public void setWidth(int w) { m_width = w; }
	public void setHeight(int h) { m_height = h; }
	
	public void setParent(Parent parent) { m_parent = parent; }
	
	public void setBounds(int x, int y, int width, int height) {
		m_x = x;
		m_y = y;
		m_width = width;
		m_height = height;
	}
	
	public void setID(String id) {
		m_id = id;
	}
	
	public void addClass(String clazz) {
		m_classes.add(clazz);
	}
	
	public void addStyleSheet(StyleSheet sheet) {
		m_stylesheets.add(sheet);
	}
	
	public void setStyle(String style, Object value) {
		m_styler.setStyle(style, value);
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
	
	public abstract void paintNode(Painter p);
}
