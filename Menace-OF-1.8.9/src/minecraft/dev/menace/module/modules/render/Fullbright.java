package dev.menace.module.modules.render;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;

public class Fullbright extends Module {

	float oldBrightness;
	
	public Fullbright() {
		super("Fullbright", 0, Category.RENDER);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		oldBrightness = MC.gameSettings.saturation;
	}
	
    @EventTarget
    public void onUpdate(EventUpdate event) {
        MC.gameSettings.saturation = 10F;
    }
	
	@Override
	public void onDisable() {
		super.onDisable();
		
		MC.gameSettings.saturation = oldBrightness;
	}

}
