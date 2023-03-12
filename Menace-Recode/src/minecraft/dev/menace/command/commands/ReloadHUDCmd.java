package dev.menace.command.commands;

import dev.menace.command.BaseCommand;
import dev.menace.ui.hud.HUDManager;
import dev.menace.utils.misc.ChatUtils;

public class ReloadHUDCmd extends BaseCommand {

    public ReloadHUDCmd() {
        super("ReloadHUD", "Reloads the HUD if you changed the file.", "ReloadHUD");
    }

    @Override
    public void call(String[] args) {
        HUDManager.load();
        ChatUtils.message("Reloaded HUD.");
    }
}
