package glgui.render.pipeline.gl;

import glcommon.Color;
import glcommon.font.Font;
import glcommon.font.Font.Glyph;
import glcommon.image.Image2D;
import glcommon.util.ResourceLocator.ClasspathResourceLocator;
import glcommon.vector.Matrix3f;
import glcommon.vector.MatrixFactory;
import glcommon.vector.Vector2f;
import glextra.material.GlobalParamBindingSet;
import glextra.material.Material;
import glextra.material.MaterialXMLLoader;
import glextra.renderer.GLTextureManager;
import glgui.painter.Painter;
import gltools.Mode;
import gltools.buffer.AttribArray;
import gltools.buffer.ColorBuffer;
import gltools.buffer.Geometry;
import gltools.buffer.IndexBuffer;
import gltools.buffer.VertexBuffer;
import gltools.gl.GL;
import gltools.gl.GL1;
import gltools.shader.InputUsage;
import gltools.shader.Program.ProgramLinkException;
import gltools.shader.Shader.ShaderCompileException;
import gltools.texture.Texture2D;
import gltools.util.GLMatrix3f;

import java.io.IOException;

public class GLPainter implements Painter {
	private static final String MATERIAL = "Materials/M2D/gui.mat";
	
	private GL m_gl;

	public GLMatrix3f m_modelMat;
	public GLMatrix3f m_projMat;
	
	private VertexBuffer m_verticesBuf;
	private VertexBuffer m_texCoordsBuf;
	private IndexBuffer m_indicesBuf;
	
	private GlobalParamBindingSet m_globals = new GlobalParamBindingSet();
	
	private Color m_color = Color.BLACK;
	private Font m_font = null;
	
	private GLTextureManager m_textureManager;
	
	private Material m_material;
	
	//Make another instance of material so we
	//are not always switching between
	//non-textured and textured program
	//and don't have to recompile every time we switch
	private Material m_materialTextured;
	
