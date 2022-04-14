package dev.menace.gui.hud.element.elements;

import dev.menace.gui.hud.ScreenPosition;
import dev.menace.gui.hud.element.ElementDraggable;
import dev.menace.module.Category;
import dev.menace.utils.render.RenderUtils;
import java.awt.Color;
import dev.menace.module.Module;

public class TabGui extends ElementDraggable {

	public static int selected = 0;
	public static int selectedM = 0;
	public static boolean dropDown = false;
	public static Category selectedCategory;
	public static Module selectedModule;
	
	@Override
	public int getWidth() {
		return font.getStringWidth("movement  >") + 1;
	}

	@Override
	public int getHeight() {
		return 70;
	}

	@Override
	public void render(ScreenPosition pos) {
		int render = 0;
		int i = 0;
		for (Category c : Category.values()) {
			int color = Color.BLACK.getRGB();
			if (render == selected) {color = Color.RED.getRGB(); selectedCategory = c;}
			RenderUtils.drawRect(pos.getAbsoluteX(), pos.getAbsoluteY() + i, pos.getAbsoluteX() + font.getStringWidth("movement  >") + 1, pos.getAbsoluteY() + i + 10, color);
			font.drawString(c.toString().toLowerCase(), pos.getAbsoluteX(), pos.getAbsoluteY() + i, -1);
			font.drawString(">", pos.getAbsoluteX() + font.getStringWidth("movement  "), pos.getAbsoluteY() + i, -1);
			i += 10;
			render++;
		}
		
		int k = 0;
		int renderr = 0;
		if (dropDown) {
			for (Module m : Menace.moduleManager.modules) {
				if (m.getCategory() == selectedCategory) {
					int color = Color.BLACK.getRGB();
					if (renderr == selectedM) {color = Color.RED.getRGB(); selectedModule = m;}
					String name = m.isToggled() ? m.getName() + " X" : m.getName();
					RenderUtils.drawRect(pos.getAbsoluteX() + font.getStringWidth("movement  >") + 3,
							pos.getAbsoluteY() + k, pos.getAbsoluteX() + font.getStringWidth("movement  >") + font.getStringWidth(name) + 3, pos.getAbsoluteY() + k + 10, color);
					font.drawString(name, pos.getAbsoluteX() + font.getStringWidth("movement  >") + 3, pos.getAbsoluteY() + k, -1);
					k+=10;
					renderr++;
				}
			}
		}
	}

}
