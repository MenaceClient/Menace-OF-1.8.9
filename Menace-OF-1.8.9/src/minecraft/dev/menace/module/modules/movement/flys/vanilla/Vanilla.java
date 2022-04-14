package dev.menace.module.modules.movement.flys.vanilla;

import dev.menace.Menace;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.modules.movement.flys.FlightBase;
import dev.menace.utils.entity.self.PlayerUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;

import java.awt.Color;

public class Vanilla extends FlightBase {

	 private int packets;
	
	@Override
	public void onUpdate() {
		MC.thePlayer.capabilities.isFlying = true;
		MC.thePlayer.capabilities.allowFlying = true;
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
	
	@Override
	public void onDisable() {
		MC.thePlayer.capabilities.isFlying = false;
		MC.thePlayer.capabilities.allowFlying = false;
		packets = 0;
	}
	
}
