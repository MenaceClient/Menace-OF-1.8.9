package dev.menace.module.modules.misc;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.TranslatorUtils;
import net.minecraft.network.play.server.S02PacketChat;

public class TranslatorModule extends BaseModule {

    public TranslatorModule() {
        super("Translator", Category.MISC, 0);
    }

    @EventTarget
    public void onReceive(EventReceivePacket event) {
        if (event.getPacket() instanceof S02PacketChat) {
            String message = ((S02PacketChat) event.getPacket()).getChatComponent().getUnformattedText();

            //Check for non-ascii
            if (message != null && !message.matches("\\A\\p{ASCII}*\\z") && !message.contains("§") && !message.contains("|")) {
                ChatUtils.message("Translated: " + TranslatorUtils.translate(message));
            }
        }
    }

}
