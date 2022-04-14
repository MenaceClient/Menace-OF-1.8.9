package dev.menace.module.modules.combat;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventAttack;
import dev.menace.module.Category;
import dev.menace.module.Module;
import net.minecraft.network.play.client.C02PacketUseEntity;

public class ComboOneTap extends Module {

	public ComboOneTap() {
		super("ComboOneTap", 0, Category.COMBAT);
	}
	
	@EventTarget
	public void onAttack(EventAttack event) {
		for (int i = 0; i <= 50; i++) {
			MC.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(event.getTargetEntity(), C02PacketUseEntity.Action.ATTACK));
		}
	}

}
