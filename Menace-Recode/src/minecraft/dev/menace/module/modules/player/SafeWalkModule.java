package dev.menace.module.modules.player;

import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ToggleSetting;

public class SafeWalkModule extends BaseModule {

	public ToggleSetting inair;
	
	public SafeWalkModule() {
		super("SafeWalk", "No fall off block", Category.PLAYER, 0);
	}
	
	@Override
	public void setup() {
		inair = new ToggleSetting("InAir", true, false);
		this.rSetting(inair);
		super.setup();
	}

}
