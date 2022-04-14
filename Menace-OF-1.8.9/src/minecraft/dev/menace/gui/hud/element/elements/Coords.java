package dev.menace.gui.hud.element.elements;

import java.awt.Color;

import dev.menace.gui.hud.ScreenPosition;
import dev.menace.gui.hud.element.ElementDraggable;

public class Coords extends ElementDraggable {

	@Override
	public int getWidth() {
		return font.getStringWidth("X: 100") + 1;
	}

	@Override
	public int getHeight() {
		return (font.FONT_HEIGHT * 3) + 1;
	}

	@Override
	public void render(ScreenPosition pos) {
		font.drawString(String.format("X: %.1f", MC.thePlayer.posX), pos.getAbsoluteX(), pos.getAbsoluteY(), new Color(250, 0, 0).getRGB());
		font.drawString(String.format("Y: %.1f", MC.thePlayer.posY - 1), pos.getAbsoluteX(), pos.getAbsoluteY() + 10, new Color(250, 0, 0).getRGB());
		font.drawString(String.format("Z: %.1f", MC.thePlayer.posZ), pos.getAbsoluteX(), pos.getAbsoluteY() + 20, new Color(250, 0, 0).getRGB());
	}

}
