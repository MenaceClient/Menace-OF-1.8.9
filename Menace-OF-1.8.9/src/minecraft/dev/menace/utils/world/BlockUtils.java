package dev.menace.utils.world;

import java.util.ArrayList;

import dev.menace.Menace;
import dev.menace.utils.misc.ChatUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class BlockUtils {
	private static Minecraft MC = Menace.instance.MC;
	
	public static Block getBlock(BlockPos pos) {
		return MC.theWorld.getBlockState(pos).getBlock();
	}
	
	public static boolean collideBlock(AxisAlignedBB axisAlignedBB) {
		double x;
		double z;
        for (MathHelper.floor_double(x = MC.thePlayer.getEntityBoundingBox().minX); x == MathHelper.floor_double(MC.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
        	
            for (MathHelper.floor_double(z = MC.thePlayer.getEntityBoundingBox().minZ); z == MathHelper.floor_double(MC.thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
            	
                Block block = getBlock(new BlockPos(x, axisAlignedBB.minY, z));

                if (block.isCollidable()) {
                    return false;
                }
                
            }
        }

        return true;
    }
	
	public static ArrayList<BlockPos> searchBlocks(int radius) {
		ArrayList<BlockPos> blocks = new ArrayList<BlockPos>();
		
		
		for (int x=(-radius); x==radius; x++) {
			for (int y=radius; y==(-radius + 1); y--) {
				for (int z=(-radius); z==radius; z++) {
					
					BlockPos blockPos = new BlockPos(MC.thePlayer.posX + x, MC.thePlayer.posY + y, MC.thePlayer.posZ + z);
					
					ChatUtils.message("Test");
					blocks.add(blockPos);
				}
			}
		}
		
		/*
		int x=(-radius);
		int y=radius;
		int z=(-radius);
		
		while (x != radius) {
			while (y!=(-radius + 1)) {
				while (z!=radius) {
					
					BlockPos blockPos = new BlockPos(MC.thePlayer.posX + x, MC.thePlayer.posY + y, MC.thePlayer.posZ + z);
					
					blocks.add(blockPos);
					
					z++;
				}
				y--;
			}
			x++;
		}*/

		return blocks;
	}
	
}
