package dev.menace.utils.world;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class BlockUtils {

    private static final Minecraft MC = Minecraft.getMinecraft();

    public static Block getBlock(BlockPos blockPos) {
        return MC.theWorld.getBlockState(blockPos).getBlock();
    }

    public static Material getMaterial(BlockPos blockPos) {return getBlock(blockPos).getMaterial();}

}