	public GLPainter(GL gl) {
		m_gl = gl;
		
		m_textureManager = new GLTextureManager();
		
		m_modelMat = new GLMatrix3f();
		m_projMat = new GLMatrix3f();
		
		m_verticesBuf = new VertexBuffer();
		m_texCoordsBuf = new VertexBuffer();
		m_indicesBuf = new IndexBuffer();
		
		m_verticesBuf.init(m_gl.getGL1());
		m_texCoordsBuf.init(m_gl.getGL1());
		m_indicesBuf.init(m_gl.getGL1());
		
		m_globals.addParam(InputUsage.MODEL_MATRIX_2D, m_modelMat);
		m_globals.addParam(InputUsage.PROJECTION_MATRIX_2D, m_projMat);
		
		try {
			m_material = MaterialXMLLoader.s_load(gl, MATERIAL, new ClasspathResourceLocator()).get(0);
			m_materialTextured = MaterialXMLLoader.s_load(gl, MATERIAL, new ClasspathResourceLocator()).get(0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ShaderCompileException e) {
			e.printStackTrace();
		} catch (ProgramLinkException e) {
			e.printStackTrace();
		}
		m_material.selectTechnique();
		m_materialTextured.selectTechnique();
	}
	
	//Functions for GLPipeline
	
	public void start() {
		ColorBuffer.getInstance().clear(m_gl);
		m_modelMat.setIdentity();

		m_gl.glEnable(GL1.GL_BLEND);
		m_gl.getGL1().glBlendFunc(GL1.GL_SRC_ALPHA,	GL1.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public void stop() {
		m_gl.glDisable(GL1.GL_BLEND);
	}
	
	public void updateProjection(float width, float height) {
		m_projMat.setCurrentMatrix(
				MatrixFactory.createAffineProjectionMatrix(0, width, height, 0));
	}

	public GL getGL() { return m_gl; }
	
	//Functions for Painter
	@Override
	public void setColor(Color color) {
		m_material.setColor("color", color);
		m_materialTextured.setColor("color", color);
		m_color = color;
	}
	@Override
	public Color getColor() { return m_color; }
	
	@Override
	public void setFont(Font font) {
		m_font = font;
	}
	@Override
	public Font getFont() {
		return m_font;
	}
	
	@Override
	public Matrix3f getTransform() {
		return m_modelMat.getCurrentMatrix();
	}
	@Override
	public void setTransform(Matrix3f mat) {
		m_modelMat.setCurrentMatrix(mat);
	}
	
	@Override
	public void pushTransform() {
		m_modelMat.push();
	}
	
	@Override
	public void popTransform() {
		m_modelMat.pop();
	}
	
	@Override
	public void translate(float x, float y) {
		m_modelMat.getCurrentMatrix().mul(
				MatrixFactory.createAffineTranslationMatrix(new Vector2f(x, y)));
	}
	@Override
	public void rotate(float theta) {
		m_modelMat.getCurrentMatrix().mul(
				MatrixFactory.createAffineRotationMatrix(theta));		
	}
	@Override
	public void scale(float x, float y) {
		m_modelMat.getCurrentMatrix().mul(
				MatrixFactory.createAffineScaleMatrix(new Vector2f(x, y)));
	}
	
	@Override
	public void drawLine(float x1, float y1, float x2, float y2) {
		m_material.bind(m_gl, m_globals);

		float[] vertices = {x1, y1,
				x2, x2};


		m_verticesBuf.bind(m_gl);
		m_verticesBuf.setValues(m_gl, vertices);
		m_verticesBuf.unbind(m_gl);

		Geometry geo = new Geometry();
		geo.addArray(new AttribArray(m_verticesBuf, InputUsage.VERTEX_POSITION_2D, 0, 0));
		geo.setVertexCount(2);
		geo.setMode(Mode.LINES);
		geo.render(m_gl);
		
		m_material.unbind(m_gl);
	}

	@Override
	public void drawPolygon(float[] x, float[] y) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void drawRect(float x, float y, float width, float height) {
		m_material.bind(m_gl, m_globals);
		
		//For bottom left origin
		float vertices[] = {x + width, y + height,
							 x, y + height,
							 x, y,
							 x + width, y };
		int indices[] = {0, 1, 1, 2, 2, 3, 3, 0};
		
		m_verticesBuf.bind(m_gl.getGL1());
		m_verticesBuf.setValues(m_gl.getGL1(), vertices);
		m_verticesBuf.unbind(m_gl.getGL1());
				
		m_indicesBuf.bind(m_gl.getGL1());
		m_indicesBuf.setValues(m_gl.getGL1(), indices);
		m_indicesBuf.unbind(m_gl.getGL1());
		
		Geometry geo = new Geometry();
		geo.addArray(new AttribArray(m_verticesBuf, InputUsage.VERTEX_POSITION_2D, 0, 0));
		geo.setVertexCount(indices.length);
		geo.setMode(Mode.LINES);
		geo.setIndexBuffer(m_indicesBuf);
		
		geo.render(m_gl.getGL2());
		
		m_material.unbind(m_gl);
	}

	@Override
	public void fillRect(float x, float y, float width, float height) {
		m_material.bind(m_gl, m_globals);

		fillRectNoMaterial(x, y, width, height);
		
		m_material.unbind(m_gl);
	}

	@Override
	public void fillPolygon(float[] x, float[] y) {
		throw new UnsupportedOperationException();
	}	
	
	@Override
	public void drawImage(Image2D i, float x, float y, float width, float height) {
		drawImage(i, x, y, width, height, 1, 1);
	}
	
	@Override
	public void drawImage(Image2D i, float x, float y, float width, 
							float height, float rx, float ry) {
		Texture2D tex = m_textureManager.getTexture(m_gl, i);
		m_materialTextured.setTexture2D("texture", tex);
		m_materialTextured.bind(m_gl, m_globals);
		
		float vertices[] = {x + width, y + height,
				 x, y + height,
				 x, y,
				 x + width, y };

		float texCoords[] = {rx, ry,
				0.0f, ry,
				0.0f, 0.0f,
				rx, 0 };
		int indices[] = {0, 1, 2, 0, 2, 3};

		m_verticesBuf.bind(m_gl.getGL1());
		m_verticesBuf.setValues(m_gl.getGL1(), vertices);
		m_verticesBuf.unbind(m_gl.getGL1());

		m_texCoordsBuf.bind(m_gl.getGL1());
		m_texCoordsBuf.setValues(m_gl.getGL1(), texCoords);
		m_texCoordsBuf.unbind(m_gl.getGL1());

		m_indicesBuf.bind(m_gl.getGL1());
		m_indicesBuf.setValues(m_gl.getGL1(), indices);
		m_indicesBuf.unbind(m_gl.getGL1());

		Geometry geo = new Geometry();
		geo.addArray(new AttribArray(m_verticesBuf, InputUsage.VERTEX_POSITION_2D, 0, 0));
		geo.addArray(new AttribArray(m_texCoordsBuf, InputUsage.VERTEX_TEX_COORD, 0, 0));
		geo.setVertexCount(indices.length);
		geo.setMode(Mode.TRIANGLES);
		geo.setIndexBuffer(m_indicesBuf);

		geo.render(m_gl.getGL2());

		
		m_materialTextured.unbind(m_gl);
	}
	
	@Override
	public void drawString(String string, float x, float y, float scale) {
		//The font we will be using
		Font font = getFont();
		if (font == null) throw new RuntimeException("No font set!");
		if (string == null || string.equals("")) return;
		
		//First, push the transformation matrix
		pushTransform();
		translate(x, y);
		
		char[] chars = string.toCharArray();
		for (char c : chars) {
			if (c == '\n' || c == '\t') continue;
			Glyph g = font.getGlyph(c);
			
			//Translate across the xoffset and down by the yoffset
			Image2D image = g.getImage();
			//Material is already set...
			drawImage(image, scale * g.getXOff(), scale * (g.getYOff()), 
						scale * image.getWidth(), 
						scale * image.getHeight());
			translate(g.getXAdvance() * scale, 0);
		}
		
		//Pop it to get back to what we had before
		popTransform();
	}
	
	@Override
	public void dispose() {}
	
	private void fillRectNoMaterial(float x, float y, float width, float height) {
		float vertices[] = {x + width, y + height,
				x, y + height,
				x, y,
				x + width, y };
		int indices[] = {0, 1, 2, 0, 2, 3};

		m_verticesBuf.bind(m_gl);
		m_verticesBuf.setValues(m_gl, vertices);
		m_verticesBuf.unbind(m_gl);

		m_indicesBuf.bind(m_gl.getGL1());
		m_indicesBuf.setValues(m_gl.getGL1(), indices);
		m_indicesBuf.unbind(m_gl.getGL1());

		Geometry geo = new Geometry();
		geo.addArray(new AttribArray(m_verticesBuf, InputUsage.VERTEX_POSITION_2D, 0, 0));
		geo.setVertexCount(indices.length);
		geo.setMode(Mode.TRIANGLES);
		geo.setIndexBuffer(m_indicesBuf);
		geo.render(m_gl);
	}
}
