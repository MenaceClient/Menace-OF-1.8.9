package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.utils.entity.self.PlayerUtils;

public class Sprint extends Module {

	public Sprint() {
		super("Sprint", 0, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (PlayerUtils.isMoving()) {
			MC.thePlayer.setSprinting(true);
		}
	}

}
