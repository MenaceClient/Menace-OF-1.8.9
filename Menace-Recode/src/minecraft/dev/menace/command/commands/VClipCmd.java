package dev.menace.command.commands;

import dev.menace.command.BaseCommand;
import dev.menace.utils.misc.ChatUtils;

public class VClipCmd extends BaseCommand {

	public VClipCmd() {
		super("VClip", "Allows you to teleport up and down blocks.", "$vclip <amount>");
	}

	@Override
	public void call(String[] args) {
		MC.thePlayer.setPosition(MC.thePlayer.posX, MC.thePlayer.posY + Double.parseDouble(args[0]), MC.thePlayer.posZ);
		ChatUtils.message("VClipped " + args[0] + " blocks.");
	}
	
}
