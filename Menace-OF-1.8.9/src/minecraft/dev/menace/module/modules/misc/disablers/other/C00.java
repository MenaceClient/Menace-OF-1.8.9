package dev.menace.module.modules.misc.disablers.other;

import java.util.Random;

import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.modules.misc.disablers.DisablerBase;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.server.S00PacketKeepAlive;

public class C00 extends DisablerBase {
    @Override
    public void onSendPacket(EventSendPacket event) {
    	if (event.getPacket() instanceof C00PacketKeepAlive) {
    		C00PacketKeepAlive packet = (C00PacketKeepAlive) event.getPacket();
    		Random rand = new Random();
    		packet.setkey(rand.nextInt(100000));
    	}
    }
}
