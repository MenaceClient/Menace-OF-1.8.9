package dev.menace.module.modules.player;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.utils.misc.ChatUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastEat extends Module {

	private boolean usedTimer;

	public FastEat() {
		super("FastEat", 0, Category.PLAYER);
	}

	@EventTarget
	public void onPre(EventPreMotionUpdate event) {
		if (MC.thePlayer.isEating() /*&& !Menace.instance.moduleManager.scaffoldModule.isToggled()*/) {
			for (int i = 0; i < 10; i++) {
				MC.getNetHandler().addToSendQueue(new C03PacketPlayer(MC.thePlayer.onGround));
				MC.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY, MC.thePlayer.posZ, MC.thePlayer.onGround));
			}
		}
	}

}
