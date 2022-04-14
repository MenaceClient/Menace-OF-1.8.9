package dev.menace.command.commands;

import dev.menace.command.CmdException;
import dev.menace.command.Command;

public class VClipCmd extends Command {

	public VClipCmd() {
		super("VClip", "Teleports you up or down", "VClip <Distance> (up)", "VClip <-Distance> (Down)");
	}

	@Override
	public void call(String[] args) throws CmdException {
		
		MC.thePlayer.setPosition(MC.thePlayer.posX, MC.thePlayer.posY + Double.parseDouble(args[0]), MC.thePlayer.posZ);
		
	}

}
