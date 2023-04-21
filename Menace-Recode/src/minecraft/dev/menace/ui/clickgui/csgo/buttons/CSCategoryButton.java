package dev.menace.ui.clickgui.csgo.buttons;

import java.io.IOException;
import java.util.ArrayList;

import dev.menace.Menace;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.ModuleManager;

public class CSCategoryButton extends CSButton {

	public Category category;
	public CSModButton currentMod;

	public ArrayList<CSModButton> buttons = new ArrayList<CSModButton>();

	public CSCategoryButton(int x, int y, int width, int height, int color, String displayString, Category category) {
		super(x, y, width, height, color, displayString);
		this.category = category;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		int color = this.isHovered(mouseX, mouseY) || Menace.instance.moduleManager.clickGuiModule.csgoGui.currentCategory == this ? Menace.instance.getClientColor()
				: this.color;

		fr.drawString(displayString, x, y, color);

		for (CSModButton csm : buttons) {
			if (Menace.instance.moduleManager.clickGuiModule.csgoGui.currentCategory == this) {
				csm.drawScreen(mouseX, mouseY, partialTicks);
			}
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public boolean isHovered(int mouseX, int mouseY) {
		boolean hoveredx = mouseX > this.x && mouseX < this.x + this.width;
		boolean hoveredy = mouseY > this.y && mouseY < this.y + this.height;
		return hoveredx && hoveredy;
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (CSModButton cmb : buttons) {
			cmb.mouseClicked(mouseX, mouseY, mouseButton);

			if (mouseButton == 1 && cmb.isHovered(mouseX, mouseY)) {
				this.currentMod = cmb;
			}

		}

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		for (CSModButton cmb : buttons) {
			cmb.keyTyped(typedChar, keyCode);
		}
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public void initButton() {
		this.buttons.clear();
		int x = this.x + 65;
		int y = 110;
		for (int i = 0; i < ModuleManager.modules.size(); i++) {
			BaseModule m = ModuleManager.modules.get(i);
			if (m.getCategory() == this.category) {
				CSModButton csm = new CSModButton(x, y, fr.getStringWidth(m.getName()), fr.FONT_HEIGHT, 0xFFFFFFFF,
						m.getName(), m);

				this.buttons.add(csm);
				
				y += 22;

			}
		}

		super.initButton();
	}

	private boolean isCurrentPanel() {
		return Menace.instance.moduleManager.clickGuiModule.csgoGui.currentCategory == this;
	}

}
