package dev.menace.module.modules.misc.disablers.other;

import dev.menace.event.events.EventSendPacket;
import dev.menace.module.modules.misc.disablers.DisablerBase;
import dev.menace.utils.misc.ChatUtils;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

public class Kauri extends DisablerBase {
	
	@Override
	 public void onEnable() {
	        if(!MC.isSingleplayer()) {
	            ChatUtils.message("[Disabler] §4Relog for disabler to work.");
	        }
	}
	
	@Override
	public void onSendPacket(EventSendPacket event) {
        if(event.getPacket() instanceof C0FPacketConfirmTransaction) event.setCancelled(true);
    }
}
