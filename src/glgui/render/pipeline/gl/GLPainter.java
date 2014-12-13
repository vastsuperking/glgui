package glgui.render.pipeline.gl;

import glcommon.BufferUtils;
import glcommon.font.Font;
import glcommon.font.Font.Glyph;
import glcommon.image.Image2D;
import glcommon.vector.Matrix3f;
import glcommon.vector.MatrixFactory;
import glcommon.vector.MatrixUtils;
import glcommon.vector.Vector2f;
import glextra.material.GlobalParamBindingSet;
import glextra.renderer.GLTextureManager;
import glgui.painter.Paint;
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
import gltools.util.GLMatrix3f;

import java.nio.FloatBuffer;

public class GLPainter implements Painter {	
	private GL m_gl;

	public GLMatrix3f m_modelMat;
	public GLMatrix3f m_projMat;
	
	private VertexBuffer m_verticesBuf;
	private VertexBuffer m_texCoordsBuf;
	private IndexBuffer m_indicesBuf;
	
	private GlobalParamBindingSet m_globals = new GlobalParamBindingSet();

	private Paint m_paint = null;
	
	private GLTextureManager m_textureManager;
	private GLPaintManager m_paintManager;
	
	public GLPainter(GL gl) {
		m_gl = gl;
		
		m_textureManager = new GLTextureManager();
		m_paintManager = new GLPaintManager();
		
		m_paintManager.init();
		
		m_modelMat = new GLMatrix3f(InputUsage.MODEL_MATRIX_2D);
		m_projMat = new GLMatrix3f(InputUsage.PROJECTION_MATRIX_2D);
		
		m_verticesBuf = new VertexBuffer();
		m_texCoordsBuf = new VertexBuffer();
		m_indicesBuf = new IndexBuffer();
		
		m_verticesBuf.init(m_gl.getGL1());
		m_texCoordsBuf.init(m_gl.getGL1());
		m_indicesBuf.init(m_gl.getGL1());
		
		m_globals.addParam(InputUsage.MODEL_MATRIX_2D, m_modelMat);
		m_globals.addParam(InputUsage.PROJECTION_MATRIX_2D, m_projMat);
	}
	
	public GL getGL() { return m_gl; }

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
	
	//
	
	public GLTextureManager getTextureManager() { return m_textureManager; }
	public GLPaintManager getPaintManager() { return m_paintManager; }
	
	//

	public void setPaint(Paint paint) {
		if (m_paint != null)
			m_paintManager.clearPaint(this, m_paint);
		m_paint = paint;
		m_paintManager.applyPaint(this, m_paint, m_modelMat, m_projMat);
	}
	public Paint getPaint() { return m_paint; }
	

	public Vector2f getTranslation() {
		return MatrixUtils.getTranslation(m_modelMat.getCurrentMatrix());
	}
	public Vector2f getScale() {
		return MatrixUtils.getScale(m_modelMat.getCurrentMatrix());
	}
	public float getRotation() {
		return MatrixUtils.getRotation(m_modelMat.getCurrentMatrix());
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
		m_paintManager.updateMats(this, m_paint, m_modelMat, m_projMat);
		
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
		
	}

	@Override
	public void drawPolygon(float[] x, float[] y) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void drawRect(float x, float y, float width, float height) {
		m_paintManager.updateMats(this, m_paint, m_modelMat, m_projMat);
		
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
	}
	
	@Override
	public void fillRect(float x, float y, float width, float height) {
		fillRect(x, y, width, height, 0, 0, 1, 1);
	}

