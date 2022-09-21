package dev.menace.module.modules.combat;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
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
		mode = new ListSetting("Mode", true, "Simple", new String[] {"Simple"});
		explosions = new ToggleSetting("Explosions", true, true);
		this.rSetting(mode);
		this.rSetting(explosions);
		super.setup();
	}
	
	@EventTarget
	public void onRecievePacket(@NotNull EventReceivePacket event) {
		if (event.getPacket() instanceof S12PacketEntityVelocity && mode.getValue().equalsIgnoreCase("Simple")) {
			event.cancel();
		} else if (event.getPacket() instanceof S27PacketExplosion && mode.getValue().equalsIgnoreCase("Simple") && explosions.getValue()) {
			event.cancel();
		}
	}

}
