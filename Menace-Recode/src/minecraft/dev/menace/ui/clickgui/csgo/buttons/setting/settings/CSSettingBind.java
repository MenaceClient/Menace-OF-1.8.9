package dev.menace.ui.clickgui.csgo.buttons.setting.settings;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

import dev.menace.module.BaseModule;
import dev.menace.module.settings.Setting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.ui.clickgui.csgo.buttons.setting.CSSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class CSSettingBind {

	public int x, y, width, height;
	public Minecraft mc = Minecraft.getMinecraft();
	public FontRenderer fr = mc.fontRendererObj;
	public BaseModule mod;
	public String name;
	public boolean binding;
	
	public CSSettingBind(int x, int y, int width, int height, BaseModule mod) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.mod = mod;
		name = "Bind: " + Keyboard.getKeyName(mod.getKeybind());
		binding = false;
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		fr.drawString(name, x, y, Integer.MAX_VALUE);
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (isHovered(mouseX, mouseY) && mouseButton == 0) {
			if (binding) {
				name = "Bind: " + Keyboard.getKeyName(mod.getKeybind());
				binding = false;
				return;
			}
			name = "Binding...";
			binding = true;
		}
	}
	
	public void keyTyped(int key) {
		if (binding) {
			if (key == Keyboard.KEY_DELETE || key == Keyboard.KEY_ESCAPE) {
				mod.setKeybind(0);
				name = "Bind: " + Keyboard.getKeyName(mod.getKeybind());
				binding = false;
				return;
			}
			
			mod.setKeybind(key);
			name = "Bind: " + Keyboard.getKeyName(mod.getKeybind());
			binding = false;
		}
	}

	private boolean isHovered(int mouseX, int mouseY) {
		int stringwidth = fr.getStringWidth(name);
		boolean hoveredx = mouseX > this.x && mouseX < this.x + stringwidth;
		boolean hoveredy = mouseY > this.y && mouseY < this.y + fr.FONT_HEIGHT;
		return hoveredx && hoveredy;
	}

}
