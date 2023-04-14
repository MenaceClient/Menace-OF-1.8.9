package dev.menace.utils.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.util.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PlayerUtils {

	private static final Minecraft MC = Minecraft.getMinecraft();
	
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

    public static float[] getRotations2(Entity entity) {
        double deltaX = entity.posX + (entity.posX - entity.lastTickPosX) - MC.thePlayer.posX;
        double deltaY = entity.posY - 3.5 + entity.getEyeHeight() - MC.thePlayer.posY + MC.thePlayer.getEyeHeight();
        double deltaZ = entity.posZ + (entity.posZ - entity.lastTickPosZ) - MC.thePlayer.posZ;
        double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));
        float yaw = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ));
        float pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));

        if (deltaX < 0 && deltaZ < 0) {
            yaw = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
        } else if (deltaX > 0 && deltaZ < 0) {
            yaw = (float) (-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
        }

        return new float[]{yaw, pitch};
    }

    public static float[] getDirectionToBlock(final double x, final double y, final double z, final EnumFacing enumfacing) {
        final EntityEgg var4 = new EntityEgg(MC.theWorld);
        var4.posX = x + 0.5D;
        var4.posY = y + 0.5D;
        var4.posZ = z + 0.5D;
        var4.posX += (double) enumfacing.getDirectionVec().getX() * 0.5D;
        var4.posY += (double) enumfacing.getDirectionVec().getY() * 0.5D;
        var4.posZ += (double) enumfacing.getDirectionVec().getZ() * 0.5D;
        return getRotationsForBlock(var4.posX, var4.posY, var4.posZ);
    }

    public static float[] getRotationsForBlock(final double posX, final double posY, final double posZ) {
        final EntityPlayerSP player = MC.thePlayer;
        final double x = posX - player.posX;
        final double y = posY - (player.posY + (double) player.getEyeHeight());
        final double z = posZ - player.posZ;
        final double dist = MathHelper.sqrt_double(x * x + z * z);
        final float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        final float pitch = (float) (-(Math.atan2(y, dist) * 180.0D / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static Vec3 getHead(final AxisAlignedBB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.maxY, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }

    public static Vec3 getCenter(final AxisAlignedBB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.5, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }

    public static Vec3 getCock(final AxisAlignedBB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.3, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }

    public static Vec3 getFeet(final AxisAlignedBB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }

    public static Vec3 getClosestPoint(final AxisAlignedBB bb, final Vec3 pos, float[] rotations) {
        final Vec3[] points = new Vec3[] {
                new Vec3(bb.minX, bb.minY, bb.minZ),
                new Vec3(bb.minX, bb.minY, bb.maxZ),
                new Vec3(bb.minX, bb.maxY, bb.minZ),
                new Vec3(bb.minX, bb.maxY, bb.maxZ),
                new Vec3(bb.maxX, bb.minY, bb.minZ),
                new Vec3(bb.maxX, bb.minY, bb.maxZ),
                new Vec3(bb.maxX, bb.maxY, bb.minZ),
                new Vec3(bb.maxX, bb.maxY, bb.maxZ)
        };
        double minDist = Double.MAX_VALUE;
        Vec3 closest = null;
        for (final Vec3 point : points) {
            final double dist = pos.squareDistanceTo(point);
            if (dist < minDist) {
                minDist = dist;
                closest = point;
            }
        }
        return closest;
        //return new Vec3(Math.max(bb.minX, Math.min(pos.xCoord, bb.maxX)), Math.max(bb.minY, Math.min(pos.yCoord, bb.maxY)), Math.max(bb.minZ, Math.min(pos.zCoord, bb.maxZ)));
    }

    public static float[] getRotations(final Vec3 vec) {
        final Vec3 eyesPos = getEyesPos();
        final double diffX = vec.xCoord - eyesPos.xCoord;
        final double diffY = vec.yCoord - eyesPos.yCoord;
        final double diffZ = vec.zCoord - eyesPos.zCoord;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch) };
    }

    /**
     * Smooths the current rotation using the last for it to make aura harder to flag.
     *
     * @param rotations     Current rotations.
     * @param lastRotations Last rotations.
     * @return Current rotation smoothed according to last.
     */
    public static float[] getFixedRotation(final float[] rotations, final float[] lastRotations) {
        final Minecraft mc = Minecraft.getMinecraft();

        final float yaw = rotations[0];
        final float pitch = rotations[1];

        final float lastYaw = lastRotations[0];
        final float lastPitch = lastRotations[1];

        final float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        final float gcd = f * f * f * 1.2F;

        final float deltaYaw = yaw - lastYaw;
        final float deltaPitch = pitch - lastPitch;

        final float fixedDeltaYaw = deltaYaw - (deltaYaw % gcd);
        final float fixedDeltaPitch = deltaPitch - (deltaPitch % gcd);

        final float fixedYaw = lastYaw + fixedDeltaYaw;
        final float fixedPitch = lastPitch + fixedDeltaPitch;

        return new float[]{fixedYaw, fixedPitch};
    }

    public static Vec3 getEyesPos() {
        return new Vec3(MC.thePlayer.posX, MC.thePlayer.posY + MC.thePlayer.getEyeHeight(), MC.thePlayer.posZ);
    }

    public static boolean isInLiquid() {
        for (int x = MathHelper.floor_double(MC.thePlayer.boundingBox.minY); x < MathHelper.floor_double(MC.thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(MC.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(MC.thePlayer.boundingBox.maxZ) + 1; ++z) {
                BlockPos pos = new BlockPos(x, (int) MC.thePlayer.boundingBox.minY, z);
                Block block = MC.theWorld.getBlockState(pos).getBlock();
                if (block != null && !(block instanceof BlockAir))
                    return block instanceof BlockLiquid;
            }
        }
        return false;
    }

    public static double calculateGround() {
        AxisAlignedBB playerBoundingBox = MC.thePlayer.getEntityBoundingBox();
        double blockHeight = 1.0;
        double ground = MC.thePlayer.posY;

        while (ground > 0.0) {
            AxisAlignedBB customBox = new AxisAlignedBB(playerBoundingBox.maxX, ground + blockHeight, playerBoundingBox.maxZ, playerBoundingBox.minX, ground, playerBoundingBox.minZ);
            if (MC.theWorld.checkBlockCollision(customBox)) {
                if (blockHeight <= 0.05) return ground + blockHeight;
                ground += blockHeight;
                blockHeight = 0.05;
            }
            ground -= blockHeight;
        }

        return 0.0;
    }

    public static boolean isBlockUnder() {
        for (int offset = 0; offset < MC.thePlayer.posY + MC.thePlayer.getEyeHeight(); offset += 2) {
            final AxisAlignedBB boundingBox = MC.thePlayer.getEntityBoundingBox().offset(0, -offset, 0);

            if (!MC.theWorld.getCollidingBoundingBoxes(MC.thePlayer, boundingBox).isEmpty())
                return true;
        }
        return false;
    }

}
