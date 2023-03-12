package dev.menace.module.modules.world;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import net.minecraft.network.play.server.S02PacketChat;

public class PhaseModule extends BaseModule {

    public PhaseModule() {
        super("Phase", Category.WORLD, 0);
    }

    @EventTarget
    public void onRecievePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = (S02PacketChat) event.getPacket();
            if (packet.getChatComponent().getUnformattedText().contains("Game started!")) {
                //unblink
            }
        }
    }

}
