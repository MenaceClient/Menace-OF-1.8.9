package dev.menace.module.modules.combat;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventPreMotion;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.misc.ChatUtils;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class VelocityModule extends BaseModule {

	ListSetting mode;
	ToggleSetting explosions;
	
	public VelocityModule() {
		super("Velocity", "Stop KB like a pro.", Category.COMBAT, 0);
	}
	
	@Override
	public void setup() {
		mode = new ListSetting("Mode", true, "Simple", new String[] {"Simple", "Bypass", "Matrix"});
		explosions = new ToggleSetting("Explosions", true, true);
		this.rSetting(mode);
		this.rSetting(explosions);
		super.setup();
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mode.getValue().equalsIgnoreCase("Matrix")) {
			if (mc.thePlayer.hurtTime > 0) {
				mc.thePlayer.motionX *= 0.6;
				mc.thePlayer.motionZ *= 0.6;
			}
		}
	}

	@EventTarget
	public void onRecievePacket(EventReceivePacket event) {
		if (event.getPacket() instanceof S12PacketEntityVelocity) {
			if (mode.getValue().equalsIgnoreCase("Simple")) {
				event.cancel();
			} else if (mode.getValue().equalsIgnoreCase("Bypass")) {
				S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
				packet.motionX = 0;
				packet.motionZ = 0;
			}
		} else if (event.getPacket() instanceof S27PacketExplosion && mode.getValue().equalsIgnoreCase("Simple") && explosions.getValue()) {
			event.cancel();
		}
	}

}
