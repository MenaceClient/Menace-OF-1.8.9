package dev.menace.module.modules.misc;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventTeleport;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class ExitFlagModule extends BaseModule {

    private int exitFlagCount;
    MSTimer timer = new MSTimer();
    public ExitFlagModule() {
        super("ExitFlag", "Leaves the game you are in if you flag too much", Category.MISC, 0);
    }

    @Override
    public void onEnable() {
        exitFlagCount = 0;
        super.onEnable();
    }

    @EventTarget
    public void onTeleport(EventTeleport event) {
        exitFlagCount = 0;
        timer.reset();
    }

    @EventTarget
    public void onRecievePacket(EventReceivePacket event) {
        if(timer.hasTimePassed(500)) {
            if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                exitFlagCount++;

                if (exitFlagCount >= 10) {
                    mc.thePlayer.sendChatMessage("/leave");
                    ChatUtils.message("You were flagging too much, so we sent you back to the lobby.");
                    exitFlagCount = 0;
                    timer.reset();
                }
            }
        }
    }

}