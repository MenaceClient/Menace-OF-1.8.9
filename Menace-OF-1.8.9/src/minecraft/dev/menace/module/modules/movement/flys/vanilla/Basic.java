package dev.menace.module.modules.movement.flys.vanilla;

import dev.menace.Menace;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.modules.movement.flys.FlightBase;
import dev.menace.utils.entity.self.PlayerUtils;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Basic extends FlightBase {

	private int packets;
	
	@Override
	public void onUpdate() {
		MC.thePlayer.motionY = 0;
		
		if (MC.gameSettings.keyBindJump.isKeyDown()) {
			MC.thePlayer.motionY = 0.1; 
		}
		
		if (MC.gameSettings.keyBindSneak.isKeyDown()) {
			MC.thePlayer.motionY = -0.1; 
		}
	}
	
	@Override
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof C03PacketPlayer) {
            packets++;
            if (packets == 40 && Menace.instance.moduleManager.flightModule.vanillaKickBypass.isChecked()) {
                PlayerUtils.handleVanillaKickBypass();
                packets = 0;
            }
        }
    }
}
