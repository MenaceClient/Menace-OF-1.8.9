package dev.menace.module.modules.combat;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class VelocityModule extends BaseModule {

	ListSetting mode;
	
	public VelocityModule() {
		super("Velocity", "Stop KB like a pro.", Category.COMBAT, 0);
	}
	
	@Override
	public void setup() {
		mode = new ListSetting("Mode", true, "Simple", new String[] {"Simple", "Matrix"});
		this.rSetting(mode);
		super.setup();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
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
	public void onSendPacket(EventSendPacket event) {
		if (event.getPacket() instanceof S12PacketEntityVelocity && MC.thePlayer.hurtTime > 0 && mode.getValue().equalsIgnoreCase("Simple")) {
			event.cancel();
		}
	}

}
