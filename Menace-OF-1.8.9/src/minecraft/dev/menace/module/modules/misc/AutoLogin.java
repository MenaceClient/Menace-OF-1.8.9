package dev.menace.module.modules.misc;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.utils.misc.ChatUtils;
import net.minecraft.network.play.server.S02PacketChat;

public class AutoLogin extends Module {

	public AutoLogin() {
		super("AutoLogin", 0, Category.MISC);
	}
	
	@EventTarget
	public void onRecievePacket(EventReceivePacket event) {
		if (event.getPacket() instanceof S02PacketChat) {
			S02PacketChat packet = (S02PacketChat) event.getPacket();
			String chat = packet.getChatComponent().getUnformattedTextForChat();
			if (chat.contains("/register")) {
				ChatUtils.out("/register ospp009rYs5 ospp009rYs5");
			}
			else if (chat.contains("/login")) {
				ChatUtils.out("/login ospp009rYs5");
			}
		}
	}

}
