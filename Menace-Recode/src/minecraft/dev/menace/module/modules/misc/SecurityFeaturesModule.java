package dev.menace.module.modules.misc;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.ChatComponentText;

public class SecurityFeaturesModule extends BaseModule {


    public SecurityFeaturesModule() {
        super("SecurityFeatures", Category.MISC, 0);
    }

    @EventTarget
    public void onRecievePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = (S02PacketChat) event.getPacket();
            packet.setChatComponent(new ChatComponentText(packet.getChatComponent().getFormattedText().replaceAll(mc.thePlayer.getName(), Menace.instance.user.getUsername())));
        } else if (event.getPacket() instanceof S45PacketTitle) {
            S45PacketTitle packet = (S45PacketTitle) event.getPacket();
            packet.setMessage(new ChatComponentText(packet.getMessage().getFormattedText().replaceAll(mc.thePlayer.getName(), Menace.instance.user.getUsername())));
        }
    }

}
