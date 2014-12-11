package glgui.css;

import glcommon.util.ResourceLocator;
import glcommon.util.ResourceLocator.ClasspathResourceLocator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;

public class StyleSheet {
	private CSSParser m_parser = new CSSParser();
	
	public StyleSheet() {}
	
	public CSSParser getParser() { return m_parser; }
	
	public HashMap<String, CSSDeclaration> getDeclarations(CSSNode node) {
		HashMap<String, CSSDeclaration> properties = new HashMap<String, CSSDeclaration>();
		for (Rule r : m_parser.getRules()) {
			if (r.appliesTo(node)) {
				properties.putAll(r.getDeclarations());
			}
		}
		return properties;
	}
	
	public void load(String resource, ResourceLocator locator) throws Exception {
		load(locator.getResource(resource));
	}
	public void load(final InputStream input) throws Exception {
		m_parser.parse(s_read(input));
	}
	
	public static String s_read(InputStream is) throws IOException {
		return IOUtils.toString(is);
	}
	
	public static void main(String[] args) throws Exception {
		StyleSheet sheet = new StyleSheet();
		sheet.load("Test/Stylesheets/test.css", new ClasspathResourceLocator());
		System.out.println(sheet.getDeclarations(new TestCSSNode(null, "foo", "foobar", "bar")));
	}
	
	private static class TestCSSNode implements CSSNode {
		private CSSNode m_parent;
		private String m_tag;
		private String m_id;
		private Set<String> m_classes;
		
		public TestCSSNode(CSSNode parent, String tag, String id, String... classes) {
			m_parent = parent;
			m_tag = tag;
			m_id = id;
			m_classes = new HashSet<String>();
			m_classes.addAll(Arrays.asList(classes));
		}
		@Override
		public CSSNode getParent() {
			return m_parent;
		}
		@Override
		public String getTag() {
			return m_tag;
		}
		@Override
		public String getID() {
			return m_id;
		}
		@Override
		public Set<String> getClasses() {
			return m_classes;
		}
		@Override
		public boolean inState(String state) {
			return false;
		}
	}
}
