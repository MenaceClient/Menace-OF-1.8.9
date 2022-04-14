package dev.menace.module.modules.combat;

import java.util.ArrayList;
import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventJump;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.DoubleSetting;
import dev.menace.module.settings.StringSetting;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class Velocity extends Module {

	private boolean velocityInput;
	
	//Settings
	StringSetting mode;
	DoubleSetting veloX;
	DoubleSetting veloY;
	
	
	public Velocity() {
		super("Velocity", 0, Category.COMBAT);
	}
	
	@Override
	public void setup() {
		ArrayList<String> op = new ArrayList<String>();
		op.add("Simple");
		op.add("AAC");
		mode = new StringSetting("Mode", this, "Simple", op);
		veloX = new DoubleSetting("Velocity X", this, 0, 0, 10);
		veloY = new DoubleSetting("Velocity Y", this, 0, 0, 10);
		this.rSetting(mode);
		this.rSetting(veloX);
		this.rSetting(veloY);
	}
	
	@Override
	public void onEnable() {
		velocityInput = false;
		super.onEnable();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		
		if (mode.getString().equalsIgnoreCase("AAC")) {
            if (MC.thePlayer.hurtTime > 0) {
                if (!velocityInput || MC.thePlayer.onGround || MC.thePlayer.fallDistance > 2F) {
                    return;
                }

                MC.thePlayer.addVelocity(0.0, -1.0, 0.0);
                MC.thePlayer.onGround = true;
            } else {
                velocityInput = false;
            }
        }
	}
	
	@EventTarget
	public void onRecievePacket(EventReceivePacket event) {
		if (event.getPacket() instanceof S12PacketEntityVelocity) {
			
			if (mode.getString().equalsIgnoreCase("AAC")) {
				velocityInput = true;
				return;
			}
			
			S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
			
			double velX = veloX.getValue();
			double velY = veloY.getValue();
			
			if (velX == 0 && velY == 0) {
                event.setCancelled(true);
				//packet.motionX = 0;
	            //packet.motionY = 0;
	            //packet.motionZ = 0;
                return;
			}
			
			
			packet.motionX = (int) (packet.getMotionX() * velX);
            packet.motionY = (int) (packet.getMotionY() * velY);
            packet.motionZ = (int) (packet.getMotionZ() * velX);
		}
	}

	@EventTarget
	public void onJump(EventJump event) {
		if (mode.getString().equalsIgnoreCase("AAC") && MC.thePlayer.hurtTime > 0) {
			event.setCancelled(true);
		}
	}
	
}
