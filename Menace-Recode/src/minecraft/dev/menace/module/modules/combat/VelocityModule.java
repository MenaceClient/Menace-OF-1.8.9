package dev.menace.module.modules.combat;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class VelocityModule extends BaseModule {

	public VelocityModule() {
		super("Velocity", "Stop KB like a pro.", Category.COMBAT, 0);
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		if (event.getPacket() instanceof S12PacketEntityVelocity && MC.thePlayer.hurtTime > 0) {
			event.cancel();
		}
	}

}
