package dev.menace.module.modules.player;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.player.PacketUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class BlinkModule extends BaseModule {

	private final Queue<Packet<?>> packets = new ConcurrentLinkedDeque<>();
	public EntityOtherPlayerMP fp;
	
	ToggleSetting fakePlayer;
	public ToggleSetting pulse;
	SliderSetting pulseAmount;
	
	public BlinkModule() {
		super("Blink", Category.PLAYER, 0);
	}
	
	@Override
	public void setup() {
		fakePlayer = new ToggleSetting("FakePlayer", true, true);
		pulse = new ToggleSetting("Pulse", true, false);
		pulseAmount = new SliderSetting("PulseAmount", false, 50, 1, 100, 10, true) {
			@Override
			public void constantCheck() {
				if (Menace.instance.moduleManager.blinkModule.pulse.getValue()) {
					this.setVisible(true);
				} else {
					this.setVisible(false);
				}
				super.constantCheck();
			}
		};
		this.rSetting(fakePlayer);
		this.rSetting(pulse);
		this.rSetting(pulseAmount);
		super.setup();
	}
	
	@Override
	public void onEnable() {
		if (fakePlayer.getValue()) {
            fp = new EntityOtherPlayerMP(MC.theWorld, MC.thePlayer.gameProfile);
            fp.clonePlayer(MC.thePlayer, true);
            fp.copyLocationAndAnglesFrom(MC.thePlayer);
            fp.rotationYawHead = MC.thePlayer.rotationYawHead;
            MC.theWorld.addEntityToWorld(-1337, fp);
        }
		super.onEnable();
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		this.setDisplayName("Packets: " + packets.size());
		if (packets.size() >= pulseAmount.getValue() && pulse.getValue()) blink();
		if (event.getPacket() instanceof C04PacketPlayerPosition || event.getPacket() instanceof C06PacketPlayerPosLook ||
				event.getPacket() instanceof C08PacketPlayerBlockPlacement ||
				event.getPacket() instanceof C0APacketAnimation ||
				event.getPacket() instanceof C0BPacketEntityAction || event.getPacket() instanceof C02PacketUseEntity ||
				event.getPacket() instanceof C03PacketPlayer) {
                event.setCancelled(true);
                packets.add(event.getPacket());
        } 
	}
	
	@Override
	public void onDisable() {
		blink();
		if (fp != null) {
            MC.theWorld.removeEntityFromWorld(fp.entityId);
            fp = null;
        }
		super.onDisable();
	}
	
	public void blink() {
		if (fp != null) {
            MC.theWorld.removeEntityFromWorld(fp.entityId);
            fp = null;
        }
		while (!packets.isEmpty()) {
			PacketUtils.sendPacketNoEvent(packets.poll());
		}
		if (fakePlayer.getValue()) {
            fp = new EntityOtherPlayerMP(MC.theWorld, MC.thePlayer.gameProfile);
            fp.clonePlayer(MC.thePlayer, true);
            fp.copyLocationAndAnglesFrom(MC.thePlayer);
            fp.rotationYawHead = MC.thePlayer.rotationYawHead;
            MC.theWorld.addEntityToWorld(-1337, fp);
        }
	}

}
