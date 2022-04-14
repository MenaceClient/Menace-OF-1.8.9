package dev.menace.command.commands;

import dev.menace.Menace;
import dev.menace.command.CmdException;
import dev.menace.command.Command;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventChatOutput;
import dev.menace.event.events.EventUpdate;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.MSTimer;

public class SpamCmd extends Command {

	private boolean toggled = false;
	private String message = "";
	private long delay = 1000L;
	private int setup = -1;
	private MSTimer timer = new MSTimer();
	private int antispam = 0;
	
	public SpamCmd() {
		super("Spammer-spam", "Spam something in chat.", ".spam start", ".spam stop");
	}

	@Override
	public void call(String[] args) throws CmdException {
		
		if (args[0].equalsIgnoreCase("start") && !toggled) {
			ChatUtils.message("What would you like to spam? (Reply in chat)");
			Menace.instance.eventManager.register(this);
			setup = 0;
		}
		
		if (args[0].equalsIgnoreCase("stop") && toggled) {
			ChatUtils.message("Spammer Stopped.");
			Menace.instance.eventManager.unregister(this);
			toggled = false;
			message = "";
			delay = 1000L;
			setup = -1;
			antispam = 0;
		}
	}
	
	@EventTarget
	public void onSendChatMessage(EventChatOutput event) {
		if (toggled) return;
		if (setup == 0 && !event.getMessage().equalsIgnoreCase(".spam start")) {
			message = event.getMessage();
			event.setCancelled(true);
			ChatUtils.message(event.getMessage() + " ok");
			ChatUtils.message("Delay? (Reply in chat, in MS)");
			setup++;
		} else if (setup == 1) {
			delay = Long.parseLong(event.getMessage());
			event.setCancelled(true);
			ChatUtils.message(event.getMessage() + " ok");
			timer.reset();
			toggled = true;
		}
		
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (!toggled || !timer.hasTimePassed(delay)) return;
		
		ChatUtils.out("[" + String.valueOf(antispam) + "]" + message);
		antispam++;
		timer.reset();
		
	}

}
