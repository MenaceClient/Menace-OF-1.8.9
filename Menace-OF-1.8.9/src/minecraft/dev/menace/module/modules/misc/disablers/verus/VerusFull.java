package dev.menace.module.modules.misc.disablers.verus;

import java.util.ArrayList;
import java.util.LinkedList;

import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.modules.misc.disablers.DisablerBase;
import dev.menace.utils.misc.MSTimer;
import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;

public class VerusFull extends DisablerBase {

	public ArrayList<Packet> packets;

	public LinkedList<Packet<INetHandlerPlayServer>> packetQueue;

	public MSTimer timer;

	public MSTimer timer2;

	private boolean inGround;

	private boolean transaction;

	private boolean transactionMultiply;

	private boolean transactionSend;

	public VerusFull() {
		this.packets = new ArrayList<Packet>();
		this.packetQueue = new LinkedList<Packet<INetHandlerPlayServer>>();
		this.timer = new MSTimer();
		this.timer2 = new MSTimer();
		this.inGround = true;
		this.transaction = true;
		this.transactionMultiply = true;
		this.transactionSend = true;
	}

	@Override
	public void onEnable() {
		MC.thePlayer.ticksExisted = 0;
		this.timer.reset();
		this.timer2.reset();
	}

	@Override
	public void onDisable() {
		if (this.packets != null && this.packets.size() > 0)
			this.packets.clear(); 
		if (this.packetQueue != null && this.packetQueue.size() > 0)
			this.packetQueue.clear(); 
		this.timer.reset();
		this.timer2.reset();
	}

	@Override
	public void onUpdate() {
		boolean send = this.transactionSend;
		if (send)
			while (this.packetQueue.size() > 22) {
				MC.getNetHandler().getNetworkManager().sendPacketNoEvent(this.packetQueue.poll());
			}  
	}

	@Override
	public void onSendPacket(EventSendPacket event) {
		if (MC.thePlayer != null && MC.thePlayer.ticksExisted == 0)
			this.packetQueue.clear(); 
		if (event.getPacket() instanceof C03PacketPlayer) { 
			if (this.inGround) {
				if (MC.thePlayer.ticksExisted % 85 == 0) {
					MC.getNetHandler().getNetworkManager().sendPacketNoEvent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, 
							MC.thePlayer.posY, MC.thePlayer.posZ, false));
					MC.getNetHandler().getNetworkManager().sendPacketNoEvent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, 
							MC.thePlayer.posY - 90.0D, MC.thePlayer.posZ, true));
					MC.getNetHandler().getNetworkManager().sendPacketNoEvent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, 
							MC.thePlayer.posY, MC.thePlayer.posZ, false));
					event.setCancelled(true);
				}
			}
		} else if (event.getPacket() instanceof  C0FPacketConfirmTransaction) {
			boolean c0f = this.transaction;
			boolean c0fMultiply = this.transactionMultiply;
			if (c0f) {
				if (c0fMultiply) {
					for (int i = 0; i < 1; i++)
						this.packetQueue.add(event.getPacket()); 
					event.setCancelled(true);
				} else {
					this.packetQueue.add(event.getPacket());
				} 
				event.setCancelled(true);
			} 
		} 

	}

	@Override
	public void onReceivePacket(EventReceivePacket event) {
		if (event.getPacket() instanceof S08PacketPlayerPosLook) {
			event.setCancelled(true);
			//MC.getNetHandler().addToSendQueue((Packet)new C0FPacketConfirmTransaction(2147483647, 
					//packetConfirmTransaction.getUid(), true));
			double x = ((S08PacketPlayerPosLook)event.getPacket()).getX() - MC.thePlayer.posX;
			double y = ((S08PacketPlayerPosLook)event.getPacket()).getY() - MC.thePlayer.posY;
			double z = ((S08PacketPlayerPosLook)event.getPacket()).getZ() - MC.thePlayer.posZ;
			double diff = Math.sqrt(x * x + y * y + z * z);
			event.setCancelled(true);
			if (diff <= 0.5D) {
				MC.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C06PacketPlayerPosLook((
						(S08PacketPlayerPosLook)event.getPacket()).getX(), ((S08PacketPlayerPosLook)event.getPacket()).getY(), (
								(S08PacketPlayerPosLook)event.getPacket()).getZ(), ((S08PacketPlayerPosLook)event.getPacket()).getYaw(), (
										(S08PacketPlayerPosLook)event.getPacket()).getPitch(), false));
				event.setCancelled(true);
			} 

		}
	}
}
