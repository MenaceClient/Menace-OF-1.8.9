package dev.menace.command.commands;

import dev.menace.Menace;
import dev.menace.command.CmdException;
import dev.menace.command.Command;
import dev.menace.module.Module;
import dev.menace.module.settings.BoolSetting;
import dev.menace.module.settings.DoubleSetting;
import dev.menace.module.settings.Setting;
import dev.menace.module.settings.StringSetting;
import dev.menace.utils.misc.ChatUtils;

public class SettingCmd extends Command {

	public SettingCmd() {
		super("Setting-Set", "Changes settings for a module", ".Setting <Module> <Setting> <value>");
	}

	@Override
	public void call(String[] args) throws CmdException {

		if (args[0].equalsIgnoreCase("save")) {
			
			for (Module m : Menace.instance.moduleManager.modules) {
				m.settingsSave();
			}
			ChatUtils.message("Successfully saved");
			
		} else if (args[0].equalsIgnoreCase("load")) {
			
			for (Module m : Menace.instance.moduleManager.modules) {
				m.settingsLoad();
			}
			ChatUtils.message("Successfully loaded");
			
		}
		
		for (Module m : Menace.instance.moduleManager.modules) {

			if (m.getName().equalsIgnoreCase(args[0])) {

				for (Setting s : m.settings) {

					if (s.getName().equalsIgnoreCase(args[1].replace("_", " "))) {

						if (s instanceof BoolSetting && (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false"))) {
							((BoolSetting)s).setChecked(Boolean.valueOf(args[2]));

							ChatUtils.message("Set " + s.getName() + " to " + args[2]);

							return;
						}

						if (s instanceof DoubleSetting && args[2] != null) {
							((DoubleSetting)s).setValue(Double.valueOf(args[2]));

							ChatUtils.message("Set " + s.getName() + " to " + args[2]);

							return;
						}

						if (s instanceof StringSetting) {

							for (String o : ((StringSetting)s).getOptions()) {
								if (args[2].replace("_", " ").equalsIgnoreCase(o)) {
									((StringSetting)s).setString(args[2]);

									ChatUtils.message("Set " + s.getName() + " to " + args[2]);

									return;
								}
							}
						}
					}

				}

				ChatUtils.error("Cannot find the setting " + args[1]);

			}
		}
	}
}
