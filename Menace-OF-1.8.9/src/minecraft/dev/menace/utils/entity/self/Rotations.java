package dev.menace.utils.entity.self;

import java.util.concurrent.TimeUnit;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventSendPacket;
import dev.menace.utils.misc.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Rotations {
	
	static Minecraft MC = Minecraft.getMinecraft();
	
	public static float yaw, pitch;
	
	public void init() {
		Menace.instance.eventManager.register(this);
	}
	
	public static void rotateSmoothLegit(float yaw, float pitch, float speed) {

		float newYaw;
		float newPitch;

		newYaw = MathUtils.lerp(MC.thePlayer.rotationYaw, yaw, speed);
		newPitch = MathUtils.lerp(MC.thePlayer.rotationPitch, pitch, speed);
		MC.thePlayer.setRotation(newYaw, newPitch);
	}
	
	public static void rotateLegit(float yaw, float pitch) {
		MC.thePlayer.setRotation(yaw, pitch);
	}
	
	public static void rotatePacket(float yaw, float pitch) {
		MC.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, MC.thePlayer.onGround));
	}
	
	public static float[] getRotations(Entity entity) {
		return getRotations(entity.posX, entity.posY, entity.posZ);
	}
	
	public static float[] getRotations(BlockPos pos) {
		return getRotations(pos.getX(), pos.getY(), pos.getZ());
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		
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
    
    public static Vec3 getCenter(final AxisAlignedBB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.5, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
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
    
    public static Vec3 getEyesPos() {
        return new Vec3(MC.thePlayer.posX, MC.thePlayer.posY + MC.thePlayer.getEyeHeight(), MC.thePlayer.posZ);
    }
    
    public static float getDistanceBetweenAngles(final float angle1, final float angle2) {
        float angle3 = Math.abs(angle1 - angle2) % 360.0f;
        if (angle3 > 180.0f) {
            angle3 = 360.0f - angle3;
        }
        return angle3;
    }
    
    public static double[] getRotationFromEyeHasPrev(Entity attacked) {
        final double x = (attacked.prevPosX + (attacked.posX - attacked.prevPosX));
        final double y = (attacked.prevPosY + (attacked.posY - attacked.prevPosY));
        final double z = (attacked.prevPosZ + (attacked.posZ - attacked.prevPosZ));
        return getRotationFromEyeHasPrev(x, y, z);
    }
    
    public static double[] getRotationFromEyeHasPrev(double x, double y, double z) {
        double xDiff = x - (MC.thePlayer.prevPosX + (MC.thePlayer.posX - MC.thePlayer.prevPosX));
        double yDiff = y - ((MC.thePlayer.prevPosY + (MC.thePlayer.posY - MC.thePlayer.prevPosY)) + (MC.thePlayer.getEntityBoundingBox().maxY - MC.thePlayer.getEntityBoundingBox().minY));
        double zDiff = z - (MC.thePlayer.prevPosZ + (MC.thePlayer.posZ - MC.thePlayer.prevPosZ));
        final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        double[] i = {(float) (Math.atan2(zDiff, xDiff) * 180D / Math.PI) - 90F, (float) -(Math.atan2(yDiff, dist) * 180D / Math.PI)};
        return i;
    }
	
}
