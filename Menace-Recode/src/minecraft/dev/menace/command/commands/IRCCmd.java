package dev.menace.command.commands;

import dev.menace.Menace;
import dev.menace.command.BaseCommand;
import dev.menace.utils.misc.ChatUtils;

public class IRCCmd extends BaseCommand {
    public IRCCmd() {
        super("irc", "Chat in the IRC", "irc <message>");
    }

    @Override
    public void call(String[] args) {
        if (args[0] != null) {
            StringBuilder sb = new StringBuilder();
            for (String s : args) {
                sb.append(s);
            }

            Menace.instance.irc.sendMsg(sb.toString());
        }
    }
}
