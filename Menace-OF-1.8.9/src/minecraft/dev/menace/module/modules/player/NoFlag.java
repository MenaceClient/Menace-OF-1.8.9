package dev.menace.module.modules.player;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.BoolSetting;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoFlag extends Module {

	S08PacketPlayerPosLook s08;
	
	//Settings
	BoolSetting c03;
	
	public NoFlag() {
		super("FlagFold", 0, Category.PLAYER);
	}
	
	@Override
	public void setup() {
		c03 = new BoolSetting("C03", this, true);
		this.rSetting(c03);
	}
	
	@EventTarget
	public void onRecievePacket(EventReceivePacket event) {
		
		if (MC.theWorld == null || MC.isSingleplayer()) return;
		
        if (event.getPacket() instanceof S08PacketPlayerPosLook && !(MC.currentScreen instanceof GuiChest)) {
            S08PacketPlayerPosLook s08PacketPlayerPosLook = (S08PacketPlayerPosLook) event.getPacket();

            s08 = s08PacketPlayerPosLook;
            
            event.setCancelled(true);
            
            if (c03.isChecked()) {
            	MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer
            			.C06PacketPlayerPosLook(s08PacketPlayerPosLook.getX(),
            					s08PacketPlayerPosLook.getY(),
            					s08PacketPlayerPosLook.getZ(),
            					s08PacketPlayerPosLook.getYaw(),
            					s08PacketPlayerPosLook.getPitch(), true));
            } 
        }
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		
		if (s08 != null) {
			MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer
					.C06PacketPlayerPosLook(s08.getX(),
							s08.getY(),
							s08.getZ(),
							s08.getYaw(),
							s08.getPitch(), false));
		}
		
	}

}
