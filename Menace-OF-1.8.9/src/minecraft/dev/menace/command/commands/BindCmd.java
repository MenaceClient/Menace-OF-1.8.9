package dev.menace.command.commands;

import org.lwjgl.input.Keyboard;

import dev.menace.Menace;
import dev.menace.command.CmdException;
import dev.menace.command.Command;
import dev.menace.utils.misc.ChatUtils;

public class BindCmd extends Command {

	public BindCmd() {
		super("bind", "bind a module", "bind <module> <key>");
	}

	@Override
	public void call(String[] args) throws CmdException {
		
		//if (args[1].length() > 1) {
			//ChatUtils.message(args[1] + " is not a single letter dumbass.");
			//return;
		//}
		
		Menace.instance.moduleManager
		.getModules()
		.stream()
		.filter(module -> args[0].equalsIgnoreCase(module.getName())).forEach(module -> module.setKey(Keyboard.getKeyIndex(args[1].toUpperCase())));
		
		ChatUtils.message("Bound " + args[0] + " to " + Keyboard.getKeyName(Keyboard.getKeyIndex(args[1].toUpperCase())).toLowerCase());
	}

}
