package dev.menace.gui.clickgui.menace;

import dev.menace.module.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

public class MenaceClickGui extends GuiScreen {
	
	ArrayList<Panel> panels = new ArrayList<Panel>();
	
	Minecraft MC = Minecraft.getMinecraft();
	
	public MenaceClickGui() {
		int i = 2;
		for (Category c : Category.values()) {
			Panel p = new Panel(c, 2, i, MC.fontRendererObj.getStringWidth(c.name()) + 3, i + 10);
			panels.add(p);
			i+=11;
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawDefaultBackground();
		
		for (Panel p : panels) {
			p.render(this);
		}
	}
	
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		for (Panel p : panels) {
			p.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		}
	}
	
}
