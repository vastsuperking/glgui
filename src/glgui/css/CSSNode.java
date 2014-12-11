package glgui.css;

import java.util.Set;

public interface CSSNode {
	public CSSNode getParent();
	
	public String getTag();
	public String getID();
	public Set<String> getClasses();
	
	public boolean inState(String state);
}
