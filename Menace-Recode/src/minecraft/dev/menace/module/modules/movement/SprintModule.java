package dev.menace.module.modules.movement;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.player.MovementUtils;

import java.awt.*;

public class SprintModule extends BaseModule {

	public ToggleSetting omniSprint;
	
	public SprintModule() {
		super("Sprint", "ToggleSprint but better", Category.MOVEMENT, 0);
	}
	
	@Override
	public void setup() {
		omniSprint = new ToggleSetting("OmniSprint", true, false);
		this.rSetting(omniSprint);
		super.setup();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (MovementUtils.isMoving() && mc.thePlayer.moveForward > 0 && (!Menace.instance.moduleManager.scaffoldModule.isToggled() || Menace.instance.moduleManager.scaffoldModule.sprint.getValue())) {
			mc.thePlayer.setSprinting(true);
		}
	}

}
