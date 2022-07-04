package dev.menace.ui.hud.elements;

import dev.menace.Menace;
import dev.menace.ui.hud.BaseElement;
import dev.menace.utils.misc.ServerUtils;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class WatermarkElement extends BaseElement {

	public WatermarkElement() {
		super(2, 2, true);
	}
	
	@Override
	public void render() {
		//this.drawString("Menace §01.8.9", this.getAbsoluteX(), this.getAbsoluteY());

		RenderUtils.drawRect(this.getAbsoluteX(), this.getAbsoluteY(), this.getAbsoluteX() + this.getWidth(), this.getAbsoluteY() + 2, Menace.instance.getClientColor());
		RenderUtils.drawRect(this.getAbsoluteX(), this.getAbsoluteY() + 2, this.getAbsoluteX() + this.getWidth(), this.getAbsoluteY() + this.getFontHeight() + 4, new Color(0, 0, 0, 100).getRGB());
		this.drawString("Menace | " + Minecraft.debugFPS + " FPS | " + ServerUtils.getRemoteIp(), this.getAbsoluteX() + 2, this.getAbsoluteY() + 3, Color.white.getRGB());
	}

	@Override
	public void renderDummy() {

	}

	@Override
	public int getWidth() {
		return this.getStringWidth("Menace | " + Minecraft.debugFPS + " FPS | " + ServerUtils.getRemoteIp()) + 2;
	}

	@Override
	public int getHeight() {
		return this.getFontHeight() + 3;
	}

}