	@Override
	public void fillRect(float x, float y, float width, float height,
						 float tx, float ty, float tw, float th) {
		m_paintManager.updateMats(this, m_paint, m_modelMat, m_projMat);
		
		float vertices[] = {x + width, y + height,
				x, y + height,
				x, y,
				x + width, y };
		
		float texCoords[] = {
				tx + tw, ty + th,
				tx, ty + th,
				tx, ty,
				tx + tw, ty};
		int indices[] = {0, 1, 2, 0, 2, 3};

		m_verticesBuf.bind(m_gl);
		m_verticesBuf.setValues(m_gl, vertices);
		m_verticesBuf.unbind(m_gl);

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
		geo.render(m_gl);
	}
	@Override
	public void fillRoundedRect(float x, float y, float width, float height,
								float radius, int resolution) {
		fillRoundedRect(x, y, width, height, radius, resolution, 0, 0, 1, 1);
	}
	@Override
	public void fillRoundedRect(float x, float y, float width, float height, 
								float radius, int resolution,
								float tx, float ty, float tw, float th) {
		float radiusTw = tw * radius / width;
		float radiusTh = tw * radius / width;
		//Fill bottom left corner
		fillArc(x + radius, y + radius, radius, radius, 
				(float) (Math.PI), (float) (0.5 * Math.PI), resolution,
				tx + radiusTw, ty + radiusTh, radiusTw, radiusTh);
		//Fill bottom right corner
		fillArc(x + width - radius, y + radius, radius, radius,
				(float) (0.5 * Math.PI), (float) (0.5 * Math.PI), resolution,
				tx + tw - radiusTw, ty + radiusTh, 
				radiusTw, radiusTh);
		
		//Fill top left
		fillArc(x + radius, y + height - radius, radius, radius,
				0, (float) (-0.5 * Math.PI), resolution,
				tx + radiusTw, ty + th - radiusTh,
				radiusTw, radiusTh);
		//Fill top right
		fillArc(x + width - radius, y + height - radius, radius, radius,
				0, (float) (0.5 * Math.PI), resolution,
				tx + tw - radiusTw, ty + th - radiusTh,
				radiusTw, radiusTh);
		
		//Draw rect inbetween bottom left and bottom right
		fillRect(x + radius, y, width - 2 * radius, radius,
				 tx + radiusTw, ty, 
				 tw - 2 * radiusTw, radiusTh);
		
		//Draw rect inbetween top left and top right
		fillRect(x + radius, y + height - radius, width - 2 * radius, radius,
				 tx + radiusTw, ty + th - radiusTh, 
				 tw - 2 * radiusTw, radiusTh);
		
		//Draw rect spanning the width of the middle
		fillRect(x, y + radius, width, height - 2  * radius,
				 tx, ty + radiusTh, tw, th - 2 * radiusTh);
		
	}
	@Override
	public void fillArc(float x, float y, float radius, float smRadius, float startRads, float rads) {
		fillArc(x, y, radius, smRadius, startRads, rads, (int) ( getScale().scale(new Vector2f(radius, smRadius)).length() / 10 ));
	}
	public void fillArc(float x, float y, float radius, float smRadius, 
			float startRads, float rads, int resolution) {
		fillArc(x, y, radius, smRadius, startRads, rads, resolution, 0, 0, 1, 1);
	}
	@Override
	public void fillArc(float x, float y, float radius, float smRadius, 
						float startRads, float rads, int resolution,
						float tx, float ty, float tw, float th) {
		if (resolution < 1) resolution = 1;
		
		m_paintManager.updateMats(this, m_paint, m_modelMat, m_projMat);
		// + 2 for starting and ending points
		FloatBuffer verts = BufferUtils.createFloatBuffer((resolution + 2) * 2);
		FloatBuffer texCoords = BufferUtils.createFloatBuffer((resolution + 2) * 2);
		//Put origin coord
		verts.put(x).put(y);
		texCoords.put(tx).put(ty);
		
		//Divide the equivalent of 90 deg by number of steps
		float step = rads / (float) resolution;
		//Current angle to eval the curve for, in radians
		float angle = startRads;
		
		for (int i = 0; i < resolution + 1; i++) {
			float s = (float) Math.sin(angle);
			float c = (float) Math.cos(angle);
			
			float vX = x + s * smRadius;
			float vY = y + c * radius;
			
			float tcX = tx + s * tw;
			float tcY = ty + c * th;
			verts.put(vX).put(vY);
			texCoords.put(tcX).put(tcY);
			angle += step;
		}
		verts.flip();
		texCoords.flip();
		
		m_verticesBuf.bind(m_gl);
		m_verticesBuf.bufferData(m_gl, verts);
		m_verticesBuf.unbind(m_gl);

		m_texCoordsBuf.bind(m_gl.getGL1());
		m_texCoordsBuf.bufferData(m_gl.getGL1(), texCoords);
		m_texCoordsBuf.unbind(m_gl.getGL1());
		
		Geometry geo = new Geometry();
		geo.addArray(new AttribArray(m_verticesBuf, InputUsage.VERTEX_POSITION_2D, 0, 0));
		geo.addArray(new AttribArray(m_texCoordsBuf, InputUsage.VERTEX_TEX_COORD, 0, 0));
		geo.setMode(Mode.TRIANGLE_FAN);
		geo.setVertexCount(resolution + 2);
		geo.render(m_gl);
	}
	public void fillElipse(float x, float y, float radius, float smRadius) {
		fillArc(x, y, radius, smRadius, 0, (float) (2 * Math.PI));
	}
	public void fillElipse(float x, float y, float radius, float smRadius, int resolution) {
		fillArc(x, y, radius, smRadius, 0, (float) (2 * Math.PI), resolution);
	}
	public void fillElipse(float x, float y, float radius, float smRadius, int resolution,
						   float tx, float ty, float th, float tw) {
		fillArc(x, y, radius, smRadius, 0, (float) (2 * Math.PI), resolution, tx, ty, th, tw);
	}
	
