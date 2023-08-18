package dev.menace.command.commands;

import dev.menace.Menace;
import dev.menace.command.BaseCommand;
import dev.menace.utils.misc.ChatUtils;

public class FriendCmd extends BaseCommand {

    public FriendCmd() {
        super("Friend", "Add or remove a friend.", "friend add <player>", "friend remove <player>", "friend list", "friend clear");
    }

    @Override
    public void call(String[] args) {
        if (args.length < 3) {
            return;
        }

        if (args[0].equalsIgnoreCase("add")) {
            Menace.instance.friendManager.addFriend(args[1]);
        } else if (args[0].equalsIgnoreCase("remove")) {
            Menace.instance.friendManager.removeFriend(args[1]);
        } else if (args[0].equalsIgnoreCase("list")) {
            ChatUtils.message("Friends:");
            Menace.instance.friendManager.getFriends().forEach(ChatUtils::message);
        } else if (args[0].equalsIgnoreCase("clear")) {
            Menace.instance.friendManager.clearFriends();
        }

    }
}
