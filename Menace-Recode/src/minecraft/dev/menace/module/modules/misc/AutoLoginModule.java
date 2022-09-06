package dev.menace.module.modules.misc;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.network.play.server.S02PacketChat;
import org.jetbrains.annotations.NotNull;

public class AutoLoginModule extends BaseModule {
    public AutoLoginModule() {
        super("AutoLogin", Category.MISC, 0);
    }

    @EventTarget
    public void onRecievePacket(@NotNull EventReceivePacket event) {
        if (event.getPacket() instanceof S02PacketChat) {
            String message = ((S02PacketChat)event.getPacket()).getChatComponent().getUnformattedText();
            if (message.contains("/register <password> <password>")) {
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        mc.thePlayer.sendChatMessage("/register password password");
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            } else if (message.contains("/login <password>")) {
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        mc.thePlayer.sendChatMessage("/login password");
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

}
