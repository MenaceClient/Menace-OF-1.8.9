package dev.menace.command.commands;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

import dev.menace.Menace;
import dev.menace.command.BaseCommand;
import dev.menace.utils.misc.ChatUtils;

public class BindCmd extends BaseCommand {

	public BindCmd() {
		super("bind", "bind a module", "bind <module> <key>");
	}

	@Override
	public void call(String @NotNull [] args) {
		
		Menace.instance.moduleManager
		.getModules()
		.stream()
		.filter(module -> args[0].equalsIgnoreCase(module.getName())).forEach(module -> module.setKeybind(Keyboard.getKeyIndex(args[1].toUpperCase())));
		
		ChatUtils.message("Bound " + args[0] + " to " + Keyboard.getKeyName(Keyboard.getKeyIndex(args[1].toUpperCase())).toLowerCase());
	}

}
