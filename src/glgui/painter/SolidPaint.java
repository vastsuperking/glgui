package glgui.painter;

import glcommon.Color;

public class SolidPaint extends Paint {
	private Color m_color;
	
	public SolidPaint(Color color) { m_color = color; }
	
	public Color getColor() { return m_color; }
}
