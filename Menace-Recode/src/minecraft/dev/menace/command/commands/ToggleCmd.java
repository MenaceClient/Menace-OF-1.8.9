package dev.menace.command.commands;

import dev.menace.Menace;
import dev.menace.command.BaseCommand;
import dev.menace.module.BaseModule;
import dev.menace.utils.misc.ChatUtils;

public class ToggleCmd extends BaseCommand {
    public ToggleCmd() {
        super("Toggle", "Toggle a module.", "toggle <module>");
    }

    @Override
    public void call(String[] args) {
        Menace.instance.moduleManager
            .getModules()
            .stream()
            .filter(module -> args[0].equalsIgnoreCase(module.getName())).forEach(BaseModule::toggle);

        ChatUtils.message("Toggled " + args[0]);
    }

}
