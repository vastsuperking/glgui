package glgui.gui;

import glcommon.util.Timer;
import glextra.FBuffer;
import glextra.FBuffer.FBufferAttachment;
import glextra.FBuffer.FBufferMode;
import glgui.painter.Paint;
import glgui.painter.Painter;
import glgui.render.pipeline.gl.GLPainter;
import gltools.buffer.FrameBuffer.AttachmentPoint;
import gltools.display.GLApplication;
import gltools.gl.GL;
import gltools.shader.InputUsage;
import gltools.texture.Texture2D;
import gltools.texture.TextureFormat;

import java.util.concurrent.atomic.AtomicBoolean;


public class GLNode extends Node {
	private FBuffer m_buffer;
	private Texture2D m_colorBuffer;
	
	private AtomicBoolean m_resizedBuffer = new AtomicBoolean(false);

	private Timer m_timer = new Timer();
	private GLApplication m_application;
	
	public GLNode() {
	}
	
	public void setApplication(GLApplication app) { m_application = app; }
	public GLApplication getApplication() { return m_application; }
	
	@Override
	public void setWidth(int width) {
		super.setWidth(width);
		m_resizedBuffer.set(true);;
	}
	@Override
	public void setHeight(int height) {
		super.setHeight(height);
		m_resizedBuffer.set(true);;
	}
	
	@Override
	public int getWidth() {
		return super.getWidth();
	}

	@Override
	public int getHeight() {
		return super.getHeight();
	}

	@Override
	public void paintNode(Painter p) {
		Paint paint = p.getPaint();
		p.setPaint(null);
		if (p instanceof GLPainter) {
			GLPainter painter = (GLPainter) p;
			GL gl = painter.getGL();
			
			boolean initialized = false;
			
			if (m_buffer == null) {
				initialized = true;
				m_buffer = new FBuffer(getWidth(), getHeight());
				FBufferAttachment attachment = new FBufferAttachment(0, AttachmentPoint.COLOR_ATTACHMENT0, 
						 											InputUsage.GBUFFER_DIFFUSE_SAMPLER,
						 											TextureFormat.RGBA8); 
				m_buffer.addAttachment(attachment);
				m_buffer.init(gl);
				
				m_colorBuffer = attachment.getTexture();
			}
			if (m_resizedBuffer.get() && m_buffer != null)
				m_buffer.resize(gl, getWidth(), getHeight());
			if (m_application != null) {
				painter.unset();
				
				m_buffer.bind(gl, FBufferMode.WRITE);

				if (initialized) {
					m_application.init(gl, null, null, getWidth(), getHeight());
				}
				
				if (m_resizedBuffer.get()) {
					m_application.resize(gl, getWidth(), getHeight());
				}
				
				m_application.render(gl);
				m_timer.mark();
				m_application.update(m_timer.getDeltaMillis());

				m_buffer.unbind(gl, FBufferMode.WRITE);
				
				
				painter.set();

				painter.drawTexture(m_colorBuffer, 0, 0, getWidth(), getHeight());
			}
		} else throw new RuntimeException("Not using GL, cannot create GLWidget!");
		p.setPaint(paint);
		
		m_resizedBuffer.set(false);;
	}	
}
