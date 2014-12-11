package glgui.css;

import glgui.css.eval.CSSFunction;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class CSSTest {
	public static void main(String[] args) throws IOException {
		String css = s_read(CSSTest.class.getClassLoader().getResourceAsStream("Test/Stylesheets/test.css"));
		char[] cssChars = css.toCharArray();
		
		CSSParser parser = new CSSParser();
		parser.getEvaluator().addFunction(new CSSFunction("bar", Integer.class) {
			@Override
			public Object execute(Object... obj) {
				int i = (int) obj[0];
				return i + 1;
			}
		});
		parser.getEvaluator().addFunction(new CSSFunction("foo", Integer.class, Integer.class) {
			@Override
			public Object execute(Object... obj) {
				int a = (int) obj[0];
				int b = (int) obj[1];
				return a + b;
			}
		});
		
		for (char c : cssChars) {
			parser.process(c);
		}
		
		List<Rule> rules = parser.getRules();
		for (Rule r : rules) {
			System.out.println("Rule: " + r.getSelectors() + " " + r.getDeclarations().values());
		}
	}
	
	public static String s_read(InputStream input) throws IOException {
		return IOUtils.toString(input);
	}
}
