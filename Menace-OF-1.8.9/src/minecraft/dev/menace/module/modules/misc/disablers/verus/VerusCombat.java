package dev.menace.module.modules.misc.disablers.verus;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import dev.menace.event.events.EventSendPacket;
import dev.menace.module.modules.misc.disablers.DisablerBase;
import dev.menace.utils.misc.MSTimer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

public class VerusCombat extends DisablerBase {
	
	private final MSTimer timer = new MSTimer();
	private final Queue<Packet<?>> packetQueue = new ConcurrentLinkedDeque<>();
	
	@Override
	public void onEnable() {
		this.timer.reset();
	}
	
	@Override
	public void onDisable() {
        while (!this.packetQueue.isEmpty()) {
            MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(this.packetQueue.poll());
         }
	}
	
	@Override
    public void onUpdate() {

        if (this.timer.hasTimePassed(250L)) {
            this.timer.reset();

            while (!this.packetQueue.isEmpty()) {
               MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(this.packetQueue.poll());
            }
        }
    }
	
	@Override
	public void onSendPacket(EventSendPacket event) {

		if (event.getPacket() instanceof C0FPacketConfirmTransaction) {

			short action = -1;
			action = ((C0FPacketConfirmTransaction) event.getPacket()).getUid();

			if (action != -1 && this.isInventory(action)) return;

			event.setCancelled(true);
			this.packetQueue.add(event.getPacket());
		}
		
        if (event.getPacket() instanceof C00PacketKeepAlive) {
        	event.setCancelled(true);
            this.packetQueue.add(event.getPacket());
        }

		if (event.getPacket() instanceof C0BPacketEntityAction) {
			//event.setCancelled(true);
		}
	}

	boolean isInventory(short action) {
		return action > 0 && action < 100;
	}

}
