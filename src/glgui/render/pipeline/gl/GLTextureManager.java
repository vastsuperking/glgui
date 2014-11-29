package glgui.render.pipeline.gl;

import glgui.painter.Texture;
import glgui.painter.TextureInstance;
import gltools.gl.GL1;
import gltools.texture.Texture2D;

public class GLTextureManager implements TextureInstance.Key<GLTexture> {
	public Texture2D getGLTexture(GL1 gl, Texture tex) {
		GLTexture texture = null;
		
		if (tex.hasInstance(this)) {
			texture = tex.getInstance(this);
		} else {
			texture = new GLTexture(gl, tex);
			tex.addInstance(this, texture);
		}
		
		return texture.getGLTexture();
	}
}
