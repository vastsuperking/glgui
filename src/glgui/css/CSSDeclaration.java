package glgui.css;

public class CSSDeclaration {
	private String m_name;
	private String m_value;
	private Object m_valObject;
	
	public CSSDeclaration() {
		
	}
	
	public void setName(String name) { m_name = name; }
	public void setValue(String value) { m_value = value; }
	public void setObjectValue(Object value) { m_valObject = value; }
	
	public String getName() { return m_name; }
	public String getValue() { return m_value; }
	public Object getObjectValue() { return m_valObject; }
	
	public String toString() {
		return m_name + " = " + m_value + "(" + m_valObject + ")";
	}
}
