package dev.menace.command.commands;

import java.util.concurrent.atomic.AtomicBoolean;

import dev.menace.Menace;
import dev.menace.command.BaseCommand;
import dev.menace.module.config.Config;
import dev.menace.utils.misc.ChatUtils;

public class ConfigCmd extends BaseCommand {

	public ConfigCmd() {
		super("Config", "Save and load your configs", ".config save <name>", ".config load <name>");
	}

	@Override
	public void call(String[] args) {
		if (args[0].equalsIgnoreCase("save")) {
			Menace.instance.moduleManager.saveModules(args[1]);
			ChatUtils.message("Successfully saved " + args[1] + ".");
			Menace.instance.configManager.reload();
		} else if (args[0].equalsIgnoreCase("load")) {
			Menace.instance.configManager.reload();
			Config cfg = Menace.instance.configManager.getConfigByName(args[1]);
			if (cfg == null) {
				ChatUtils.message("Config " + args[1] + " does not exist.");
				return;
			}
			cfg.load();
			ChatUtils.message("Loaded config " + args[1]);
		} else if (args[0].equalsIgnoreCase("none")) {
			Menace.instance.moduleManager.modules.forEach(module -> {
				if (module.isToggled()) {
					module.toggle();
				}
				
				module.setKeybindNoSave(0);
				
			});
		}
	}
	

}
