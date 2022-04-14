package dev.menace.module.modules.world;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;

public class AntiWeb extends Module {

	public AntiWeb() {
		super("AntiWeb", 0, Category.WORLD);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		MC.thePlayer.isInWeb = false;
	}

}
