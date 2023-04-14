package dev.menace.utils.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BlockUtils {

    private static final Minecraft MC = Minecraft.getMinecraft();

    public static Block getBlock(BlockPos blockPos) {
        return MC.theWorld.getBlockState(blockPos).getBlock();
    }

    public static Material getMaterial(BlockPos blockPos) {return getBlock(blockPos).getMaterial();}

    private static boolean canPassThrough(BlockPos pos) {
        Block block = BlockUtils.getBlock(pos);
        return block.getMaterial() == Material.air || block.getMaterial() == Material.plants || block.getMaterial() == Material.vine || block == Blocks.ladder || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
    }

    public static List<BlockPos> searchForBlocksInRadius(Block block, double radius) {
        List<BlockPos> blocks = new ArrayList<>();
        for (int x = (int) (MC.thePlayer.posX - radius); x < (int) (MC.thePlayer.posX + radius); x++) {
            for (int y = (int) (MC.thePlayer.posY - radius); y < (int) (MC.thePlayer.posY + radius); y++) {
                for (int z = (int) (MC.thePlayer.posZ - radius); z < (int) (MC.thePlayer.posZ + radius); z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (getBlock(pos) == block) {
                        blocks.add(pos);
                    }
                }
            }
        }
        return blocks;
    }

}
