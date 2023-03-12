package dev.menace.command.commands;

import dev.menace.Menace;
import dev.menace.command.BaseCommand;
import dev.menace.scripting.ScriptManager;
import dev.menace.utils.misc.ChatUtils;

public class ScriptCmd extends BaseCommand {

    public ScriptCmd() {
        super("Script", "Reload your scripts", "script reload");
    }

    @Override
    public void call(String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            Menace.instance.scriptManager.reloadScripts();
            ChatUtils.message("Reloaded scripts!");
        }
    }
}
