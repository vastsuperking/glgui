package glgui.css;

import static glgui.css.Constants.*;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CSSTokenizer {
	private Queue<String> m_tokenQueue = new ConcurrentLinkedQueue<String>();
	
	private StringBuilder m_token = new StringBuilder();
	
	public boolean isTokenAvailable() {
		return !m_tokenQueue.isEmpty();
	}
	
	public String getToken() {
		return m_tokenQueue.poll();
	}
	
	public void process(char c) {
		//All special CSS characters
		if (c == SPACE || c == NEW_LINE || c == TAB ||
			c == BRACES_BEG || c == BRACES_END || c == COLON || c == SEMICOLON || c == COMMA) {
			if (m_token.length() != 0) m_tokenQueue.add(m_token.toString());
			m_tokenQueue.add(Character.toString(c));
			m_token = new StringBuilder();
		} else m_token.append(c);
		
		//Flush if a comment end or start token
		if (c == STAR) {
			String token = m_token.toString();
			if (token.length() > 1 && token.charAt(token.length() - 2) == BACKSLASH) {
				//if the star is after a /, flush everything before the slash
				//as well as a /* delimeter
				m_tokenQueue.add(token.substring(0, token.length() - 2));
				m_tokenQueue.add(START_BLOCK_COMMENT);
				m_token = new StringBuilder();				
			}
		} else if (c == BACKSLASH) {
			String token = m_token.toString();
			if (token.length() > 1 && token.charAt(token.length() - 2) == STAR) {
				//if the slash comes after a star, process everything before the */ 
				//and send a */ delimeter
				m_tokenQueue.add(token.substring(0, token.length() - 2));
				m_tokenQueue.add(END_BLOCK_COMMENT);
				m_token = new StringBuilder();				
			}			
		}
	}
}
