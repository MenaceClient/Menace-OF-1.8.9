package dev.menace.command.commands;

import java.util.concurrent.atomic.AtomicBoolean;

import dev.menace.Menace;
import dev.menace.command.BaseCommand;
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
			
		} else if (args[0].equalsIgnoreCase("load")) {
			Menace.instance.configManager.reload();
			AtomicBoolean loaded = new AtomicBoolean(false);
			Menace.instance.configManager.getConfigs().forEach(config -> {
				if (config.getName().equalsIgnoreCase(args[1])) {
					config.load();
					loaded.set(true);
					ChatUtils.message("Successfully loaded " + args[1] + ".");
				}
			});

			if (!loaded.get()) {
				ChatUtils.message("Config: " + args[1] + " not found.");
			}

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
