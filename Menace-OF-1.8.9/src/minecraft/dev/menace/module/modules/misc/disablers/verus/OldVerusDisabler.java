package dev.menace.module.modules.misc.disablers.verus;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.modules.misc.disablers.DisablerBase;
import dev.menace.utils.entity.self.PlayerUtils;
import dev.menace.utils.misc.MSTimer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class OldVerusDisabler extends DisablerBase {
	
	private final Queue<Packet<?>> packetQueue = new ConcurrentLinkedDeque<>();
    private final MSTimer timer = new MSTimer();
    private boolean expectedTeleport;
    
    @Override
    public void onEnable() {
        this.packetQueue.clear();
        this.timer.reset();
        this.expectedTeleport = false;
    }
    
	@Override
	public void onDisable() {
		while (!packetQueue.isEmpty()) {
			MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(packetQueue.poll());
		}
	}
	
    @Override
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook && this.expectedTeleport) {
            S08PacketPlayerPosLook s08PacketPlayerPosLook = (S08PacketPlayerPosLook) event.getPacket();
            this.expectedTeleport = false;

            event.setCancelled(true);

            MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer
                    .C06PacketPlayerPosLook(s08PacketPlayerPosLook.getX(),
                    s08PacketPlayerPosLook.getY(),
                    s08PacketPlayerPosLook.getZ(),
                    s08PacketPlayerPosLook.getYaw(),
                    s08PacketPlayerPosLook.getPitch(), true));
        }
    }
    
    @Override
    public void onUpdate() {

        if (!this.shouldRun()) {
            this.expectedTeleport = false;
            this.timer.reset();
            this.packetQueue.clear();
            return;
        }

        if (this.timer.hasTimePassed(250L)) {
            this.timer.reset();

            if (!this.packetQueue.isEmpty()) {
               MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(this.packetQueue.poll());
            }
        }
    }
    
    @Override
    public void onSendPacket(EventSendPacket event) {

        if (!this.shouldRun()) return;

        if (event.getPacket() instanceof C0FPacketConfirmTransaction) {

            short action = -1;

            if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
                action = ((C0FPacketConfirmTransaction) event.getPacket()).getUid();
            }
            
            if (action != -1 && this.isInventory(action)) return;

            event.setCancelled(true);
            this.packetQueue.add(event.getPacket());
        }

        if (event.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer c03PacketPlayer = (C03PacketPlayer) event.getPacket();


            if (MC.thePlayer.ticksExisted % 25 == 0) {
                this.expectedTeleport = true;

                c03PacketPlayer.setMoving(false);
                c03PacketPlayer.setY(-0.015625);
                c03PacketPlayer.setOnGround(false);
            }
        }
        
        if (event.getPacket() instanceof C00PacketKeepAlive) {
        	event.setCancelled(true);
            this.packetQueue.add(event.getPacket());
        }
        
        if (event.getPacket() instanceof C0BPacketEntityAction) {
        	C0BPacketEntityAction paket = (C0BPacketEntityAction) event.getPacket();
        	paket.setAction(C0BPacketEntityAction.Action.RIDING_JUMP);
        	paket.setAuxData(-1);
        }
    }
    
    boolean shouldRun() {
        return MC.thePlayer != null && MC.thePlayer.ticksExisted > 5;
    }

    boolean isInventory(short action) {
        return action > 0 && action < 100;
    }
}
