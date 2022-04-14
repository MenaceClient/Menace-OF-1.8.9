package dev.menace.module.modules.world;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.utils.entity.self.PlayerUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.network.play.client.C03PacketPlayer;

public class ItemMagnet extends Module {

	public ItemMagnet() {
		super("ItemMagnet", 0, Category.WORLD);
	}

	@EventTarget
	public void onUpdate(EventUpdate event){
		for (Object theObject : MC.theWorld.loadedEntityList) {
			if (theObject instanceof EntityItem) {
				final EntityItem e = (EntityItem)theObject;
				PlayerUtils.tpToEnt(e);
			}
		}
	}
}
