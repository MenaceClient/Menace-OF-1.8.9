package dev.menace.module.modules.render;

import dev.menace.Menace;
import dev.menace.module.Category;
import dev.menace.module.Module;

public class HudDesigner extends Module {

	public HudDesigner() {
		super("HudDesigner", 0, Category.RENDER);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		this.toggle();
		
		Menace.instance.hudManager.openConfigScreen();
	}

}
