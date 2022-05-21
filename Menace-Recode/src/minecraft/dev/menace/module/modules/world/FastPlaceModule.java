package dev.menace.module.modules.world;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;

public class FastPlaceModule extends BaseModule {

	public FastPlaceModule() {
		super("FastPlace", Category.WORLD, 0);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		MC.rightClickDelayTimer = 0;
	}
	
}
