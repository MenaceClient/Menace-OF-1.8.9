package dev.menace.module.modules.misc.disablers.verus;

import java.util.LinkedList;

import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.modules.misc.disablers.DisablerBase;
import dev.menace.utils.misc.MSTimer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class VerusScaffold extends DisablerBase {

	private final LinkedList<Packet<?>> packets = new LinkedList<>();
	private boolean teleportState;
	private final MSTimer timer = new MSTimer();
	private final LinkedList<Packet<?>> transactions = new LinkedList<>();

	@Override
	public void onEnable() {
        this.transactions.clear();
        this.teleportState = false;
        this.packets.clear();
        this.timer.reset();
	}

	@Override
	public void onSendPacket(EventSendPacket event) {
		if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
			{
				packets.add(event.getPacket());
				transactions.add(event.getPacket());
			}

			event.setCancelled(true);

			if (packets.size() > 300) {
				MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(packets.poll());
			}
		}

		if (event.getPacket() instanceof C0BPacketEntityAction)
			event.setCancelled(true);


		if (event.getPacket() instanceof C03PacketPlayer) {
			C03PacketPlayer c03PacketPlayer = (C03PacketPlayer) event.getPacket();

			if (MC.thePlayer.ticksExisted % 5 == 0) {
				MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C0CPacketInput());
			}
			if (MC.thePlayer.ticksExisted % 40 == 0) {
				teleportState = true;

				c03PacketPlayer.setY(-0.015625);
				c03PacketPlayer.setOnGround(false);
			}
		}
	}

	@Override
	public void onReceivePacket(EventReceivePacket event) {
		if (event.getPacket() instanceof S08PacketPlayerPosLook && teleportState) {
			final S08PacketPlayerPosLook packetPlayerPosLook = (S08PacketPlayerPosLook) event.getPacket();
			teleportState = false;

			{
				event.setCancelled(true);

				MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(packetPlayerPosLook.getX(), packetPlayerPosLook.getY(), packetPlayerPosLook.getZ(), packetPlayerPosLook.getYaw(), packetPlayerPosLook.getPitch(), true));
			}
		}
	}

	@Override
	public void onUpdate() {
		if (MC.thePlayer.ticksExisted % 180 == 0) {
			//IngameChatLog.INGAME_CHAT_LOG.doLog(transactions.size() + "");

			if (transactions.size() >= 20) {
				for (int i = 0; i < transactions.size(); i++) {
					MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(transactions.poll());
				}
			}
		}
		if (timer.hasTimePassed(370L)) {
			timer.reset();

			if (!packets.isEmpty()) {
				MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(packets.poll());
			}
		}
	}

}
