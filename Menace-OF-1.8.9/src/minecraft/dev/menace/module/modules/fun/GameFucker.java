package dev.menace.module.modules.fun;

import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.utils.misc.ChatUtils;

public class GameFucker extends Module {

	public GameFucker() {
		super("GameFucker", 0, Category.FUN);
	}
	
	@Override
	public void onEnable() {
		this.toggle();
		ChatUtils.message("idfk");
		MC.shutdownMinecraftApplet();
	}

}
