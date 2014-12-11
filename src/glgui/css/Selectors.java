package glgui.css;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

public class Selectors extends ArrayList<Selector> {
	private static final long serialVersionUID = 1L;
	
	public Selectors() {}
	public Selectors(Collection<? extends Selector> collection) {
		super(collection);
	}
	
	public boolean canInheritFrom(Selectors selectors) {
		ListIterator<Selector> thisIt = listIterator(size());
		ListIterator<Selector> otherIt = selectors.listIterator(selectors.size());
		
		while (thisIt.hasPrevious()) {
			Selector s = thisIt.next();
			if (otherIt.hasPrevious()) {
				Selector s2 = otherIt.next();
				if (!s.equals(s2)) return false;
			} else break;
		}
		return true;
	}
	public boolean appliesToWithoutStates(CSSNode node) {
		ListIterator<Selector> it = listIterator(size());

		CSSNode n = node;
		while (it.hasPrevious()) {
			Selector s = it.previous();
			
			if (!s.appliesToWithoutStates(n)) return false;
			if (node.getParent() != null) n = node.getParent();
			else break;
		}
		return true;
	}
	public boolean appliesTo(CSSNode node) {
		ListIterator<Selector> it = listIterator(size());

		CSSNode n = node;
		while (it.hasPrevious()) {
			Selector s = it.previous();
			
			if (!s.appliesTo(n)) return false;
			if (node.getParent() != null) n = node.getParent();
			else break;
		}
		return true;
	}
}
