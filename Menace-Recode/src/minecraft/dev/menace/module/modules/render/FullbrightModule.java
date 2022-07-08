package dev.menace.module.modules.render;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;

public class FullbrightModule extends BaseModule {

	float oldBrightness;
	
	public FullbrightModule() {
		super("Fullbright", "Changes the world light level.", Category.RENDER, 0);
	}
	
	@Override
	public void onEnable() {
		oldBrightness = mc.gameSettings.saturation;
		super.onEnable();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		mc.gameSettings.saturation = 10F;
	}
	
	@Override
	public void onDisable() {
		mc.gameSettings.saturation = oldBrightness;
		super.onDisable();
	}

}
