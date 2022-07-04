package dev.menace.ui.hud;

import java.awt.Color;

import dev.menace.Menace;
import dev.menace.module.modules.render.HUDModule;
import dev.menace.utils.render.MenaceFontRenderer;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public abstract class BaseElement {

	protected Minecraft MC = Minecraft.getMinecraft();
	private double posX, posY;
	private boolean visible;
	public static String font = "Arial";
	public static MenaceFontRenderer fr = MenaceFontRenderer.getFontOnPC(font, 20);
	
	public BaseElement(int posX, int posY, boolean visible) {
		this.setAbsolute(posX, posY);
		this.visible = visible;
	}
	
	public BaseElement(double posX, double posY, boolean visible) {
		this.setRelative(posX, posY);
		this.visible = visible;
	}
	
	public abstract void render();
	public abstract void renderDummy();
	public abstract int getWidth();
	public abstract int getHeight();

	public int getAbsoluteX() {
		ScaledResolution sr = new ScaledResolution(MC);
		return (int) (posX * sr.getScaledWidth());
	}
	
	public int getAbsoluteY() {
		ScaledResolution sr = new ScaledResolution(MC);
		return (int) (posY * sr.getScaledHeight());
	}
	
	public double getRelativeX() {
		return posX;
	}
	
	public double getRelativeY() {
		return posY;
	}
	
	public void setAbsolute(int x, int y) {
		ScaledResolution sr = new ScaledResolution(MC);
		
		this.posX = (double) x / sr.getScaledWidth();
		this.posY = (double) y / sr.getScaledHeight();
	}

	public void setRelative(double x, double y) {
		this.posX = x;
		this.posY = y;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	protected void drawString(String string, int x, int y) {
		int color = Color.WHITE.getRGB();
		HUDModule hudModule = Menace.instance.moduleManager.hudModule;
		switch (hudModule.color.getValue()) {
		
		case "Fade" :
			color = RenderUtils.fade(hudModule.rainbowSpeed.getValueF(), -y).getRGB();
			break;
		
		case "Custom" :
			color = new Color(hudModule.red.getValueI(), hudModule.green.getValueI(), hudModule.blue.getValueI(), hudModule.alpha.getValueI()).getRGB();
			break;
		
		default : 
			color = Color.WHITE.getRGB();
			break;
		}
		
		if (Menace.instance.moduleManager.hudModule.customFont.getValue()) {
			fr.drawString(string, x, y, color);
		} else {
			MC.fontRendererObj.drawString(string, x, y, color);
		}
	}

	protected void drawString(String string, int x, int y, int color) {
		if (Menace.instance.moduleManager.hudModule.customFont.getValue()) {
			fr.drawString(string, x, y, color);
		} else {
			MC.fontRendererObj.drawString(string, x, y, color);
		}
	}
	
	protected int getStringWidth(String string) {
		if (Menace.instance.moduleManager.hudModule.customFont.getValue()) {
			return fr.getStringWidth(string);
		} else {
			return MC.fontRendererObj.getStringWidth(string);
		}
	}
	
	protected int getFontHeight() {
		if (Menace.instance.moduleManager.hudModule.customFont.getValue()) {
			return fr.FONT_HEIGHT;
		} else {
			return MC.fontRendererObj.FONT_HEIGHT;
		}
	}
	
}
