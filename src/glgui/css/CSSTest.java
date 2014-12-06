package glgui.css;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class CSSTest {
	public static void main(String[] args) throws IOException {
		String css = s_read(CSSTest.class.getClassLoader().getResourceAsStream("Test/Stylesheets/test.css"));
		char[] cssChars = css.toCharArray();
		
		CSSParser parser = new CSSParser();
		
		for (char c : cssChars) {
			parser.process(c);
		}
		
		List<Rule> rules = parser.getRules();
		for (Rule r : rules) {
			System.out.println("Rule: " + r.getSelectors() + " " + r.getParameters());
		}
	}
	
	public static String s_read(InputStream input) throws IOException {
		return IOUtils.toString(input);
	}
}
