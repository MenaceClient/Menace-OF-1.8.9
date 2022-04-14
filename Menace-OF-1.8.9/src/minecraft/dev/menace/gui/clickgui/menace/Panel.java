package dev.menace.gui.clickgui.menace;

import java.awt.Color;

import dev.menace.module.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class Panel {

	private Category c;
	private int x, y;
	private int width, height;
	private Minecraft MC = Minecraft.getMinecraft();
	int posX, posY, prevX, prevY;
	
	public Panel(Category c, int x, int y, int width, int height) {
		this.c = c;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void render(GuiScreen screen) {
		
		screen.drawRect(this.x, this.y, this.width, this.height, Color.BLACK.getRGB());
		screen.drawString(MC.fontRendererObj, c.name(), this.x + 1, this.y, -1);
		
	}
	
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if (clickedMouseButton == 0) {
			posX = mouseX - prevX;
			posY = mouseY - prevY;
		}
		
		this.prevX = mouseX;
		this.prevY = mouseY;
	}
	
}
