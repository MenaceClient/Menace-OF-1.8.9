package dev.menace.module.modules.player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.swing.Timer;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.BoolSetting;
import dev.menace.module.settings.DoubleSetting;
import dev.menace.module.settings.StringSetting;
import dev.menace.utils.misc.MSTimer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;


public class Blink extends Module {
	
	EntityOtherPlayerMP fakePlayer;
	private final Queue<Packet<?>> packets = new ConcurrentLinkedDeque<>();
	private final Queue<Packet<?>> recievedPackets = new ConcurrentLinkedDeque<>();
	private int renderPackets;
	double x;
	double y;
	double z;
	
	//Settings
	BoolSetting fakePlayerSet;
	StringSetting directionSet;
	StringSetting packetsSet;
	BoolSetting cancelSet;
	BoolSetting pulseSet;
	DoubleSetting amountSet;

	public Blink() {
		super("Blink", 0, Category.PLAYER);
	}
	
	@Override
	public void setup() {
		ArrayList<String> direction = new ArrayList<>();
		ArrayList<String> packet = new ArrayList<>();
		direction.add("Client");
		direction.add("Bi-Directional");
		packet.add("Movement");
		packet.add("All");
		fakePlayerSet = new BoolSetting("FakePlayer", this, false);
		directionSet = new StringSetting("Direction", this, "Client", direction);
		packetsSet = new StringSetting("Packets", this, "Movement", packet);
		cancelSet = new BoolSetting("Cancel", this, false);
		pulseSet = new BoolSetting("Pulse", this, false);
		amountSet = new DoubleSetting("Amount", this, 50, 1, 100);
        this.rSetting(fakePlayerSet);
        this.rSetting(directionSet);
        this.rSetting(packetsSet);
        this.rSetting(cancelSet);
        this.rSetting(pulseSet);
        this.rSetting(amountSet);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
        if (MC.thePlayer == null) this.toggle();
        
        x = MC.thePlayer.posX;
        y = MC.thePlayer.posY;
        z = MC.thePlayer.posZ;

        renderPackets = 0;
        
        if (fakePlayerSet.isChecked()) {
            fakePlayer = new EntityOtherPlayerMP(MC.theWorld, MC.thePlayer.gameProfile);
            fakePlayer.clonePlayer(MC.thePlayer, true);
            fakePlayer.copyLocationAndAnglesFrom(MC.thePlayer);
            fakePlayer.rotationYawHead = MC.thePlayer.rotationYawHead;
            MC.theWorld.addEntityToWorld(-1337, fakePlayer);
        }
      
	}
	

	@EventTarget
	public void packetOut(EventSendPacket event) {
		
		this.setDisplayName("Blink §7[Packets:" + String.valueOf(renderPackets) + "]");
		
		if (packets.size() >= amountSet.getValue()
				&& pulseSet.isChecked()) {
			blink();
			return;
		}
		
		Packet packet = event.getPacket();
		
		if (packetsSet.getString().equalsIgnoreCase("All")) {
			event.setCancelled(true);
		}
		
		if (packet instanceof C03PacketPlayer) {
			event.setCancelled(true);
			
			if (!cancelSet.isChecked()) {
				MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
			}
		}
		
		
        if (packet instanceof C04PacketPlayerPosition || packet instanceof C06PacketPlayerPosLook ||
                packet instanceof C08PacketPlayerBlockPlacement ||
                packet instanceof C0APacketAnimation ||
                packet instanceof C0BPacketEntityAction || packet instanceof C02PacketUseEntity) {
                event.setCancelled(true);
                renderPackets++;
                packets.add(packet);
        } 
		
	}
	
	@EventTarget
	public void onRecievePacket(EventReceivePacket event) {
		if (!directionSet.getString().equalsIgnoreCase("Bi-Directional")) {
			return;
		}
		
		//if (Menace.instance.settingsManager.getSettingByName("Packets").getValString().equalsIgnoreCase("All")) {
			event.setCancelled(true);
			recievedPackets.add(event.getPacket());
		//}
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		
		//while (!recievedPackets.isEmpty()) {
			//EventReceivePacket event = new EventReceivePacket(recievedPackets.poll());
			//event.call();
        //}
		
		blink();
        if (fakePlayer != null) {
            MC.theWorld.removeEntityFromWorld(fakePlayer.entityId);
            fakePlayer = null;
        }
	}
	

	public void blink() {
		
        if (fakePlayer != null) {
            MC.theWorld.removeEntityFromWorld(fakePlayer.entityId);
            fakePlayer = null;
        }
        
        while (!packets.isEmpty()) {
        	MC.getNetHandler().getNetworkManager().sendPacketNoEvent(packets.poll());
        }

		renderPackets = 0;
		
        if (fakePlayerSet.isChecked()) {
            fakePlayer = new EntityOtherPlayerMP(MC.theWorld, MC.thePlayer.gameProfile);
            fakePlayer.clonePlayer(MC.thePlayer, true);
            fakePlayer.copyLocationAndAnglesFrom(MC.thePlayer);
            fakePlayer.rotationYawHead = MC.thePlayer.rotationYawHead;
            MC.theWorld.addEntityToWorld(-1337, fakePlayer);
        }
	} 
}
