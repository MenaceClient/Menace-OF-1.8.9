package dev.menace.gui.hud;

public interface IRenderer extends IRendererConfig {

	int getWidth();
	int getHeight();
	
	void render(ScreenPosition pos);
	
	default void renderDummy(ScreenPosition pos) {
		render(pos);
	}
	
	public default boolean isEnabled() {
		return true;
	}
	
}
