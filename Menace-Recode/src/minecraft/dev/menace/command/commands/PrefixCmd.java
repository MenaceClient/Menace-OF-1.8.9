package dev.menace.command.commands;

import dev.menace.Menace;
import dev.menace.command.BaseCommand;
import dev.menace.utils.misc.ChatUtils;

public class PrefixCmd extends BaseCommand {

	public PrefixCmd() {
		super("Prefix", "Changes the chat prefix.", "$prefix <newPrefix>");
	}

	@Override
	public void call(String[] args) {
		Menace.instance.cmdManager.prefix = args[0];
		ChatUtils.message("Set the prefix to " + args[0]);
	}
	
}
