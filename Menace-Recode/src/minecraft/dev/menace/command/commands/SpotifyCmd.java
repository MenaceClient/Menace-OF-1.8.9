package dev.menace.command.commands;

import dev.menace.Menace;
import dev.menace.command.BaseCommand;
import dev.menace.utils.misc.ChatUtils;

public class SpotifyCmd extends BaseCommand {
    public SpotifyCmd() {
        super("Spotify", "Control different Spotify actions.", "");
    }

    @Override
    public void call(String[] args) {
        if (args.length == 0) {
            return;
        }

        if (args[0].equalsIgnoreCase("auth") || args[0].equalsIgnoreCase("authenticate")) {
            if (Menace.instance.spotifyUtils.CLIENTID.equals("")) {
                ChatUtils.message("Please set your Spotify Client ID with " + Menace.instance.cmdManager.prefix + "spotify setClientID <clientid>");
                return;
            } else if (Menace.instance.spotifyUtils.CLIENTSECRET.equals("")) {
                ChatUtils.message("Please set your Spotify Client Secret with " + Menace.instance.cmdManager.prefix + "spotify setClientSecret <clientsecret>");
                return;
            } else if (Menace.instance.spotifyUtils.isAuthed()) {
                ChatUtils.message("You are already authenticated with Spotify.");
                return;
            }
            Menace.instance.spotifyUtils.auth();
            ChatUtils.message("Authenting with Spotify please check your browser.");
        } else if (args[0].equalsIgnoreCase("setClientID")) {
            Menace.instance.spotifyUtils.CLIENTID = args[1];
            ChatUtils.message("Set Spotify Client ID to " + args[1]);
        } else if (args[0].equalsIgnoreCase("setClientSecret")) {
            Menace.instance.spotifyUtils.CLIENTSECRET = args[1];
            ChatUtils.message("Set Spotify Client Secret to " + args[1]);
        }
    }
}
