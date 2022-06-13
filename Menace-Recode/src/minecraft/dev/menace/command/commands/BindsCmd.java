package dev.menace.command.commands;

import org.lwjgl.input.Keyboard;

import dev.menace.Menace;
import dev.menace.command.BaseCommand;
import dev.menace.module.BaseModule;
import dev.menace.utils.misc.ChatUtils;

public class BindsCmd extends BaseCommand {

	public BindsCmd() {
		super("Binds", "Lists All module binds", "");
	}

	@Override
	public void call(String[] args) {
		
		String message = null;
		int i = 0;
		for (BaseModule m : Menace.instance.moduleManager.modules) {
			i++;
			if (message == null) {
				message = "§4Binds: \n§r";
			} else if (i == Menace.instance.moduleManager.modules.size()) {
				message = message + "§7" + m.getName() + ": §d" + Keyboard.getKeyName(m.getKeybind()).toLowerCase() + "§r";
			} else {
				message = message + "§7" + m.getName() + ": §d" + Keyboard.getKeyName(m.getKeybind()).toLowerCase() + "§r\n";
			}
		}
		
		ChatUtils.message(message);
	}

}
