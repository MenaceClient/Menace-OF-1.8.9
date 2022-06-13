package dev.menace.command.commands;

import java.io.File;

import dev.menace.Menace;
import dev.menace.command.BaseCommand;
import dev.menace.utils.file.FileManager;
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
			if (!(new File(FileManager.getConfigFolder(), args[1] + ".json").exists())) {
				ChatUtils.message("File: " + args[1] + "not found.");
				return;
			}
			Menace.instance.moduleManager.loadModules(args[1]);
			ChatUtils.message("Successfully loaded " + args[1] + ".");
			
		} else if (args[0].equalsIgnoreCase("none")) {
			Menace.instance.moduleManager.modules.stream().forEach(module -> {
				if (module.isToggled()) {
					module.toggle();
				}
				
				module.setKeybindNoSave(0);
				
			});
		}
	}
	

}
