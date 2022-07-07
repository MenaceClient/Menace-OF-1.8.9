package dev.menace.module.modules.movement.flight.other;

import dev.menace.Menace;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.module.modules.movement.flight.FlightBase;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.timer.CustomTimer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class MatrixDamageFly extends FlightBase {

    private boolean veloPacket;
    private double veloY;
    private final CustomTimer tickTimer = new CustomTimer();

    @Override
    public void onEnable() {
        ChatUtils.message("Waiting for damage...");
        veloPacket = false;
        veloY = 0;
        tickTimer.reset();
    }

    @Override
    public void onUpdate() {
        if (veloPacket) {
            double yaw = Math.toRadians(MC.thePlayer.rotationYaw);
            MC.thePlayer.motionX += (-Math.sin(yaw) * 0.416);
            MC.thePlayer.motionZ += (Math.cos(yaw) * 0.416);
            MC.thePlayer.motionY = veloY;
            tickTimer.update();
            if (tickTimer.hasTimePassed(27)) {
                Menace.instance.moduleManager.flightModule.toggle();
            }
        }
    }

    @Override
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S12PacketEntityVelocity && MC.theWorld.getEntityByID(((S12PacketEntityVelocity)event.getPacket()).getEntityID()) == MC.thePlayer) {
            if (((S12PacketEntityVelocity)event.getPacket()).motionY / 8000.0 > 0.2) {
                veloPacket = true;
                veloY = ((S12PacketEntityVelocity)event.getPacket()).motionY / 8000.0;
            }
        }
    }
}
