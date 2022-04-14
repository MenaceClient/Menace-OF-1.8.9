package dev.menace.command.commands;

import org.lwjgl.input.Keyboard;

import dev.menace.Menace;
import dev.menace.command.CmdException;
import dev.menace.command.Command;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.ColorUtils;

public class BindsCmd extends Command {

	public BindsCmd() {
		super("Binds", "Lists All module binds", "");
	}

	@Override
	public void call(String[] args) throws CmdException {
		
		String message = null;
		int i = 0;
		for (dev.menace.module.Module m : Menace.instance.moduleManager.modules) {
			i++;
			if (message == null) {
				message = "§4Binds: \n§r";
			} else if (i == Menace.instance.moduleManager.modules.size()) {
				message = message + "§7" + m.getName() + ": §d" + Keyboard.getKeyName(m.getKey()).toLowerCase() + "§r";
			} else {
				message = message + "§7" + m.getName() + ": §d" + Keyboard.getKeyName(m.getKey()).toLowerCase() + "§r\n";
			}
		}
		
		ChatUtils.message(ColorUtils.parse(message));
	}

}
