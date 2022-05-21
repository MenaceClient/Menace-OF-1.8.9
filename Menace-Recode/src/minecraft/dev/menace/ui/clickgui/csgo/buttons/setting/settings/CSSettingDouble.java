package dev.menace.ui.clickgui.csgo.buttons.setting.settings;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import dev.menace.Menace;
import dev.menace.module.settings.SliderSetting;
import dev.menace.ui.clickgui.csgo.buttons.setting.CSSetting;

public class CSSettingDouble extends CSSetting {

	public CSSettingDouble(int x, int y, int width, int height, SliderSetting s) {
		super(x, y, width, height, s);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		double reach = ((SliderSetting) this.set).getValue();

		double reach1 = reach * 100;

		double reach2 = Math.round(reach1);

		double round = reach2 / 100;
		mc.fontRendererObj.drawString("<", this.x + 1, this.y + 1,
				this.isHoveredLeft(mouseX, mouseY) ? Menace.instance.getClientColor() : Integer.MAX_VALUE);
		mc.fontRendererObj.drawString(">", this.x + 1 + fr.getStringWidth(this.set.getName() + " " + round) + 15,
				this.y + 1, this.isHoveredRight(mouseX, mouseY) ? Menace.instance.getClientColor() : Integer.MAX_VALUE);
		mc.fontRendererObj.drawString(this.set.getName() + " " + round, this.x + 10, this.y + 1, Integer.MAX_VALUE);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public boolean isHoveredLeft(int mouseX, int mouseY) {
		boolean hoveredx = mouseX > this.x + 1 && mouseX < this.x + 1 + 5;
		boolean hoveredy = mouseY > this.y + 1 && mouseY < this.y + mc.fontRendererObj.FONT_HEIGHT;
		return hoveredx && hoveredy;

	}

	public boolean isHoveredRight(int mouseX, int mouseY) {
		double round = Math.round(((SliderSetting) this.set).getValue() * 10) / 10;

		boolean hoveredx = mouseX > this.x + 1 + fr.getStringWidth(this.set.getName() + " " + round) + 15
				&& mouseX < this.x + 1 + fr.getStringWidth(this.set.getName() + " " + round) + 20;
		boolean hoveredy = mouseY > this.y + 1 && mouseY < this.y + mc.fontRendererObj.FONT_HEIGHT;
		return hoveredx && hoveredy;
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		SliderSetting s = (SliderSetting) this.set;
		if (mouseButton == 0) {
			if (isHoveredLeft(mouseX, mouseY)) {
				boolean more = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

				double plus = 0;
				if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
					if (s.onlyInt()) {
						plus = 1;
					} else {
						plus = 0.1;
					}
				} else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
					plus = s.onlyInt() ? 10 : 1;
				} else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
					plus = s.onlyInt() ? 1 : 0.01;
				} else if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
					plus = 0;
					s.setValue(s.getMin());
				}

				if (s.getValue() - plus < s.getMin() || s.getValue() - plus == s.getMin()) {
					s.setValue(s.getMin());
				} else if (s.getValue() - plus > s.getMin()) {

					s.setValue(s.getValue() - plus);

				}
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
					s.setValue(s.getMin());
				}
			} else if (isHoveredRight(mouseX, mouseY)) {
				double plus = 0;

				if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
					if (s.onlyInt()) {
						plus = 1;
					} else {
						plus = 0.1;
					}
				} else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
					plus = s.onlyInt() ? 10 : 1;
				} else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
					plus = s.onlyInt() ? 1 : 0.01;
				} else if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
					plus = 0;
					s.setValue(s.getMax());
				}

				if (s.getValue() + plus > s.getMax() || s.getValue() + plus == s.getMax()) {
					s.setValue(s.getMax());
				} else if (s.getValue() + plus < s.getMax()) {

					s.setValue(s.getValue() + plus);

				}

			}

		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

}
