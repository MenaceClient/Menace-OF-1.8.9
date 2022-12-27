package dev.menace.module.modules.player;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.utils.player.PacketUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public class NoFallModule extends BaseModule {

	ListSetting mode;
	boolean shouldSpoof;
	private float lastTickFallDist, fallDist;

	public NoFallModule() {
		super("NoFall", "Cancels fall damage", Category.PLAYER, 0);
	}

	@Override
	public void setup() {
		mode = new ListSetting("Mode", true, "SpoofGround", new String[] {"SpoofGround", "NoGround", "Verus", "Experimental", "Hypixel"});
		this.rSetting(mode);
		super.setup();
	}

	@Override
	public void onEnable() {
		shouldSpoof = false;
		super.onEnable();
	}

	@EventTarget
	public void onEvent(EventUpdate e) {
		if(mode.getValue().equalsIgnoreCase("Hypixel")) {
			if (mc.thePlayer.fallDistance > 3) {
				PacketUtils.sendPacket(new C03PacketPlayer(true));
				//shouldSpoof = true;
			}
			if (mc.thePlayer.fallDistance > 4 && mc.thePlayer.motionY < -0.1) {
				mc.thePlayer.setSprinting(!mc.thePlayer.isSprinting());
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
	public void onPreMotion(EventMove event) {
		if (!mode.getValue().equalsIgnoreCase("Experimental"))
			return;

		if (mc.thePlayer.fallDistance == 0)
			fallDist = 0;

		fallDist += mc.thePlayer.fallDistance - lastTickFallDist;
		lastTickFallDist = mc.thePlayer.fallDistance;

		double num = 0.015625; //0.015625

		double mathGround = Math.round(event.getY() / num) * num;

		if (fallDist > 1.3) {
			event.setX(0);
			event.setZ(0);
		}

		if (fallDist > 1.3 && mc.thePlayer.ticksExisted % 5 == 0) {
			mc.thePlayer.setPosition(mc.thePlayer.lastTickPosX, mc.thePlayer.posY + mathGround, mc.thePlayer.lastTickPosZ);
			event.setY(mathGround);

			mathGround = Math.round(event.getY() / num) * num;
			if (Math.abs(mathGround - event.getY()) < 0.01) {
				if (mc.thePlayer.motionY < -0.4) mc.thePlayer.motionY = -0.4;
				shouldSpoof = true;
			}
		}
	}

	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		if (mode.getValue().equalsIgnoreCase("NoGround") && event.getPacket() instanceof C03PacketPlayer && !mc.thePlayer.isInWater()) {
			((C03PacketPlayer)event.getPacket()).setOnGround(false);
		}

		if (event.getPacket() instanceof C03PacketPlayer && (mode.getValue().equalsIgnoreCase("SpoofGround") && mc.thePlayer.fallDistance - mc.thePlayer.motionY > 3 || shouldSpoof)) {
			((C03PacketPlayer)event.getPacket()).setOnGround(true);
			shouldSpoof = false;
		}
	}

}
