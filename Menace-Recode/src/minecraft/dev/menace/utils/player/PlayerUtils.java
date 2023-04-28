package dev.menace.utils.player;

import dev.menace.event.events.EventPreMotion;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.MathUtils;
import dev.menace.utils.world.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.*;

import java.util.concurrent.ThreadLocalRandom;

public class PlayerUtils {

	private static final Minecraft mc = Minecraft.getMinecraft();
	
	public static float[] getRotations(Entity entity) {
		return getRotations(entity.posX, entity.posY, entity.posZ);
	}
	
	public static float[] getRotations(BlockPos pos) {
		return getRotations(pos.getX(), pos.getY(), pos.getZ());
	}
	
    public static float[] getRotations(double x, double y, double z) {
		double diffX = x - mc.thePlayer.posX;
		double diffY = y - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()) + 1;
		double diffZ = z - mc.thePlayer.posZ;

		double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

		float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
		float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));
        return new float[] { yaw, pitch };
    }

    public static float[] getRotations2(Entity entity) {
        double deltaX = entity.posX + (entity.posX - entity.lastTickPosX) - mc.thePlayer.posX;
        double deltaY = entity.posY - 3.5 + entity.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
        double deltaZ = entity.posZ + (entity.posZ - entity.lastTickPosZ) - mc.thePlayer.posZ;
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
        final EntityEgg var4 = new EntityEgg(mc.theWorld);
        var4.posX = x + 0.5D;
        var4.posY = y + 0.5D;
        var4.posZ = z + 0.5D;
        var4.posX += (double) enumfacing.getDirectionVec().getX() * 0.5D;
        var4.posY += (double) enumfacing.getDirectionVec().getY() * 0.5D;
        var4.posZ += (double) enumfacing.getDirectionVec().getZ() * 0.5D;
        return getRotationsForBlock(var4.posX, var4.posY, var4.posZ);
    }

    public static float[] getRotationsForBlock(final double posX, final double posY, final double posZ) {
        final EntityPlayerSP player = mc.thePlayer;
        final double x = posX - player.posX;
        final double y = posY - (player.posY + (double) player.getEyeHeight());
        final double z = posZ - player.posZ;
        final double dist = MathHelper.sqrt_double(x * x + z * z);
        final float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        final float pitch = (float) (-(Math.atan2(y, dist) * 180.0D / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static float[] getRotsNew(final BlockPos pos, final EnumFacing facing) {
        final float yaw = getYaw(pos, facing);
        final float[] rots2 = getDirectionToBlock(pos.getX(), pos.getY(), pos.getZ(), facing);
        return new float[] { (float)(yaw + ThreadLocalRandom.current().nextDouble(-1.0, 1.0)), mc.thePlayer.onGround ? 80.31f : Math.min(90.0f, rots2[1]) };
    }

    public static float[] getDirectionToBlock(final int var0, final int var1, final int var2, final EnumFacing var3) {
        final EntityEgg var4 = new EntityEgg(mc.theWorld);
        var4.posX = var0 + 0.5;
        var4.posY = var1 + 0.5;
        var4.posZ = var2 + 0.5;
        var4.posX += var3.getDirectionVec().getX() * 0.25;
        var4.posY += var3.getDirectionVec().getY() * 0.25;
        var4.posZ += var3.getDirectionVec().getZ() * 0.25;
        return getDirectionToEntity(var4);
    }

    public static float[] getDirectionToEntity(final Entity var0) {
        return new float[] { getYaw(var0) + mc.thePlayer.rotationYaw, getPitch(var0) + mc.thePlayer.rotationPitch };
    }

    public static float getYaw(final Entity var0) {
        final double var = var0.posX - mc.thePlayer.posX;
        final double var2 = var0.posZ - mc.thePlayer.posZ;
        final double degrees = Math.toDegrees(Math.atan(var2 / var));
        double var3;
        if (var2 < 0.0 && var < 0.0) {
            var3 = 90.0 + degrees;
        }
        else if (var2 < 0.0 && var > 0.0) {
            var3 = -90.0 + degrees;
        }
        else {
            var3 = Math.toDegrees(-Math.atan(var / var2));
        }
        return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float)var3));
    }

    public static float getPitch(final Entity var0) {
        final double var = var0.posX - mc.thePlayer.posX;
        final double var2 = var0.posZ - mc.thePlayer.posZ;
        final double var3 = var0.posY - 1.6 + var0.getEyeHeight() - mc.thePlayer.posY;
        final double var4 = MathHelper.sqrt_double(var * var + var2 * var2);
        final double var5 = -Math.toDegrees(Math.atan(var3 / var4));
        return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float)var5);
    }

    public static float getYaw(final BlockPos block, final EnumFacing face) {
        final Vec3 vecToModify = new Vec3(block.getX(), block.getY(), block.getZ());
        switch (face) {
            case EAST:
            case WEST: {
                vecToModify.zCoord += 0.5;
                break;
            }
            case SOUTH:
            case NORTH: {
                vecToModify.xCoord += 0.5;
                break;
            }
            case UP:
            case DOWN: {
                vecToModify.xCoord += 0.5;
                vecToModify.zCoord += 0.5;
                break;
            }
        }
        final double x = vecToModify.xCoord - mc.thePlayer.posX;
        final double z = vecToModify.zCoord - mc.thePlayer.posZ;
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        if (yaw < 0.0f) {
            yaw -= 360.0f;
        }
        return yaw;
    }

    public static Vec3 getHead(final AxisAlignedBB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.maxY, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }

    public static Vec3 getEyes(Entity entity) {
        return new Vec3(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
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
        return new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
    }

    public static boolean isInLiquid() {
        for (int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minY); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
                BlockPos pos = new BlockPos(x, (int) mc.thePlayer.boundingBox.minY, z);
                Block block = mc.theWorld.getBlockState(pos).getBlock();
                if (block != null && !(block instanceof BlockAir))
                    return block instanceof BlockLiquid;
            }
        }
        return false;
    }

    public static boolean isOnGround(Entity entity) {
        AxisAlignedBB playerBoundingBox = entity.getEntityBoundingBox();
        AxisAlignedBB customBox = new AxisAlignedBB(playerBoundingBox.maxX, entity.posY - 1, playerBoundingBox.maxZ, playerBoundingBox.minX, entity.posY, playerBoundingBox.minZ);
        return mc.theWorld.checkBlockCollision(customBox);
    }

    public static double calculateGround() {
        AxisAlignedBB playerBoundingBox = mc.thePlayer.getEntityBoundingBox();
        double blockHeight = 1.0;
        double ground = mc.thePlayer.posY;

        while (ground > 0.0) {
            AxisAlignedBB customBox = new AxisAlignedBB(playerBoundingBox.maxX, ground + blockHeight, playerBoundingBox.maxZ, playerBoundingBox.minX, ground, playerBoundingBox.minZ);
            if (mc.theWorld.checkBlockCollision(customBox)) {
                if (blockHeight <= 0.05) return ground + blockHeight;
                ground += blockHeight;
                blockHeight = 0.05;
            }
            ground -= blockHeight;
        }

        return 0.0;
    }

    public static boolean isBlockUnder() {
        for (int offset = 0; offset < mc.thePlayer.posY + mc.thePlayer.getEyeHeight(); offset += 2) {
            final AxisAlignedBB boundingBox = mc.thePlayer.getEntityBoundingBox().offset(0, -offset, 0);

            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, boundingBox).isEmpty())
                return true;
        }
        return false;
    }

    public static boolean placeBlockSimple(final BlockPos pos) {
        final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (BlockUtils.getBlock(neighbor).canCollideCheck(mc.theWorld.getBlockState(neighbor), false)) {
                final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
                if (eyesPos.squareDistanceTo(hitVec) <= 36.0) {

                    mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), neighbor, side2, hitVec);

                    mc.thePlayer.swingItem();

                    mc.rightClickDelayTimer = 4;

                    return true;
                }
            }
        }
        return false;
    }

    public static void placeBlockPacket(final BlockPos pos) {
        final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (BlockUtils.getBlock(neighbor).canCollideCheck(mc.theWorld.getBlockState(neighbor), false)) {
                final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
                if (eyesPos.squareDistanceTo(hitVec) <= 36.0) {

                    float f = (float)(hitVec.xCoord - (double)neighbor.getX());
                    float f1 = (float)(hitVec.yCoord - (double)neighbor.getY());
                    float f2 = (float)(hitVec.zCoord - (double)neighbor.getZ());
                    PacketUtils.sendPacket(new C08PacketPlayerBlockPlacement(neighbor, side.getIndex(), mc.thePlayer.inventory.getCurrentItem(), f, f1, f2));
                }
            }
        }
    }

}
