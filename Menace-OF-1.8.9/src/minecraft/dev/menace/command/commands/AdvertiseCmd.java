package dev.menace.command.commands;

import dev.menace.command.CmdException;
import dev.menace.command.Command;
import dev.menace.utils.misc.ChatUtils;

public class AdvertiseCmd extends Command {

	int antispam1 = 0;
	
	public AdvertiseCmd() {
		super("Advertise-adv", "Advertise menace client in chat!", ".advertise", ".adv");
	}

	@Override
	public void call(String[] args) throws CmdException {
		
		ChatUtils.out("[" + String.valueOf(antispam1) + "] I use Menace, and you can too! Get it at menaceclient_ml!");
		antispam1++;
		
	}

}
