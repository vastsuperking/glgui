package glgui.render.pipeline.gl;

import glcommon.image.Image2D;
import glcommon.util.ResourceLocator.ClasspathResourceLocator;
import glcommon.vector.Matrix3f;
import glgui.painter.ImagePaint;
import glgui.painter.LinearGradientPaint;
import glgui.painter.Paint;
import glgui.painter.SolidPaint;
import gltools.gl.GL;
import gltools.shader.DataType;
import gltools.shader.InputUsage;
import gltools.shader.Program;
import gltools.shader.ProgramXMLLoader;
import gltools.shader.Uniform;
import gltools.texture.Texture2D;
import gltools.util.GLMatrix3f;

import java.util.HashMap;

public class GLPaintManager {
	private HashMap<Class<? extends Paint>, GLPaintType<? extends Paint>> m_paints = 
			new HashMap<Class<? extends Paint>, GLPaintType<? extends Paint>>(); 
	
	private boolean m_bound = false;
	
	public GLPaintManager() {
		
	}
	
	public void init() {
		m_paints.put(SolidPaint.class, new GLSolidPaintType());
		m_paints.put(ImagePaint.class, new GLTexturePaintType());
		m_paints.put(LinearGradientPaint.class, new GLLinearGradientPaintType());
	}
	
	public <P extends Paint> void applyPaint(GLPainter painter, P p, GLMatrix3f model, GLMatrix3f proj) {
		if (!m_paints.containsKey(p.getClass()))
			throw new RuntimeException("Cannot draw with paint: " + p);
		@SuppressWarnings("unchecked")
		GLPaintType<P> type = (GLPaintType<P>) m_paints.get(p.getClass());
		type.apply(painter, p);
		type.updateMats(painter, model.getCurrentMatrix(), proj.getCurrentMatrix());
		m_bound = true;
	}
	public <P extends Paint> void updateMats(GLPainter painter, P p, GLMatrix3f model, GLMatrix3f proj) {
		if (!m_bound) return;
		if (!m_paints.containsKey(p.getClass()))
			throw new RuntimeException("Cannot draw with paint: " + p);
		@SuppressWarnings("unchecked")
		GLPaintType<P> type = (GLPaintType<P>) m_paints.get(p.getClass());
		type.updateMats(painter, model.getCurrentMatrix(), proj.getCurrentMatrix());
	}
	public <P extends Paint> void clearPaint(GLPainter painter, P p) {
		if (!m_paints.containsKey(p.getClass()))
			throw new RuntimeException("Cannot clear paint: " + p);
		@SuppressWarnings("unchecked")
		GLPaintType<P> type = (GLPaintType<P>) m_paints.get(p.getClass());
		type.clear(painter, p);
		
		m_bound = false;
	}
	
	public <P extends Paint> void enterTextMode(GLPainter painter, P p) {
		if (!m_paints.containsKey(p.getClass()))
			throw new RuntimeException("Cannot clear paint: " + p);
		@SuppressWarnings("unchecked")
		GLPaintType<P> type = (GLPaintType<P>) m_paints.get(p.getClass());
		type.enterTextMode(painter, p);
	}
	public <P extends Paint> void exitTextMode(GLPainter painter, P p) {
		if (!m_paints.containsKey(p.getClass()))
			throw new RuntimeException("Cannot clear paint: " + p);
		@SuppressWarnings("unchecked")
		GLPaintType<P> type = (GLPaintType<P>) m_paints.get(p.getClass());
		type.exitTextMode(painter, p);
	}
	public <P extends Paint> void setTextGlyph(GLPainter painter, P p, Image2D glyph) {
		if (!m_paints.containsKey(p.getClass()))
			throw new RuntimeException("Cannot clear paint: " + p);
		@SuppressWarnings("unchecked")
		GLPaintType<P> type = (GLPaintType<P>) m_paints.get(p.getClass());
		type.setTextGlyph(painter, glyph);
	}
	
	//Represents a GL instance of a paint
	public interface GLPaintType<T extends Paint> {
		public void apply(GLPainter painter, T t);
		//Will be called after apply
		public void updateMats(GLPainter painter, Matrix3f model, Matrix3f proj);
		public void clear(GLPainter painter, T t);
		
