package dev.menace.command.commands;

import dev.menace.command.BaseCommand;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.player.PathFindingUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Vec3;

import java.util.ArrayList;

public class TeleportCmd extends BaseCommand {
    public TeleportCmd() {
        super("Teleport", "Teleport to an entity within a 500 block range", "teleport <entity>");
    }

    @Override
    public void call(String[] args) {
        if (args.length == 1) {
            ChatUtils.message("Teleporting to " + args[0]);
            EntityPlayer target = mc.theWorld.getPlayerEntityByName(args[0]);
            if (target != null && mc.thePlayer.getDistanceToEntity(target) <= 500) {
                final Vec3 from = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                final Vec3 to = new Vec3(target.posX, target.posY, target.posZ);
                ArrayList<Vec3> path = PathFindingUtils.computePath(from, to);
                for (final Vec3 pathElm : path) {
                    PacketUtils.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));
                }
                mc.thePlayer.setPositionAndUpdate(target.posX, target.posY, target.posZ);
            } else {
                ChatUtils.message("Player not found or out of range");
            }
        } else {
            ChatUtils.message("Invalid arguments");
        }
    }
}
