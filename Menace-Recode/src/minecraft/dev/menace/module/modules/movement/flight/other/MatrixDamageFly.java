package dev.menace.module.modules.movement.flight.other;

import dev.menace.Menace;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.module.modules.movement.flight.FlightBase;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.timer.CustomTimer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class MatrixDamageFly extends FlightBase {

    @Override
    public void onUpdate() {

        if(mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown()) {
            MovementUtils.strafe(0.00026f);
        }
        mc.thePlayer.motionY = 0;
        //PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + mc.thePlayer.motionX, mc.thePlayer.posY + (mc.gameSettings.keyBindJump.isKeyDown() ? 0.0624 : 0) - (mc.gameSettings.keyBindSneak.isKeyDown() ? 0.0624 : 0), mc.thePlayer.posZ + mc.thePlayer.motionZ, false));
        PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX + mc.thePlayer.motionX, mc.thePlayer.posY - 42069, mc.thePlayer.posZ + mc.thePlayer.motionZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
    }

}
