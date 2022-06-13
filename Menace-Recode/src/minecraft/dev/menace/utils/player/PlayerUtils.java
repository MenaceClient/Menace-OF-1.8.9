package dev.menace.utils.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class PlayerUtils {

	private static Minecraft MC = Minecraft.getMinecraft();
	
	public static float[] getRotations(Entity entity) {
		return getRotations(entity.posX, entity.posY, entity.posZ);
	}
	
	public static float[] getRotations(BlockPos pos) {
		return getRotations(pos.getX(), pos.getY(), pos.getZ());
	}
	
    public static float[] getRotations(double x, double y, double z) {
		double diffX = x - MC.thePlayer.posX;
		double diffY = y - (MC.thePlayer.posY + MC.thePlayer.getEyeHeight()) + 1;
		double diffZ = z - MC.thePlayer.posZ;

		double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

		float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
		float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));
        return new float[] { yaw, pitch };
    }
    
    public static boolean isInLiquid() {
        for(int x = MathHelper.floor_double(MC.thePlayer.boundingBox.minY); x < MathHelper.floor_double(MC.thePlayer.boundingBox.maxX) + 1; ++x) {
            for(int z = MathHelper.floor_double(MC.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(MC.thePlayer.boundingBox.maxZ) + 1; ++z) {
                BlockPos pos = new BlockPos(x, (int)MC.thePlayer.boundingBox.minY, z);
                Block block = MC.theWorld.getBlockState(pos).getBlock();
                if(block != null && !(block instanceof BlockAir))
                    return block instanceof BlockLiquid;
            }
        }
        return false;
    }
	
}
