package dev.menace.gui.hud.element;

import dev.menace.utils.render.UnicodeFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class Element {

	private boolean isEnabled = false;
	
	protected final Minecraft MC;
	protected final FontRenderer font;
	protected final UnicodeFontRenderer specialFont;
	protected final dev.menace.Menace Menace;
	
	public Element() {
		this.MC = Minecraft.getMinecraft();
		this.font = this.MC.fontRendererObj;
		this.specialFont = UnicodeFontRenderer.getFontOnPC("Arial", 20);
		this.Menace = dev.menace.Menace.instance;
		
		setEnabled(isEnabled);
	}

	public void setEnabled(boolean enabled) {
		this.isEnabled = enabled;
		
		if (enabled) {
			Menace.eventManager.register(this);
		} else {
			Menace.eventManager.unregister(this);
		}
		
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
}
