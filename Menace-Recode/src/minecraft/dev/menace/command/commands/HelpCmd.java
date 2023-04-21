package dev.menace.command.commands;

import dev.menace.Menace;
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
            int i = 0;
            while (i < cmd.getSyntax().length) {
                sb.append(Menace.instance.cmdManager.prefix);
                sb.append(cmd.getSyntax()[i]);
                if (i != cmd.getSyntax().length - 1)
                    sb.append(" - ");
                i++;
            }
            ChatUtils.noPrefix("§5" + cmd.getCmd()+ " - §r" + cmd.getDescription() + " - {" + sb + "}");
        }
        ChatUtils.message("===Help===");
    }
}
