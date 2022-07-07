package dev.menace.module.modules.combat;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.player.PacketUtils;
import net.minecraft.network.play.client.C02PacketUseEntity;
import org.jetbrains.annotations.NotNull;

public class ComboOneTapModule extends BaseModule {

    public ComboOneTapModule() {
        super("ComboOneTap", Category.COMBAT, 0);
    }

    @EventTarget
    public void onSendPacket(@NotNull EventSendPacket event) {
        if (event.getPacket() instanceof C02PacketUseEntity) {
            C02PacketUseEntity packet = (C02PacketUseEntity)event.getPacket();
            if(packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
                for (int i = 0; i < 75; i++) {
                    PacketUtils.sendPacketNoEvent(packet);
                }
            }
        }
    }

}
