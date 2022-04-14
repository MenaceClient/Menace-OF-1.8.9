package dev.menace.command.commands;

import dev.menace.command.CmdException;
import dev.menace.command.Command;
import dev.menace.utils.entity.self.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class TeleportCmd extends Command {

	public TeleportCmd() {
		super("Teleport-tp", "Teleports you to a player or place", ".tp <player>", ".tp <x> <y> <z>");
	}

	@Override
	public void call(String[] args) throws CmdException {
		if (args.length > 1) {
			
			PlayerUtils.tpToPos(new BlockPos(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2])));
			MC.thePlayer.setPosition(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
			
		} else {
			EntityPlayer p = null;
			
			for (EntityPlayer player : MC.theWorld.playerEntities) {
				if (player.getName().equalsIgnoreCase(args[0])) {
					p = player;
					break;
				}
			}
			
			if (p == null) return;
			
			PlayerUtils.tpToEnt(p);
			MC.thePlayer.setPosition(p.posX, p.posY, p.posZ);
		}
	}

}
