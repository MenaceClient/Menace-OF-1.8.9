package dev.menace.gui.hud.element.elements;

import java.awt.Color;

import dev.menace.gui.hud.ScreenPosition;
import dev.menace.gui.hud.element.ElementDraggable;
import dev.menace.utils.entity.self.PlayerUtils;

public class BPS extends ElementDraggable {

	@Override
	public int getWidth() {
		return font.getStringWidth("B/S: " + Math.round(PlayerUtils.bps));
	}

	@Override
	public int getHeight() {
		return font.FONT_HEIGHT;
	}

	@Override
	public void render(ScreenPosition pos) {
		font.drawString(String.format("B/S: %.1f", PlayerUtils.bps), pos.getAbsoluteX(), pos.getAbsoluteY(), new Color(250, 0, 0).getRGB());
	}
	
	@Override
	public void renderDummy(ScreenPosition pos) {
		font.drawString(String.format("B/S: %.1f", PlayerUtils.bps), pos.getAbsoluteX(), pos.getAbsoluteY(), new Color(250, 0, 0).getRGB());
	}

}
