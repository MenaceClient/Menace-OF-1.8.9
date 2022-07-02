package dev.menace.module.modules.combat;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.ToggleSetting;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.jetbrains.annotations.NotNull;

public class VelocityModule extends BaseModule {

	ListSetting mode;
	ToggleSetting explosions;
	ToggleSetting onlyDamage;
	
	public VelocityModule() {
		super("Velocity", "Stop KB like a pro.", Category.COMBAT, 0);
	}
	
	@Override
	public void setup() {
		mode = new ListSetting("Mode", true, "Simple", new String[] {"Simple", "Matrix"});
		onlyDamage = new ToggleSetting("OnlyDamage", true, false) {
			@Override
			public void constantCheck() {
				this.setVisible(Menace.instance.moduleManager.velocityModule.mode.getValue().equalsIgnoreCase("Simple"));
			}
		};
		explosions = new ToggleSetting("Explosions", true, true) {
			@Override
			public void constantCheck() {
				this.setVisible(Menace.instance.moduleManager.velocityModule.mode.getValue().equalsIgnoreCase("Simple"));
			}
		};
		this.rSetting(mode);
		this.rSetting(onlyDamage);
		this.rSetting(explosions);
		super.setup();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		this.setDisplayName(mode.getValue());
		   if (MC.thePlayer.hurtTime <= 0 || !mode.getValue().equalsIgnoreCase("Matrix")) {
               return;
           } 
           if (MC.thePlayer.onGround) {
               if (MC.thePlayer.hurtTime <= 6) {
                   MC.thePlayer.motionX *= 0.700054132;
                   MC.thePlayer.motionZ *= 0.700054132;
               } 
               if (MC.thePlayer.hurtTime <= 5) {
                   MC.thePlayer.motionX *= 0.803150645;
                   MC.thePlayer.motionZ *= 0.803150645;
               }
           } else if (MC.thePlayer.hurtTime <= 10) {
               MC.thePlayer.motionX *= 0.605001;
               MC.thePlayer.motionZ *= 0.605001;
          }
           /*kill me for needing to test all those motions also made by sub0z#7605 uwu*/
	}
	
	@EventTarget
	public void onRecievePacket(@NotNull EventReceivePacket event) {
		if (event.getPacket() instanceof S12PacketEntityVelocity && mode.getValue().equalsIgnoreCase("Simple")
				&& (!onlyDamage.getValue() || MC.thePlayer.hurtTime > 0)) {
			event.cancel();
		} else if (event.getPacket() instanceof S27PacketExplosion && mode.getValue().equalsIgnoreCase("Simple")
				&& explosions.getValue() && (!onlyDamage.getValue() || MC.thePlayer.hurtTime > 0)) {
			event.cancel();
		}
	}

}
