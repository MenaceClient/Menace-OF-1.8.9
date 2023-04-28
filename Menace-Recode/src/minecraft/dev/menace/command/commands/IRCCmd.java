package dev.menace.command.commands;

import dev.menace.Menace;
import dev.menace.command.BaseCommand;
import dev.menace.utils.misc.ChatUtils;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.User;

import java.io.IOException;
import java.util.Arrays;

public class IRCCmd extends BaseCommand {
    public IRCCmd() {
        super("IRC", "Chat in the IRC", "irc <message>");
    }

    @Override
    public void call(String[] args) {
        if (args[0] != null) {

            if (args[0].equalsIgnoreCase("connect")) {
                if (Menace.instance.ircBot.isConnected()) {
                    ChatUtils.message("Already connected to IRC");
                    return;
                }
                ChatUtils.message("Connecting to IRC (This may take a second)...");
                new Thread(() -> {
                    try {
                        Menace.instance.ircBot.reconnect();
                        Menace.instance.ircBot.joinChannel("#MenaceIRC5573");
                    } catch (IOException | IrcException e) {
                        throw new RuntimeException(e);
                    }
                    ChatUtils.message("Connected to IRC");
                }).start();
                return;
            } else if (args[0].equalsIgnoreCase("disconnect")) {
                Menace.instance.ircBot.quitServer("Disconnected by user");
                ChatUtils.message("Disconnected from the IRC");
                return;
            } else if (args[0].equalsIgnoreCase("online")) {
                StringBuilder sb = new StringBuilder();
                for (User user : Menace.instance.ircBot.getUsers("#MenaceIRC5573")) {
                    sb.append(user.getNick()).append(", ");
                }
                sb.delete(sb.length() - 2, sb.length());
                ChatUtils.message("Online: " + sb);
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (String s : args) {
                sb.append(s).append(" ");
            }

            Menace.instance.ircBot.sendMessage(sb.toString());
        }
    }
}
