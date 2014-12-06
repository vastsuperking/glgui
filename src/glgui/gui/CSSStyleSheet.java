package glgui.gui;

import glcommon.util.ResourceLocator;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class CSSStyleSheet {
	
	public CSSStyleSheet() {}
	
	
	public HashMap<String, String> getProperties(List<String> classes, String id) {
		return null;
	}
	
	public void load(String resource, ResourceLocator locator) throws Exception {
		load(locator.getResource(resource));
	}
	public void load(InputStream input) throws Exception {
		
	}
	
	public static String s_read(InputStream is) throws IOException {
		return IOUtils.toString(is);
	}
}
