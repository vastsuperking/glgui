package glgui.css;

import java.util.Collection;
import java.util.LinkedHashSet;

public class SelectorSet extends LinkedHashSet<Selector> {
	private static final long serialVersionUID = 1L;
	
	public SelectorSet() {}
	public SelectorSet(Collection<? extends Selector> collection) {
		super(collection);
	}
}
