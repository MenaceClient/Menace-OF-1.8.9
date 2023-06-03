package dev.menace.ui.clickgui.csgo.buttons;

import dev.menace.Menace;
import dev.menace.module.BaseModule;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.Setting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.ui.clickgui.csgo.buttons.setting.CSSetting;
import dev.menace.ui.clickgui.csgo.buttons.setting.settings.*;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;

public class CSModButton extends CSButton {

	public BaseModule mod;
	public CSSettingBind bindSetting;

	public CSModButton(int x, int y, int width, int height, int color, String displayString, BaseModule mod) {
		super(x, y, width, height, color, displayString);
		this.mod = mod;
		initSettings();
	}

	private void initSettings() {
		int y = 110;
		int x = this.x + 100;
		int longestName = 0;
		for (Setting s : this.mod.getSettings()) {
			if (s instanceof ToggleSetting) {

				CSSettingCheck check = new CSSettingCheck(x, y, y, x, (ToggleSetting) s);

				settings.add(check);

				if (mc.fontRendererObj.getStringWidth(s.getName()) + 50 > longestName) {
					longestName = mc.fontRendererObj.getStringWidth(s.getName()) + 50;
				}

				y += 13;
			}
			if (s instanceof SliderSetting) {

				CSSettingDouble doubleset = new CSSettingDouble(x, y, 0, 0, (SliderSetting) s);

				settings.add(doubleset);

				if (mc.fontRendererObj.getStringWidth(s.getName()) + 50 > longestName) {
					longestName = mc.fontRendererObj.getStringWidth(s.getName()) + 50;
				}

				y += 15;

			}
			if (s instanceof ListSetting) {

				CSSettingCombo combo = new CSSettingCombo(x, y, 70, mc.fontRendererObj.FONT_HEIGHT + 2, (ListSetting) s);
				settings.add(combo);

				if (mc.fontRendererObj.getStringWidth(s.getName()) + 50 > longestName) {
					longestName = mc.fontRendererObj.getStringWidth(s.getName()) + 50;
				}

				for (int i1 = 0; i1 < ((ListSetting) s).getOptions().length; i1++) {
					y += fr.FONT_HEIGHT + 2;
					if (y > 100 + GuiScreen.width - 220) {
						y = 110;
						x += longestName;
					}

				}

				y += fr.FONT_HEIGHT + 5;

			}

			if (y > GuiScreen.height-110) {
				y = 110;
				x += longestName;
			}
		}
		
		bindSetting = new CSSettingBind(x, y, y, x, mod);
	}

	public ArrayList<CSSetting> settings = new ArrayList<CSSetting>();

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		int color = this.isHovered(mouseX, mouseY) ? Menace.instance.getClientColor() : 0xFFFFFFFF;

		if (this.mod.isToggled()) {
			color = Menace.instance.getClientColorDarker();
		}

		if (this.isCurrentMod()) {
			color = Menace.instance.getClientColor();
		}

		fr.drawString(displayString, x, y, color);

		int y = 110;
		int x = this.x + 100;
		int longestName = 0;
		for (CSSetting cs : settings) {
			cs.set.constantCheck();
			if (isCurrentMod() && cs.set.isVisible()) {
				Setting s = cs.set;
				if (s instanceof ToggleSetting) {

					cs.x = x;
					cs.y = y;
					cs.width = y;
					cs.height = x;

					if (mc.fontRendererObj.getStringWidth(s.getName()) + 50 > longestName) {
						longestName = mc.fontRendererObj.getStringWidth(s.getName()) + 50;
					}

					y += 13;
				}
				if (s instanceof SliderSetting) {

					cs.x = x;
					cs.y = y;
					cs.width = 0;
					cs.height = 0;
					if (mc.fontRendererObj.getStringWidth(s.getName()) + 50 > longestName) {
						longestName = mc.fontRendererObj.getStringWidth(s.getName()) + 50;
					}

					y += 15;

				}
				if (s instanceof ListSetting) {
					cs.x = x;
					cs.y = y;
					cs.width = 70;
					cs.height = mc.fontRendererObj.FONT_HEIGHT + 2;
					int xx = cs.x;
					int yy = cs.y + cs.height;
					for (CSSettingComboValue value : ((CSSettingCombo)cs).values) {
						value.x = xx;
						value.y = yy;
						value.width = 70;
						value.height = mc.fontRendererObj.FONT_HEIGHT + 2;
						
						yy += mc.fontRendererObj.FONT_HEIGHT + 2;
					}

					if (mc.fontRendererObj.getStringWidth(s.getName()) + 50 > longestName) {
						longestName = mc.fontRendererObj.getStringWidth(s.getName()) + 50;
					}

					for (int i1 = 0; i1 < ((ListSetting) s).getOptions().length; i1++) {
						y += fr.FONT_HEIGHT + 2;
						if (y > 100 + GuiScreen.width - 220) {
							y = 0;
							x += longestName;
						}

					}

					y += fr.FONT_HEIGHT + 5;

				}

				if (y > GuiScreen.height-110) {
					y = 110;
					x += longestName;
				}
				
				cs.drawScreen(mouseX, mouseY, partialTicks);
			}
		}
		
		if (isCurrentMod()) {
			bindSetting.x = x;
			bindSetting.y = y;
			bindSetting.width = y;
			bindSetting.height = x;
			bindSetting.drawScreen(mouseX, mouseY, partialTicks);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

		if (this.isHovered(mouseX, mouseY)) {

			if (mouseButton == 0 && isHovered(mouseX, mouseY) && Menace.instance.moduleManager.clickGuiModule.csgoGui.currentCategory != null
					&& Menace.instance.moduleManager.clickGuiModule.csgoGui.currentCategory.category == this.mod.getCategory()) {
				this.mod.toggle();

			} else if (mouseButton == 1) {
				try {

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		for (CSSetting cs : settings) {
			if (isCurrentMod() && cs.set.isVisible()) {
				cs.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
		
		if (isCurrentMod()) {
			bindSetting.mouseClicked(mouseX, mouseY, mouseButton);
		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		if (isCurrentMod()) {
			bindSetting.keyTyped(keyCode);
		}
		super.keyTyped(typedChar, keyCode);
	}

	public boolean isHovered(int mouseX, int mouseY) {
		boolean hoveredx = mouseX > this.x && mouseX < this.x + this.width;
		boolean hoveredy = mouseY > this.y && mouseY < this.y + this.height;
		return hoveredx && hoveredy;
	}

	private boolean isCurrentMod() {
		return Menace.instance.moduleManager.clickGuiModule.csgoGui.currentCategory != null
				&& Menace.instance.moduleManager.clickGuiModule.csgoGui.currentCategory.currentMod != null
				&& Menace.instance.moduleManager.clickGuiModule.csgoGui.currentCategory.currentMod == this;
	}

	@Override
	public void initButton() {
		initSettings();

		super.initButton();
	}

}
