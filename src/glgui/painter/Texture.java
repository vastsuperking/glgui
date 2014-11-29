package glgui.painter;

import glcommon.image.Image2D;
import glcommon.image.ImageIO;
import glcommon.util.ResourceLocator;
import glgui.painter.TextureInstance.Key;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Texture {
	private static final Logger logger = LoggerFactory.getLogger(Texture.class);

	public Image2D m_image;
	public HashMap<Key<? extends TextureInstance>, TextureInstance> m_instances = 
				new HashMap<Key<? extends TextureInstance>, TextureInstance>();
	
	public Texture(Image2D image) {
		m_image = image;
	}
	public Texture(File file) throws IOException {
		this(ImageIO.s_read(file));
	}
	public Texture(InputStream in) throws IOException {
		this(ImageIO.s_read(in));
	}
	public Texture(String resource, ResourceLocator locator) throws IOException {
		this(locator.getResource(resource));
	}
	
	public <T extends TextureInstance> void addInstance(TextureInstance.Key<T> key, T i) {
		m_instances.put(key, i);
	}
	public void removeInstance(TextureInstance.Key< ? extends TextureInstance> key) {
		m_instances.remove(key);
	}
	
	public  boolean hasInstance(TextureInstance.Key<? extends TextureInstance> key) {
		return m_instances.containsKey(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends TextureInstance> T getInstance(TextureInstance.Key<T> key) {
		return (T) m_instances.get(key);
	}
	
	public Image2D getImage() {
		return m_image;
	}
	
	public void delete() {
		synchronized(m_instances) {
			for (TextureInstance o : m_instances.values()) {
				o.delete();
			}
			m_instances.clear();
		}
	}
	
	@Override
	public void finalize() {
		if (m_instances.size() > 0) {
			logger.warn("Auto-cleaning up texture, delete() not called!");
			delete();
		}
	}
	
}
