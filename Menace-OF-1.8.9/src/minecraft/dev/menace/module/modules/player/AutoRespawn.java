package dev.menace.module.modules.player;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;

public class AutoRespawn extends Module {

	public AutoRespawn() {
		super("AutoRespawn", 0, Category.PLAYER);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (MC.currentScreen instanceof GuiGameOver) {
			MC.displayGuiScreen((GuiScreen)null);
			MC.thePlayer.respawnPlayer();
		}
	}

}
