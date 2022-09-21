package dev.menace.module.modules.misc;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.player.PacketUtils;
import net.minecraft.network.play.client.C03PacketPlayer;

public class DisablerModule extends BaseModule {

    public DisablerModule() {
        super("Disabler", Category.MISC, 0);
    }

    @Override
    public void onEnable() {
        this.setDisplayName("Strafe");
        super.onEnable();
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (Menace.instance.moduleManager.speedModule.isToggled()) {
            if (event.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook) {
                event.cancel();
            } else if (event.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
                event.cancel();
                C03PacketPlayer.C06PacketPlayerPosLook c06 = (C03PacketPlayer.C06PacketPlayerPosLook) event.getPacket();
                PacketUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(c06.getPositionX(), c06.getPositionY(), c06.getPositionZ(), c06.isOnGround()));
            }
        }
    }

}