		public void enterTextMode(GLPainter painter, T t); 
		public void setTextGlyph(GLPainter painter, Image2D image);
		public void exitTextMode(GLPainter painter, T t); 
	}
	
	//--------------------Paint Types-----------------------------------------//
	
	//--------------------Solid Paint-----------------------------------------//
	
	public static class GLSolidPaintType implements GLPaintType<SolidPaint> {
		private static final String SOLID_PAINT_PROGRAM = "Programs/solid.prog";
		private static final String SOLID_PAINT_TEXT_PROGRAM = "Programs/Text/text_solid.prog";
		
		private Program m_prog = null;
		private Program m_text = null;
		
		public void init(GL gl) {
			try {
				m_prog = ProgramXMLLoader.s_load(gl, SOLID_PAINT_PROGRAM, new ClasspathResourceLocator()).get(0);
				m_text = ProgramXMLLoader.s_load(gl, SOLID_PAINT_TEXT_PROGRAM, new ClasspathResourceLocator()).get(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		public void apply(GLPainter painter, SolidPaint p) {
			GL gl = painter.getGL();
			if (m_prog == null) init(gl);
			m_prog.bind(gl);
			m_prog.getInputs(Uniform.class, InputUsage.DIFFUSE_COLOR).setValue(gl, p.getColor().toVector4f());;
		}
		@Override
		public void updateMats(GLPainter painter, Matrix3f model, Matrix3f proj) {
			GL gl = painter.getGL();
			Program.s_getCurrent().getInputs(Uniform.class, InputUsage.MODEL_MATRIX_2D).setValue(gl, model);
			Program.s_getCurrent().getInputs(Uniform.class, InputUsage.PROJECTION_MATRIX_2D).setValue(gl, proj);
		}
		@Override
		public void clear(GLPainter painter, SolidPaint p) {
			GL gl = painter.getGL();
			m_prog.unbind(gl);
		}
		@Override
		public void enterTextMode(GLPainter painter, SolidPaint p) {
			GL gl = painter.getGL();
			if (m_text == null) init(gl);
			m_text.bind(gl);
			m_text.getInputs(Uniform.class, InputUsage.DIFFUSE_COLOR).setValue(gl, p.getColor().toVector4f());;
		}
		@Override
		public void setTextGlyph(GLPainter painter, Image2D glyph) {
			GL gl = painter.getGL();
			
			Texture2D tex = painter.getTextureManager().getTexture(gl, glyph);
			tex.bind(gl, 0);
			m_text.getInputs(Uniform.class, InputUsage.GLYPH_SAMPLER).setValue(gl, 0);;
		}
		@Override
		public void exitTextMode(GLPainter painter, SolidPaint p) {
			m_text.unbind(painter.getGL());
			apply(painter, p);
		}
	}
	
	//--------------------Image Paint-----------------------------------------//
	
	public static class GLTexturePaintType implements GLPaintType<ImagePaint> {
		private static final String TEXTURE_PAINT_PROGRAM = "Programs/textured.prog";
		private static final String TEXTURE_PAINT_TEXT_PROGRAM = "Programs/Text/text_textured.prog";
		
		private Program m_prog = null;
		private Program m_text = null;
		
		public void init(GL gl) {
			try {
				m_prog = ProgramXMLLoader.s_load(gl, TEXTURE_PAINT_PROGRAM, new ClasspathResourceLocator()).get(0);
				m_text = ProgramXMLLoader.s_load(gl, TEXTURE_PAINT_TEXT_PROGRAM, new ClasspathResourceLocator()).get(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void apply(GLPainter painter, ImagePaint p) {
			GL gl = painter.getGL();
			if (m_prog == null) init(gl);
			m_prog.bind(gl);
			
			Texture2D tex = painter.getTextureManager().getTexture(gl, p.getImage());
			tex.bind(gl, 0);
			m_prog.getInputs(Uniform.class, InputUsage.DIFFUSE_SAMPLER).setValue(gl, 0);
		}
		@Override
		public void updateMats(GLPainter painter, Matrix3f model, Matrix3f proj) {
			GL gl = painter.getGL();
			Program.s_getCurrent().getInputs(Uniform.class, InputUsage.MODEL_MATRIX_2D).setValue(gl, model);
			Program.s_getCurrent().getInputs(Uniform.class, InputUsage.PROJECTION_MATRIX_2D).setValue(gl, proj);
		}
		@Override
		public void clear(GLPainter painter, ImagePaint p) {
			GL gl = painter.getGL();
			m_prog.unbind(gl);
		}
		@Override
		public void enterTextMode(GLPainter painter, ImagePaint p) {
			GL gl = painter.getGL();
			if (m_text == null) init(gl);
			m_text.bind(gl);
			
			Texture2D tex = painter.getTextureManager().getTexture(gl, p.getImage());
			tex.bind(gl, 0);
			m_text.getInputs(Uniform.class, InputUsage.DIFFUSE_SAMPLER).setValue(gl, 0);
		}
		@Override
		public void setTextGlyph(GLPainter painter, Image2D glyph) {
			GL gl = painter.getGL();
			
			Texture2D tex = painter.getTextureManager().getTexture(gl, glyph);
			tex.bind(gl, 1);
			m_text.getInputs(Uniform.class, InputUsage.GLYPH_SAMPLER).setValue(gl, 1);;
		}
		@Override
		public void exitTextMode(GLPainter painter, ImagePaint p) {
			m_text.unbind(painter.getGL());
			apply(painter, p);
		}
	}
	//--------------------Linear Gradient Paint-----------------------------------------//
	
	public static class GLLinearGradientPaintType implements GLPaintType<LinearGradientPaint> {
		private static final String GRADIENT_PAINT_PROGRAM = "Programs/linear_gradient.prog";
		private static final String GRADIENT_PAINT_TEXT_PROGRAM = "Programs/Text/text_linear_gradient.prog";
		
		private Program m_prog = null;
		private Program m_text = null;
		
		public void init(GL gl) {
			try {
				m_prog = ProgramXMLLoader.s_load(gl, GRADIENT_PAINT_PROGRAM, new ClasspathResourceLocator()).get(0);
				m_text = ProgramXMLLoader.s_load(gl, GRADIENT_PAINT_TEXT_PROGRAM, new ClasspathResourceLocator()).get(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void apply(GLPainter painter, LinearGradientPaint p) {
			GL gl = painter.getGL();
			if (m_prog == null) init(gl);
			m_prog.bind(gl);
			
			Texture2D tex = painter.getTextureManager().getTexture(gl, p.getImage());
			tex.bind(gl, 0);
			
			m_prog.getInputs(Uniform.class, new InputUsage("GRADIENT_SAMPLER", DataType.SAMPLER2D, Uniform.class)).setValue(gl, 0);
		}
		@Override
		public void updateMats(GLPainter painter, Matrix3f model, Matrix3f proj) {
			GL gl = painter.getGL();
			Program.s_getCurrent().getInputs(Uniform.class, InputUsage.MODEL_MATRIX_2D).setValue(gl, model);
			Program.s_getCurrent().getInputs(Uniform.class, InputUsage.PROJECTION_MATRIX_2D).setValue(gl, proj);
		}
		@Override
		public void clear(GLPainter painter, LinearGradientPaint p) {
			GL gl = painter.getGL();
			m_prog.unbind(gl);
		}
		@Override
		public void enterTextMode(GLPainter painter, LinearGradientPaint p) {
			GL gl = painter.getGL();
			if (m_text == null) init(gl);
			m_text.bind(gl);
			
			Texture2D tex = painter.getTextureManager().getTexture(gl, p.getImage());
			tex.bind(gl, 0);
			
			m_text.getInputs(Uniform.class, new InputUsage("GRADIENT_SAMPLER", DataType.SAMPLER2D, Uniform.class)).setValue(gl, 0);
		}
		@Override
		public void setTextGlyph(GLPainter painter, Image2D glyph) {
			GL gl = painter.getGL();
			
			Texture2D tex = painter.getTextureManager().getTexture(gl, glyph);
			tex.bind(gl, 1);
			m_text.getInputs(Uniform.class, InputUsage.GLYPH_SAMPLER).setValue(gl, 1);;
		}
		@Override
		public void exitTextMode(GLPainter painter, LinearGradientPaint p) {
			m_text.unbind(painter.getGL());
			apply(painter, p);
		}
	}
}
