package dev.menace.utils.irc;

import dev.menace.Menace;
import dev.menace.utils.misc.ChatUtils;
import org.jibble.pircbot.PircBot;

public class IRCUtils extends PircBot
{
    public IRCUtils() {
        this.setName(Menace.instance.user.getUsername());
        this.setLogin("MenaceIRC");
        this.setVersion("Menace IRC Client v2.0 - " + Menace.instance.user.getUsername());
    }

    public void sendMessage(String message) {
        ChatUtils.irc("§a" + this.getName() + ":§b " + message);
        super.sendMessage("#MenaceIRC5573", message);
    }

    @Override
    public void onMessage(final String channel, final String sender, final String login, final String hostname, final String message) {
        ChatUtils.irc("§a" + sender + ":§b " + message);
        super.onMessage(channel, sender, login, hostname, message);
    }

    @Override
    protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        //ChatUtils.message("You have been disconnected from the IRC server.");
        super.onQuit(sourceNick, sourceLogin, sourceHostname, reason);
    }
}