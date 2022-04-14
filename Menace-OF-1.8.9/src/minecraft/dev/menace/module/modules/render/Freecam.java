package dev.menace.module.modules.render;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.Category;
import dev.menace.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Freecam extends Module {

	double x;
	double y;
	double z;
	boolean allowFlying;
	boolean noClip;
	public EntityOtherPlayerMP fakePlayer;
	
	public Freecam() {
		super("Freecam", 0, Category.RENDER);
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
			MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
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
		
		MC.thePlayer.setPosition(x, y, z);
		
		super.onDisable();
		
		MC.thePlayer.capabilities.allowFlying = allowFlying;
		MC.thePlayer.noClip = noClip;
		
		if (fakePlayer != null) {
            MC.theWorld.removeEntityFromWorld(fakePlayer.entityId);
            fakePlayer = null;
        }
	}

}
