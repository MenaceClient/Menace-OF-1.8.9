package dev.menace.ui.hud;

import java.awt.*;

import dev.menace.Menace;
import dev.menace.module.modules.render.HUDModule;
import dev.menace.utils.render.ColorUtils;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.render.font.Fonts;
import dev.menace.utils.render.font.MenaceFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public abstract class BaseElement {

	{
		HUDManager.hudElements.add(this);
	}

	protected Minecraft mc = Minecraft.getMinecraft();
	private double posX, posY;
	private boolean visible;
	private final MenaceFontRenderer fr = Menace.instance.sfPro;
	
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
		ScaledResolution sr = new ScaledResolution(mc);
		return (int) (posX * sr.getScaledWidth());
	}
	
	public int getAbsoluteY() {
		ScaledResolution sr = new ScaledResolution(mc);
		return (int) (posY * sr.getScaledHeight());
	}
	
	public double getRelativeX() {
		return posX;
	}
	
	public double getRelativeY() {
		return posY;
	}
	
	public void setAbsolute(int x, int y) {
		ScaledResolution sr = new ScaledResolution(mc);
		
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
	
	public void drawString(String string, int x, int y) {
		int color;
		HUDModule hudModule = Menace.instance.moduleManager.hudModule;
		switch (hudModule.color.getValue()) {
		
		case "Fade" :
			color = ColorUtils.fade(hudModule.rainbowSpeed.getValueF(), -y).getRGB();
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
			mc.fontRendererObj.drawString(string, x, y, color);
		}
	}

	public void drawString(String string, double x, double y, int color) {
		if (Menace.instance.moduleManager.hudModule.customFont.getValue()) {
			fr.drawString(string, x, y, color);
		} else {
			mc.fontRendererObj.drawString(string, x, y, color);
		}
	}
	
	protected int getStringWidth(String string) {
		if (Menace.instance.moduleManager.hudModule.customFont.getValue()) {
			return fr.getStringWidth(string);
		} else {
			return mc.fontRendererObj.getStringWidth(string);
		}
	}
	
	protected int getFontHeight() {
		if (Menace.instance.moduleManager.hudModule.customFont.getValue()) {
			return fr.getHeight();
		} else {
			return mc.fontRendererObj.FONT_HEIGHT;
		}
	}
	
}
