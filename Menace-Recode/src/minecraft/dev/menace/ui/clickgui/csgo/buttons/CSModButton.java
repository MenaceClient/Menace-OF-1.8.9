package dev.menace.ui.clickgui.csgo.buttons;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.text.html.CSS;

import dev.menace.Menace;
import dev.menace.module.BaseModule;
import dev.menace.module.settings.*;
import dev.menace.ui.clickgui.csgo.buttons.setting.CSSetting;
import dev.menace.ui.clickgui.csgo.buttons.setting.settings.CSSettingBind;
import dev.menace.ui.clickgui.csgo.buttons.setting.settings.CSSettingCheck;
import dev.menace.ui.clickgui.csgo.buttons.setting.settings.CSSettingCombo;
import dev.menace.ui.clickgui.csgo.buttons.setting.settings.CSSettingComboValue;
import dev.menace.ui.clickgui.csgo.buttons.setting.settings.CSSettingDouble;
import net.minecraft.client.gui.GuiScreen;

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
		for (Setting s : this.mod.getSettings()) {
			if (s.isToggle()) {

				CSSettingCheck check = new CSSettingCheck(x, y, y, x, (ToggleSetting) s);

				settings.add(check);

				y += 13;
			}
			if (s.isSlider()) {

				CSSettingDouble doubleset = new CSSettingDouble(x, y, 0, 0, (SliderSetting) s);

				settings.add(doubleset);

				y += 15;

			}
			if (s.isList()) {
				int yplus = y;

				CSSettingCombo combo = new CSSettingCombo(x, y, 70, mc.fontRendererObj.FONT_HEIGHT + 2, (ListSetting) s);
				settings.add(combo);

				for (int i1 = 0; i1 < ((ListSetting) s).getOptions().length; i1++) {
					y += fr.FONT_HEIGHT + 2;
					if (y > 100 + GuiScreen.width - 220) {
						y = 0;
						x += mc.fontRendererObj.getStringWidth(s.getName()) + 50;
					}

				}

				y += fr.FONT_HEIGHT + 5;

			}

			if (y > GuiScreen.height-110) {
				y = 110;
				x += mc.fontRendererObj.getStringWidth(s.getName()) + 50;
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
		for (CSSetting cs : settings) {
			cs.set.constantCheck();
			if (isCurrentMod() && cs.set.isVisible()) {
				Setting s = cs.set;
				if (s.isToggle()) {

					cs.x = x;
					cs.y = y;
					cs.width = y;
					cs.height = x;

					y += 13;
				}
				if (s.isSlider()) {

					cs.x = x;
					cs.y = y;
					cs.width = 0;
					cs.height = 0;

					y += 15;

				}
				if (s.isList()) {
					int yplus = y;
					
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
					for (int i1 = 0; i1 < ((ListSetting) s).getOptions().length; i1++) {
						y += fr.FONT_HEIGHT + 2;
						if (y > 100 + GuiScreen.width - 220) {
							y = 0;
							x += mc.fontRendererObj.getStringWidth(s.getName()) + 50;
						}

					}

					y += fr.FONT_HEIGHT + 5;

				}

				if (y > GuiScreen.height-110) {
					y = 110;
					x += mc.fontRendererObj.getStringWidth(s.getName()) + 50;
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
