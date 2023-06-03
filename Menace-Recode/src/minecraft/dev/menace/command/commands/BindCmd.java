package dev.menace.command.commands;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

import dev.menace.Menace;
import dev.menace.command.BaseCommand;
import dev.menace.utils.misc.ChatUtils;

import java.util.concurrent.atomic.AtomicBoolean;

public class BindCmd extends BaseCommand {

	public BindCmd() {
		super("Bind", "bind a module", "bind <module> <key>");
	}

	@Override
	public void call(String[] args) {

		AtomicBoolean found = new AtomicBoolean(false);

		Menace.instance.moduleManager
		.getModules()
		.stream()
		.filter(module -> args[0].equalsIgnoreCase(module.getName())).forEach(module -> {
			found.set(true);
			module.setKeybind(Keyboard.getKeyIndex(args[1].toUpperCase()));
		});

		if (found.get()) {
			ChatUtils.message("Bound " + args[0] + " to " + Keyboard.getKeyName(Keyboard.getKeyIndex(args[1].toUpperCase())).toUpperCase() + ".");
		} else {
			ChatUtils.message("Module not found!");
		}
	}

}
