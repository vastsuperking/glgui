package glgui.render.pipeline;

public abstract class PWindowProvider {
	private static PWindowProvider s_default;
	
	public abstract PWindow create();
	
	public static PWindowProvider getDefaultProvider() {
		return s_default;
	}
	public static void setDefaultProvider(PWindowProvider provider) {
		s_default = provider;
	}
}
