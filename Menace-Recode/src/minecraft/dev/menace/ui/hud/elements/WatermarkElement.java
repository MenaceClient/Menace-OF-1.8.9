package dev.menace.ui.hud.elements;

import dev.menace.Menace;
import dev.menace.module.modules.render.HUDModule;
import dev.menace.ui.hud.BaseElement;
import dev.menace.utils.misc.ServerUtils;
import dev.menace.utils.render.ColorUtils;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class WatermarkElement extends BaseElement {



	public WatermarkElement() {
		super(2, 2, true);
	}

	@Override
	public void render() {
		HUDModule hudModule = Menace.instance.moduleManager.hudModule;
		int color = hudModule.color.getValue().equalsIgnoreCase("Custom") ? new Color(hudModule.red.getValueI(), hudModule.green.getValueI(), hudModule.blue.getValueI(), hudModule.alpha.getValueI()).getRGB() : ColorUtils.fade(hudModule.rainbowSpeed.getValueF(), -this.getAbsoluteY()).getRGB();
		RenderUtils.drawRect(this.getAbsoluteX(), this.getAbsoluteY(), this.getAbsoluteX() + this.getWidth(), this.getAbsoluteY() + 2, color);
		RenderUtils.drawRect(this.getAbsoluteX(), this.getAbsoluteY() + 2, this.getAbsoluteX() + this.getWidth(), this.getAbsoluteY() + this.getFontHeight() + 4, new Color(0, 0, 0, 255).getRGB());
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
