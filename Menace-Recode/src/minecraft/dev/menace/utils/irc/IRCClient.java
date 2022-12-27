package dev.menace.utils.irc;

import dev.menace.Menace;
import dev.menace.utils.misc.ChatUtils;

public class IRCClient extends IRCMessageLoop {

    public IRCClient(String server, int port) {
        super(server, port);
    }

    // you have full access to PRIVMSG messages that are parsed.
    void raw(Message msg) {
        ChatUtils.irc("§a" + msg.nickname + ":§b " + msg.content);
    }

    public void sendMsg(String msg) {
        ChatUtils.irc("§a" + Menace.instance.user.getUsername() + ":§b " + msg);
        privmsg("#menace-test", msg);
    }

    public void stop() {
        quit("Disconnected");
    }

    public void start() {
        nick(Menace.instance.user.getUsername());
        user("menace", "null", "*", "Menace IRC Client v1.0 - " + Menace.instance.user.getUsername());
        join("#menace-test");
        run();
    }

}
