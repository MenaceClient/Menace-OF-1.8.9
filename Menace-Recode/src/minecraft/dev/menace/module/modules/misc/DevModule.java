package dev.menace.module.modules.misc;

import org.lwjgl.input.Keyboard;

import dev.menace.module.Category;
import dev.menace.module.BaseModule;
import dev.menace.utils.misc.ChatUtils;

public class DevModule extends BaseModule {

	public DevModule() {
		super("DevModule", Category.MISC, Keyboard.KEY_P);
	}
	
	@Override
	public void onEnable() {
		ChatUtils.message("Enable");
		super.onEnable();
		
	}
	
	@Override
	public void onDisable() {
		ChatUtils.message("Disable");
		super.onDisable();
	}

}
