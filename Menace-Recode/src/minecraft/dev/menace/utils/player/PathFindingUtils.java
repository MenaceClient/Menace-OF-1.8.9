package dev.menace.utils.player;

import dev.menace.utils.misc.MathUtils;
import dev.menace.utils.world.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PathFindingUtils {

    Minecraft mc = Minecraft.getMinecraft();

    public ArrayList<BlockPos> findPath(double destX, double destY, double destZ) {
        return findPath(new BlockPos(destX, destY, destZ));
    }

    public ArrayList<BlockPos> findPath(@NotNull EntityLivingBase destination) {
        return findPath(new BlockPos(destination.posX, destination.posY, destination.posZ));
    }

    public ArrayList<BlockPos> findPath(@NotNull BlockPos destination) {
        ArrayList<BlockPos> path = new ArrayList<>();
        EntitySnowball destAsEntity = new EntitySnowball(mc.theWorld, destination.getX(), destination.getY(), destination.getZ());
        if (mc.thePlayer.getDistanceToEntity(destAsEntity) < 5) {
            path.add(destination);
            return path;
        }

        double[] xList = MathUtils.interpolate(mc.thePlayer.posX, destination.getX(), (int) (mc.thePlayer.getDistanceToEntity(destAsEntity) / 8));
        double[] yList = MathUtils.interpolate(mc.thePlayer.posY, destination.getY(), (int) (mc.thePlayer.getDistanceToEntity(destAsEntity) / 8));
        double[] zList = MathUtils.interpolate(mc.thePlayer.posZ, destination.getZ(), (int) (mc.thePlayer.getDistanceToEntity(destAsEntity) / 8));

        int o = 0;


        while (o < xList.length) {

            path.add(new BlockPos(xList[0], yList[0], zList[0]));

            o++;
        }

        return path;
    }

}
