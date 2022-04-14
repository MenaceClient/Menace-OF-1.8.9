package dev.menace.gui.hud.element.elements;

import java.awt.Color;

import dev.menace.gui.hud.ScreenPosition;
import dev.menace.gui.hud.element.ElementDraggable;

public class FPS extends ElementDraggable {
	
	@Override
	public int getWidth() {
		return font.getStringWidth("FPS: " + MC.getDebugFPS());
	}

	@Override
	public int getHeight() {
		return font.FONT_HEIGHT;
	}

	@Override
	public void render(ScreenPosition pos) {
		font.drawString("FPS: " + MC.getDebugFPS(), pos.getAbsoluteX(), pos.getAbsoluteY(), new Color(250, 0, 0).getRGB());
	}
	
	@Override
	public void renderDummy(ScreenPosition pos) {
		font.drawString("FPS: " + MC.getDebugFPS(), pos.getAbsoluteX(), pos.getAbsoluteY(), new Color(250, 0, 0).getRGB());
	}
	
	@Override
	protected double defaultX() {
		return 0.00468384074941452;
	}
	
	@Override
	protected double defaultY() {
		return 0.04583333333333333;
	}
}
