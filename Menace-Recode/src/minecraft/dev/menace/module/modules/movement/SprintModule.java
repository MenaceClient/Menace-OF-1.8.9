package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.player.MovementUtils;

public class SprintModule extends BaseModule {

	public SprintModule() {
		super("Sprint", Category.MOVEMENT, 0);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (MovementUtils.isMoving()) {
			MC.thePlayer.setSprinting(true);
		}
	}

}
