package dev.menace.gui.hud.element.elements;

import java.awt.Color;

import dev.menace.Menace;
import dev.menace.gui.hud.ScreenPosition;
import dev.menace.gui.hud.element.ElementDraggable;
import dev.menace.module.Module;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

public class ArrayList extends ElementDraggable {

	private String width;
	
	@Override
	public int getWidth() {
		return font.getStringWidth("ArrayList");
	}

	@Override
	public int getHeight() {
		return font.FONT_HEIGHT;
	}

	@Override
	public void render(ScreenPosition pos) {
		java.util.ArrayList<Module> enabledModules = Menace.instance.moduleManager.getActiveModules();
		enabledModules.sort((m1, m2) -> font.getStringWidth(m2.getDisplayName()) - font.getStringWidth(m1.getDisplayName()));
		
		int y = pos.getAbsoluteY();
		
		for (Module m : enabledModules) {
			if (!m.isVisible()) {
				continue;
			}
			if (Menace.instance.moduleManager.hudModule.arrayAlign.getString().equalsIgnoreCase("Left")) {
				//Align left
				font.drawString(m.getDisplayName(), pos.getAbsoluteX(), y, new Color(250, 0, 0).getRGB());
			} else {
				//Align right
				font.drawString(m.getDisplayName(), pos.getAbsoluteX() - font.getStringWidth(m.getDisplayName()) + font.getStringWidth("ArrayList"), y, new Color(250, 0, 0).getRGB());
			}
			
			y += 10;
		}
	}
	
	@Override
	public void renderDummy(ScreenPosition pos) {
		font.drawString("ArrayList", pos.getAbsoluteX(), pos.getAbsoluteY(), new Color(250, 0, 0).getRGB());
	}
	
	@Override
	protected double defaultX() {
		return 0.8243559718969555;
	}
	
	@Override
	protected double defaultY() {
		return 0.008333333333333333;
	}
}
