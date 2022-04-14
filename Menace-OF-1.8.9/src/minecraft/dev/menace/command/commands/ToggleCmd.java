package dev.menace.command.commands;

import dev.menace.Menace;
import dev.menace.command.CmdException;
import dev.menace.command.Command;
import dev.menace.utils.misc.ChatUtils;

public class ToggleCmd extends Command {

	public ToggleCmd() {
		super("toggle-t", "toggles a module", "toggle <module>");
	}

	@Override
	public void call(String[] args) throws CmdException {
		System.out.println(args[0]);
		
		Menace.instance.moduleManager
		.getModules()
		.stream()
		.filter(module -> args[0].equalsIgnoreCase(module.getName())).forEach(module -> module.toggle());
		
		ChatUtils.message("Toggled: " + args[0]);
	}

}
