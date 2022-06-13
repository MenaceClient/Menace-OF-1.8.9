package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.player.MovementUtils;

public class SprintModule extends BaseModule {

	public ToggleSetting omniSprint;
	
	public SprintModule() {
		super("Sprint", Category.MOVEMENT, 0);
	}
	
	@Override
	public void setup() {
		omniSprint = new ToggleSetting("OmniSprint", true, false);
		this.rSetting(omniSprint);
		super.setup();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (MovementUtils.isMoving()) {
			MC.thePlayer.setSprinting(true);
		}
	}

}
