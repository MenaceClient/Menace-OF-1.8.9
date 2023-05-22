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

        if (args[0].equalsIgnoreCase("checkAuth")) {
            Menace.instance.spotifyUtils.auth();
            if (Menace.instance.spotifyUtils.isAuthed()) {
                ChatUtils.message("Your spotify account has been authenticated.");
            } else {
                ChatUtils.message("Authentication with Spotify failed.");
            }
        }
    }
}
