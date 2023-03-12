package dev.menace.command.commands;

import dev.menace.command.BaseCommand;
import dev.menace.command.CommandManager;
import dev.menace.utils.misc.ChatUtils;

public class HelpCmd extends BaseCommand {
    public HelpCmd() {
        super("Help", "Shows this message.", "help");
    }

    @Override
    public void call(String[] args) {
        ChatUtils.message("===Help===");
        for (BaseCommand cmd : CommandManager.cmds) {
            StringBuilder sb = new StringBuilder();
            for (String s : cmd.getSyntax()) {
                sb.append(s).append(" - ");
            }
            ChatUtils.message(cmd.getCmd()+ " - " + cmd.getDescription() + " - {" + sb + "}");
        }
        super.call(args);
    }
}
