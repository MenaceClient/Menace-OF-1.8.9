package dev.menace.ui.clickgui.csgo.buttons.setting.settings;

import java.io.IOException;
import java.util.ArrayList;

import dev.menace.module.settings.ListSetting;
import dev.menace.ui.clickgui.csgo.buttons.setting.CSSetting;
import net.minecraft.client.gui.Gui;

public class CSSettingCombo extends CSSetting {

	public CSSettingCombo(int x, int y, int width, int height, ListSetting s) {
		super(x, y, width, height, s);
		initValues();
	}

	public ArrayList<CSSettingComboValue> values = new ArrayList<CSSettingComboValue>();

	private void initValues() {
		int x = this.x;
		int y = this.y + this.height;
		for (String s : ((ListSetting) this.set).getOptions()) {
			CSSettingComboValue value = new CSSettingComboValue(x, y, 70, mc.fontRendererObj.FONT_HEIGHT + 2, (ListSetting) this.set,
					s);

			this.values.add(value);
			y += mc.fontRendererObj.FONT_HEIGHT + 2;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, Integer.MIN_VALUE);
		Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, Integer.MIN_VALUE);
		fr.drawString(displayString, this.x + width / 2 - fr.getStringWidth(displayString) / 2, this.y + 1, Integer.MAX_VALUE);

		for (CSSettingComboValue value : values) {
			value.drawScreen(mouseX, mouseY);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for(CSSettingComboValue value : values) {
			value.mouseClicked(mouseX, mouseY, mouseButton);
		}
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

}
