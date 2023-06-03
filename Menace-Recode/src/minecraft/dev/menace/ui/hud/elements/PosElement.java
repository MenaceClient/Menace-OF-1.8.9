package dev.menace.ui.hud.elements;

import dev.menace.Menace;
import dev.menace.ui.hud.BaseElement;
import dev.menace.utils.misc.ServerUtils;
import net.minecraft.client.network.OldServerPinger;

public class PosElement extends BaseElement {

	public PosElement() {
		super(1, 530, true);
	}

	@Override
	public void render() {
		if (Menace.instance.moduleManager.hudModule.posMode.getValue().equalsIgnoreCase("Single")) {
			this.drawString(String.format("X: %.1f", mc.thePlayer.posX) + " |" + String.format(" Y: %.1f", mc.thePlayer.boundingBox.minY) + " |" + String.format(" Z: %.1f", mc.thePlayer.posZ), this.getAbsoluteX(), this.getAbsoluteY());
		} else {
			this.drawString(String.format("X: %.1f", mc.thePlayer.posX), this.getAbsoluteX(), this.getAbsoluteY());
			this.drawString(String.format("Y: %.1f", mc.thePlayer.boundingBox.minY), this.getAbsoluteX(), this.getAbsoluteY() + 10);
			this.drawString(String.format("Z: %.1f", mc.thePlayer.posZ), this.getAbsoluteX(), this.getAbsoluteY() + 20);
		}
	}

	@Override
	public void renderDummy() {
		if (Menace.instance.moduleManager.hudModule.posMode.getValue().equalsIgnoreCase("Single")) {
			this.drawString(String.format("X: %.1f", mc.thePlayer.posX) + " |" + String.format(" Y: %.1f", mc.thePlayer.boundingBox.minY) + " |" + String.format(" Z: %.1f", mc.thePlayer.posZ), this.getAbsoluteX(), this.getAbsoluteY());
		} else {
			this.drawString(String.format("X: %.1f", mc.thePlayer.posX), this.getAbsoluteX(), this.getAbsoluteY());
			this.drawString(String.format("Y: %.1f", mc.thePlayer.boundingBox.minY), this.getAbsoluteX(), this.getAbsoluteY() + 10);
			this.drawString(String.format("Z: %.1f", mc.thePlayer.posZ), this.getAbsoluteX(), this.getAbsoluteY() + 20);
		}
	}

	@Override
	public int getWidth() {
		if (Menace.instance.moduleManager.hudModule.posMode.getValue().equalsIgnoreCase("Single")) {
			return this.getStringWidth("X: 100.1 | Y: 100.1 | Z: 100.1");
		} else {
			return this.getStringWidth("X: 100.1");
		}
	}

	@Override
	public int getHeight() {
		if (Menace.instance.moduleManager.hudModule.posMode.getValue().equalsIgnoreCase("Single")) {
			return this.getFontHeight();
		} else {
			return 25;
		}
	}

}
