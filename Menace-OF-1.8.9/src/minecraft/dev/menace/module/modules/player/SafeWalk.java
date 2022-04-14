package dev.menace.module.modules.player;

import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.BoolSetting;
import dev.menace.Menace;

public class SafeWalk extends Module {

	//Settings
	public BoolSetting inair;
	
	public SafeWalk() {
		super("SafeWalk", 0, Category.PLAYER);
	}
	
	@Override
	public void setup() {
		inair = new BoolSetting("in air", this, false);
		this.rSetting(inair);
	}

}
