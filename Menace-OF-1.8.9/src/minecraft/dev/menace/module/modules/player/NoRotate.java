package dev.menace.module.modules.player;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.module.Category;
import dev.menace.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate extends Module {

	public NoRotate() {
		super("NoRotate", 0, Category.PLAYER);
	}
	
	@EventTarget
	public void onRecievePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook playerPosLook = (S08PacketPlayerPosLook) event.getPacket();

            MC.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(playerPosLook.getYaw(), playerPosLook.getPitch(), MC.thePlayer.onGround));


            playerPosLook.setYaw(MC.thePlayer.rotationYaw);
            playerPosLook.setPitch(MC.thePlayer.rotationPitch);

        }
	}

}
