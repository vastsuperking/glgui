package glgui.css;

import static glgui.css.Constants.*;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CSSTokenizer {
	private Queue<String> m_tokenQueue = new ConcurrentLinkedQueue<String>();
	
	private StringBuilder m_token = new StringBuilder();
	
	public boolean isTokenAvailable() {
		return m_tokenQueue.peek() != null;
	}
	
	public String getToken() {
		return m_tokenQueue.poll();
	}
	
	public void process(char c) {
		//All special CSS characters
		if (c == SPACE || c == NEW_LINE || c == TAB ||
			c == BRACES_BEG || c == BRACES_END || c == COLON || c == SEMICOLON) {
			m_tokenQueue.add(m_token.toString());
			m_tokenQueue.add(Character.toString(c));
			m_token = new StringBuilder();
		} else m_token.append(c);
		
		//Flush if a comment end or start token
		if (c == STAR) {
			String token = m_token.toString();
			if (token.length() > 0 && token.charAt(token.length() - 1) == BACKSLASH) {
				m_tokenQueue.add(m_token.toString());
				m_token = new StringBuilder();				
			}
		} else if (c == BACKSLASH) {
			String token = m_token.toString();
			if (token.length() > 0 && token.charAt(token.length() - 1) == STAR) {
				m_tokenQueue.add(m_token.toString());
				m_token = new StringBuilder();				
			}			
		}
	}
}
