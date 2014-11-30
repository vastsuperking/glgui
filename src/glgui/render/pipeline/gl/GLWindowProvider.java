package glgui.render.pipeline.gl;

import glgui.render.pipeline.PWindow;
import glgui.render.pipeline.PWindowProvider;

public class GLWindowProvider extends PWindowProvider {
	public static void s_use() {
		PWindowProvider.setDefaultProvider(new GLWindowProvider());
	}
	
	
	@Override
	public PWindow create() {
		return new GLWindow();
	}
}
