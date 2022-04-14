package dev.menace.command.commands;

import dev.menace.command.CmdException;
import dev.menace.command.Command;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public class DamageCmd extends Command {

	public DamageCmd() {
		super("Damage-dmg", "Damages yourself", ".dmg");
	}

	@Override
	public void call(String[] args) throws CmdException {
		MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY + 3.25, MC.thePlayer.posZ, false));
		MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY, MC.thePlayer.posZ, false));
		MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY, MC.thePlayer.posZ, true));
	}

}
