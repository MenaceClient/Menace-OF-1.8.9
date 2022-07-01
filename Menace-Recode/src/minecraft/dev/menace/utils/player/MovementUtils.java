package dev.menace.utils.player;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public class MovementUtils {

	private static Minecraft MC = Minecraft.getMinecraft();

	public static boolean isMoving() {
		return MC.thePlayer.motionX != 0 && MC.thePlayer.motionZ != 0;
	}

    public static boolean shouldMove() {
        return MC.gameSettings.keyBindForward.isKeyDown()
            || MC.gameSettings.keyBindBack.isKeyDown()
            || MC.gameSettings.keyBindRight.isKeyDown()
            || MC.gameSettings.keyBindLeft.isKeyDown();
    }

	public static float getSpeed() {
        return MathHelper.sqrt_double(MC.thePlayer.motionX * MC.thePlayer.motionX + MC.thePlayer.motionZ * MC.thePlayer.motionZ);
    }

    public static void strafe() {
        strafe(getSpeed());
    }

    public static void strafe(final float speed) {
        if (!isMoving()) return;

        final double yaw = getDirection() / 180 * Math.PI;
        MC.thePlayer.motionX = -Math.sin(yaw) * speed;
        MC.thePlayer.motionZ = Math.cos(yaw) * speed;
    }

    public static void stop() {
        MC.thePlayer.posX = 0;
        MC.thePlayer.posY = 0;
        MC.thePlayer.posZ = 0;
    }
    
    public static double getDirection() {
        float rotationYaw = MC.thePlayer.rotationYaw;

        if (MC.thePlayer.moveForward < 0F) rotationYaw += 180F;

        float forward = 1F;
        if (MC.thePlayer.moveForward < 0F) forward = -0.5F;
        else if (MC.thePlayer.moveForward > 0F) forward = 0.5F;

        if (MC.thePlayer.moveStrafing > 0F) rotationYaw -= 90F * forward;

        if (MC.thePlayer.moveStrafing < 0F) rotationYaw += 90F * forward;

        return rotationYaw;
    }
}
