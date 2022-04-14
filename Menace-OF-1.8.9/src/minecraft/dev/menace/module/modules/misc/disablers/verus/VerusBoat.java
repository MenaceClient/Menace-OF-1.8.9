package dev.menace.module.modules.misc.disablers.verus;

import dev.menace.Menace;
import dev.menace.module.modules.misc.disablers.DisablerBase;
import dev.menace.utils.misc.ChatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;

public class VerusBoat extends DisablerBase {

	@Override
	public void onEnable() {
		
		if (!MC.thePlayer.isRiding() || !(MC.thePlayer.ridingEntity instanceof EntityBoat)) {
			Menace.instance.moduleManager.disablerModule.toggle();
			ChatUtils.message("You need to be in a boat for this to work!");
			return;
		}
		
		for (Entity e : MC.theWorld.loadedEntityList) {
			if (e instanceof EntityBoat && e.getDistanceToEntity(MC.thePlayer) <= 6) {
				MC.thePlayer.swingItem();
				MC.playerController.attackEntity(MC.thePlayer, e);
			}
		}
		
		Menace.instance.moduleManager.disablerModule.toggle();
		ChatUtils.message("Verus Disabled!");
	}
}
