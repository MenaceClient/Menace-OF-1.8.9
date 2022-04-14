package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;

public class Spider extends Module {

	public Spider() {
		super("Spider", 0, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (MC.thePlayer.isCollidedHorizontally) {
			MC.thePlayer.motionY = 0.1;
		}
	}

}
