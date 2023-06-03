package dev.menace.ui.clickgui.csgo.buttons.setting.settings;

import java.awt.Color;
import java.io.IOException;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.ui.clickgui.csgo.buttons.setting.CSSetting;
import dev.menace.ui.clickgui.csgo.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class CSSettingCheck extends CSSetting {

	public CSSettingCheck(int x, int y, int width, int height, ToggleSetting s) {
		super(x, y, width, height, s);
	}

	private int animation = 20;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		fr.drawString(displayString, x, y, Integer.MAX_VALUE);

		int stringwidth = fr.getStringWidth(displayString);
		
		Gui.drawRect(x + stringwidth + 20, y, x + stringwidth + 30, y + 10, 0xFF000000);
		RenderHelper.drawFullCircle(x + stringwidth + 20, y + 5, 5, 0xFF000000);
		RenderHelper.drawFullCircle(x + stringwidth + 30, y + 5, 5, 0xFF000000);
		RenderHelper.drawFullCircle(x + stringwidth + animation, y + 5, 5, ((ToggleSetting) this.set).getValue() ? Color.GREEN.getRGB() : Color.RED.getRGB());

		if (((ToggleSetting) this.set).getValue()) {
			if (animation < 30) {
				animation++;
			}
		} else {
			if (animation > 20) {
				animation--;
			}
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (isHovered(mouseX, mouseY) && mouseButton == 0) {
			((ToggleSetting) this.set).setValue(!((ToggleSetting) this.set).getValue());
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	private boolean isHovered(int mouseX, int mouseY) {
		int stringwidth = fr.getStringWidth(displayString);
		boolean hoveredx = mouseX > this.x + stringwidth + 15 && mouseX < this.x + stringwidth + 35;
		boolean hoveredy = mouseY > this.y && mouseY < this.y + 10;
		return hoveredx && hoveredy;
	}

}
