package dev.menace.module.modules.combat;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.*;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;

public class VelocityModule extends BaseModule {

	ListSetting mode;
	ToggleSetting explosions;
	SliderSetting horizontal;
	SliderSetting vertical;
	ToggleSetting smooth;
	SliderSetting hurtTime;

	//Reverse
	Queue<Double> motionX = new ConcurrentLinkedDeque<>();
	Queue<Double> motionY = new ConcurrentLinkedDeque<>();
	Queue<Double> motionZ = new ConcurrentLinkedDeque<>();
	
	public VelocityModule() {
		super("Velocity", "Edits your knockback", Category.COMBAT, 0);
	}
	
	@Override
	public void setup() {
		mode = new ListSetting("Mode", true, "Simple", new String[] {"Simple", "Matrix", "Vulcan", "Clip", "Reverse"});
		explosions = new ToggleSetting("Explosions", true, true);
		horizontal = new SliderSetting("Horizontal", true, 0, 0, 100, true) {
			@Override
			public void constantCheck() {
				this.setVisible(VelocityModule.this.mode.getValue().equalsIgnoreCase("Simple") || VelocityModule.this.mode.getValue().equalsIgnoreCase("Vulcan"));
				super.constantCheck();
			}
		};
		vertical = new SliderSetting("Vertical", true, 0, 0, 100, true) {
			@Override
			public void constantCheck() {
				this.setVisible(VelocityModule.this.mode.getValue().equalsIgnoreCase("Simple") || VelocityModule.this.mode.getValue().equalsIgnoreCase("Vulcan"));
				super.constantCheck();
			}
		};
		smooth = new ToggleSetting("Smooth", true, true) {
			@Override
			public void constantCheck() {
				this.setVisible(VelocityModule.this.mode.getValue().equalsIgnoreCase("Reverse"));
				super.constantCheck();
			}
		};
		hurtTime = new SliderSetting("HurtTime", true, 10, 0, 20, true) {
			@Override
			public void constantCheck() {
				this.setVisible(VelocityModule.this.mode.getValue().equalsIgnoreCase("Reverse"));
				super.constantCheck();
			}
		};
		this.rSetting(mode);
		this.rSetting(explosions);
		this.rSetting(horizontal);
		this.rSetting(vertical);
		this.rSetting(smooth);
		this.rSetting(hurtTime);
		super.setup();
	}

	@Override
	public void onEnable() {
		motionX.clear();
		motionY.clear();
		motionZ.clear();
		super.onEnable();
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		this.setDisplayName(mode.getValue());

		if (mode.getValue().equalsIgnoreCase("Matrix")) {
			if (mc.thePlayer.hurtTime > 0) {
				mc.thePlayer.motionX *= 0.6;
				mc.thePlayer.motionZ *= 0.6;
			}
		} else if (mode.getValue().equalsIgnoreCase("Clip")) {
			if (mc.thePlayer.hurtTime > 0) {
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.3, mc.thePlayer.posZ);
				mc.thePlayer.motionX = 0;
				mc.thePlayer.motionY = 0;
				mc.thePlayer.motionZ = 0;
			}
		} else if (mode.getValue().equalsIgnoreCase("Reverse")) {
			if (mc.thePlayer.hurtTime > 0 && mc.thePlayer.hurtTime < hurtTime.getValue()) {
				motionX.add(mc.thePlayer.motionX);
				motionY.add(mc.thePlayer.motionY);
				motionZ.add(mc.thePlayer.motionZ);
			} else {
				if (!motionX.isEmpty()) {
					mc.thePlayer.motionX = -motionX.poll();
				}
				if (!motionY.isEmpty()) {
					if (smooth.getValue()) {
						mc.thePlayer.motionY = (motionY.peek() > 0) ? -motionY.poll() : motionY.poll();
					} else {
						mc.thePlayer.motionY = -motionY.poll();
					}
				}
				if (!motionZ.isEmpty()) {
					mc.thePlayer.motionZ = -motionZ.poll();
				}
			}
		}
	}

	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		if (mode.getValue().equalsIgnoreCase("Vulcan")) {
			if (event.getPacket() instanceof C0FPacketConfirmTransaction && mc.thePlayer.hurtTime > 0) {
				event.cancel();
			}
		}
	}

	@EventTarget
	public void onRecievePacket(EventReceivePacket event) {
		if (event.getPacket() instanceof S12PacketEntityVelocity) {

			S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
			if (mc.theWorld.getEntityByID(packet.getEntityID()) == null || !mc.theWorld.getEntityByID(packet.getEntityID()).equals(mc.thePlayer)) {return;}

			if (mode.getValue().equalsIgnoreCase("Simple") || mode.getValue().equalsIgnoreCase("Vulcan")) {

				if (horizontal.getValue() == 0 && vertical.getValue() == 0) {
					event.cancel();
				} else {
					packet.motionX = (int) (packet.motionX * (horizontal.getValue() / 100));
					packet.motionY = (int) (packet.motionY * (vertical.getValue() / 100));
					packet.motionZ = (int) (packet.motionZ * (horizontal.getValue() / 100));
				}

			}
		}
	}

	@EventTarget
	public void onExplosion(EventExplosion event) {
		//Use explosion event so we dont cancel the packet
		if (explosions.getValue()) {
			if (mode.getValue().equalsIgnoreCase("Simple") || mode.getValue().equalsIgnoreCase("Vulcan")) {
				if (horizontal.getValue() == 0 && vertical.getValue() == 0) {
					event.cancel();
				} else {
					event.setMotionX((int) (event.getMotionX() * (horizontal.getValue() / 100)));
					event.setMotionY((int) (event.getMotionY() * (vertical.getValue() / 100)));
					event.setMotionZ((int) (event.getMotionZ() * (horizontal.getValue() / 100)));
				}
			}
		}
	}

}
