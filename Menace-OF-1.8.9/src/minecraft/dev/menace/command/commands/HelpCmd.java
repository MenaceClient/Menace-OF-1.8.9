package dev.menace.command.commands;

import dev.menace.Menace;
import dev.menace.command.CmdException;
import dev.menace.command.Command;
import dev.menace.utils.misc.ChatUtils;

public class HelpCmd extends Command {

	public HelpCmd() {
		super("Help", "Shows this message.", ".help");
	}

	@Override
	public void call(String[] args) throws CmdException {
		
		ChatUtils.message("---§2Help§r---");
		
		for (Command cmd : Menace.instance.commandManager.cmds) {
			
			cmd.printHelp();
			
		}
		
		ChatUtils.message("----------");
	}

}