	@Override
	public void fillPolygon(float[] x, float[] y) {
		throw new UnsupportedOperationException();
	}
	//pos and texCoords should be flipped!!
	public void fillPolygon(FloatBuffer pos, FloatBuffer texCoords) {
		m_paintManager.updateMats(this, m_paint, m_modelMat, m_projMat);
		
		m_verticesBuf.bind(m_gl);
		m_verticesBuf.bufferData(m_gl, pos);
		m_verticesBuf.unbind(m_gl);

		m_texCoordsBuf.bind(m_gl.getGL1());
		m_texCoordsBuf.bufferData(m_gl.getGL1(), texCoords);
		m_texCoordsBuf.unbind(m_gl.getGL1());
		
		Geometry geo = new Geometry();
		geo.addArray(new AttribArray(m_verticesBuf, InputUsage.VERTEX_POSITION_2D, 0, 0));
		geo.addArray(new AttribArray(m_texCoordsBuf, InputUsage.VERTEX_TEX_COORD, 0, 0));
		geo.setMode(Mode.TRIANGLES);
		geo.render(m_gl);
	}	
	/*@Override
	public void fillGradient(Gradient g, float x, float y, float width, float height) {
		Color a = g.getColorA();
		Color b = g.getColorB();
		
		m_gradient.bind(m_gl);
		
		m_projMat.load(m_gl);
		m_modelMat.load(m_gl);
		
		float vertices[] = {x + width, y + height,
				x, y + height,
				x, y,
				x + width, y };
		float colors[] = {  
				a.getRed(), a.getGreen(), a.getBlue(), a.getAlpha(),
				a.getRed(), a.getGreen(), a.getBlue(), a.getAlpha(),
				b.getRed(), b.getGreen(), b.getBlue(), b.getAlpha(),
				b.getRed(), b.getGreen(), b.getBlue(), b.getAlpha() };
		
		int indices[] = {0, 1, 2, 0, 2, 3};

		m_verticesBuf.bind(m_gl);
		m_verticesBuf.setValues(m_gl, vertices);
		m_verticesBuf.unbind(m_gl);

		m_indicesBuf.bind(m_gl.getGL1());
		m_indicesBuf.setValues(m_gl.getGL1(), indices);
		m_indicesBuf.unbind(m_gl.getGL1());
		
		VertexBuffer colorBuffer = new VertexBuffer();
		colorBuffer.init(m_gl);
		colorBuffer.bind(m_gl);
		colorBuffer.setValues(m_gl, colors);
		colorBuffer.unbind(m_gl);

		Geometry geo = new Geometry();
		geo.addArray(new AttribArray(m_verticesBuf, InputUsage.VERTEX_POSITION_2D, 0, 0));
		geo.addArray(new AttribArray(colorBuffer, InputUsage.VERTEX_COLOR, 0, 0));
		geo.setVertexCount(indices.length);
		geo.setMode(Mode.TRIANGLES);
		geo.setIndexBuffer(m_indicesBuf);
		geo.render(m_gl);
		
		colorBuffer.delete(m_gl);
		
		m_gradient.unbind(m_gl);
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
	}*/
	
	@Override
	public void drawString(String string, Font font, float x, float y, float scale) {
		//The font we will be using
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
			//TODO: Reimplement
			/*drawImage(image, scale * g.getXOff(), scale * (g.getYOff()), 
						scale * image.getWidth(), 
						scale * image.getHeight());*/
			translate(g.getXAdvance() * scale, 0);
		}
		
		//Pop it to get back to what we had before
		popTransform();
	}
	
	@Override
	public void dispose() {}
}
