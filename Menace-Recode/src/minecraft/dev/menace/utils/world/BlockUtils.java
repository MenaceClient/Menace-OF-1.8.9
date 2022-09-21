package dev.menace.utils.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class BlockUtils {

    private static final Minecraft MC = Minecraft.getMinecraft();

    public static Block getBlock(BlockPos blockPos) {
        return MC.theWorld.getBlockState(blockPos).getBlock();
    }

    public static Material getMaterial(BlockPos blockPos) {return getBlock(blockPos).getMaterial();}

    public static boolean isBlockUnder(double posX, double posY, double posZ) {
        for(int i = (int)(posY - 1.0D); i > 0; --i) {
            BlockPos pos = new BlockPos(posX, i, posZ);
            if (!(MC.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
                return true;
            }
        }

        return false;
    }

    private static boolean canPassThrough(BlockPos pos) {
        Block block = BlockUtils.getBlock(pos);
        return block.getMaterial() == Material.air || block.getMaterial() == Material.plants || block.getMaterial() == Material.vine || block == Blocks.ladder || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
    }

}
