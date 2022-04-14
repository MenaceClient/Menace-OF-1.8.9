package dev.menace.command.commands;

import dev.menace.Menace;
import dev.menace.command.CmdException;
import dev.menace.command.Command;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventChatOutput;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.utils.misc.ChatUtils;
import net.minecraft.network.play.server.S02PacketChat;

public class AnnoyCmd extends Command {

	private boolean toggled = false;
	private String name;
	
	public AnnoyCmd() {
		super("Annoy", "Annoy a player of your choice", ".annoy <player>");
	}

	@Override
	public void call(String[] args) throws CmdException {
		
		if (!toggled) {
			name = args[0];
			Menace.instance.eventManager.register(this);
			ChatUtils.message("Now annoying " + args[0]);
			toggled = !toggled;
		} else {
			name = "";
			Menace.instance.eventManager.unregister(this);
			toggled = !toggled;
		}
	}
	
	@EventTarget
	public void onChat(EventReceivePacket event) {
		if (!(event.getPacket() instanceof S02PacketChat)) return;
		S02PacketChat chat = (S02PacketChat) event.getPacket();
		
		if (chat.getChatComponent().getFormattedText().startsWith("<" + name + ">")) {
			ChatUtils.out(chat.getChatComponent().getFormattedText().replace("<" + name + ">", ""));
		}
	}
	
}
