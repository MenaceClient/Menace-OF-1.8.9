package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.utils.misc.ChatUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public class VerusTP extends Module {

	double x;
	double y;
	double z;
	double tpX;
	double tpY;
	double tpZ;
	boolean allowFlying;
	boolean noClip;
	EntityOtherPlayerMP fakePlayer;
	
	public VerusTP() {
		super("VerusTP", 0, Category.MOVEMENT);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		
        x = MC.thePlayer.posX;
        y = MC.thePlayer.posY;
        z = MC.thePlayer.posZ;
        allowFlying = MC.thePlayer.capabilities.allowFlying;
		noClip = MC.thePlayer.noClip;
		
		fakePlayer = new EntityOtherPlayerMP(MC.theWorld, MC.thePlayer.gameProfile);
        fakePlayer.clonePlayer(MC.thePlayer, true);
        fakePlayer.copyLocationAndAnglesFrom(MC.thePlayer);
        fakePlayer.rotationYawHead = MC.thePlayer.rotationYawHead;
        MC.theWorld.addEntityToWorld(-1337, fakePlayer);
	}
	
	@EventTarget
	public void onUpdate() {
		MC.thePlayer.capabilities.allowFlying = true;
		MC.thePlayer.noClip = true;
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		
		if (event.getPacket() instanceof C03PacketPlayer) {
			//MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
			event.setCancelled(true);
		}
		
	}
	
	@EventTarget
    public boolean onCollision(EventCollide event) {
		if (MC.thePlayer.capabilities.isFlying) {
			event.setBoundingBox(null);
		}
		return true;
	}
	
	@Override
	public void onDisable() {
		
        tpX = MC.thePlayer.posX;
        tpY = MC.thePlayer.posY;
        tpZ = MC.thePlayer.posZ;
		
		super.onDisable();
		
		MC.thePlayer.capabilities.allowFlying = allowFlying;
		MC.thePlayer.noClip = noClip;
		
		if (fakePlayer != null) {
            MC.theWorld.removeEntityFromWorld(fakePlayer.entityId);
            fakePlayer = null;
        }
		
		ChatUtils.message("Teleporting!");
		/*
		MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(x, y + 0.5, z, true));
		MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(x, y + 1, z, true));
		MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(x, y + 1.5, z, true));
		MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(x, y + 2, z, true));
		MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(x, y + 2.5, z, true));
		MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(x, y + 3, z, true));
		MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(x, y + 3.25, z, true));
		MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(x, y, z, false));
		MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(x, y, z, true));
		*/
		
		MC.thePlayer.setPosition(tpX, tpY, tpZ);
		
	}

	
}
