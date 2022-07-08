package dev.menace.module.modules.player;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public class NoFallModule extends BaseModule {

	ListSetting mode;

	public NoFallModule() {
		super("NoFall", "Cancels fall damage", Category.PLAYER, 0);
	}

	@Override
	public void setup() {
		mode = new ListSetting("Mode", true, "SpoofGround", new String[] {"SpoofGround", "NoGround", "Verus", "Experimental", "Hypixel"});
		this.rSetting(mode);
		super.setup();
	}
	
	@EventTarget
	public void onEvent(EventUpdate e) {
		if(mode.getValue().equalsIgnoreCase("Hypixel")) {
			if (mc.thePlayer.fallDistance > 3) {
				mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer(true));
			}
			if (mc.thePlayer.fallDistance > 4 && mc.thePlayer.motionY < -0.1) {
				if (mc.thePlayer.isSprinting()) {
					mc.thePlayer.setSprinting(false);
				} else mc.thePlayer.setSprinting(true);
				mc.thePlayer.setPosition(mc.thePlayer.lastTickPosX, mc.thePlayer.posY, mc.thePlayer.lastTickPosZ);
			}
		}
	} 

	@EventTarget
	public void onCollide(EventCollide event) {
		this.setDisplayName(mode.getValue());
		
		if (mode.getValue().equalsIgnoreCase("Verus") && mc.thePlayer.fallDistance > 2.5) {
			if (Menace.instance.moduleManager.flightModule.isToggled())
				return;
			event.setBoundingBox(new AxisAlignedBB(-2, -1, -2, 2, 1, 2).offset(event.getPosX(), event.getPosY(), event.getPosZ()));

		}
	}

	@EventTarget
	public void onSendPacket(EventSendPacket event) {	
		if (mode.getValue().equalsIgnoreCase("NoGround") && event.getPacket() instanceof C03PacketPlayer && !mc.thePlayer.isInWater()) {
			((C03PacketPlayer)event.getPacket()).setOnGround(false);
		}

		if (mode.getValue().equalsIgnoreCase("SpoofGround") && event.getPacket() instanceof C03PacketPlayer && mc.thePlayer.fallDistance - mc.thePlayer.motionY > 3) {
			((C03PacketPlayer)event.getPacket()).setOnGround(true);
		}
	}

}
