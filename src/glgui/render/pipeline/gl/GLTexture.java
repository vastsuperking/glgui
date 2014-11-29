package glgui.render.pipeline.gl;

import glgui.painter.Texture;
import glgui.painter.TextureInstance;
import gltools.gl.GL1;
import gltools.texture.Texture2D;
import gltools.texture.TextureFactory;
import gltools.texture.TextureWrapMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GLTexture implements TextureInstance {
	private static final Logger logger = LoggerFactory.getLogger(GLTexture.class);
	
	private Texture2D m_glTexture;
	private GL1 m_gl;
	
	private boolean m_deleted = false;

	public GLTexture(GL1 gl, Texture texture) {
		m_gl = gl;
		m_glTexture = TextureFactory.s_loadTexture(gl, texture.getImage());
		
		//Setup repeating
		m_glTexture.bind(gl);
		m_glTexture.setTWrapMode(TextureWrapMode.REPEAT);
		m_glTexture.setSWrapMode(TextureWrapMode.REPEAT);
		m_glTexture.loadParams(gl);
		m_glTexture.unbind(gl);
	}
	
	public Texture2D getGLTexture() { return m_glTexture; }
	
	@Override
	public void delete() {
		logger.debug("Deleting texture: " + m_glTexture.getID());
		m_glTexture.delete(m_gl);
		m_deleted = true;
	}
	
	@Override
	public void finalize() {
		if (!m_deleted) {
			logger.warn("Auto-cleaning up GLTexture, delete() not called!");
			delete();
		}
	}
}
