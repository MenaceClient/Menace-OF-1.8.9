package dev.menace.utils.player;

import dev.menace.event.events.EventMove;
import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.ArrayList;

@JSMapping
public class MovementUtils {

	private static final Minecraft mc = Minecraft.getMinecraft();

    @MappedName(77)
	public static boolean isMoving() {
		return mc.thePlayer.motionX != 0 && mc.thePlayer.motionZ != 0;
	}

    @MappedName(78)
    public static boolean shouldMove() {
        return mc.gameSettings.keyBindForward.isKeyDown()
            || mc.gameSettings.keyBindBack.isKeyDown()
            || mc.gameSettings.keyBindRight.isKeyDown()
            || mc.gameSettings.keyBindLeft.isKeyDown();
    }

    @MappedName(79)
	public static float getSpeed() {
        return MathHelper.sqrt_double(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
    }

    @MappedName(79)
    public static float getSpeed(EntityLivingBase entity) {
        return MathHelper.sqrt_double(entity.motionX * entity.motionX + entity.motionZ * entity.motionZ);
    }

    @MappedName(80)
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }

    @MappedName(81)
    public static void strafe() {
        strafe(getSpeed());
    }

    @MappedName(81)
    public static void strafe(final float speed) {
        if (!isMoving()) return;

        final double yaw = getDirection() / 180 * Math.PI;
        mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }

    @MappedName(82)
    public static void stop() {
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionY = 0;
        mc.thePlayer.motionZ = 0;
    }

    @MappedName(83)
    public static void stopHoriz() {
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionZ = 0;
    }

    @MappedName(84)
    public static void setSpeed(EventMove moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        double yaw = pseudoYaw;
        if (forward == 0.0 && strafe == 0.0) {
            moveEvent.setZ(0.0);
            moveEvent.setX(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (forward > 0.0) ? -45 : 45;
                } else if (strafe < 0.0) {
                    yaw += (forward > 0.0) ? 45 : -45;
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            double cos = Math.cos(Math.toRadians((yaw + 90.0f)));
            double sin = Math.sin(Math.toRadians((yaw + 90.0f)));
            moveEvent.setX(forward * moveSpeed * cos + strafe * moveSpeed * sin);
            moveEvent.setZ(forward * moveSpeed * sin - strafe * moveSpeed * cos);
        }
    }

    @MappedName(85)
    public static double getDirection() {
        float rotationYaw = mc.thePlayer.rotationYaw;

        if (mc.thePlayer.moveForward < 0F) rotationYaw += 180F;

        float forward = 1F;
        if (mc.thePlayer.moveForward < 0F) forward = -0.5F;
        else if (mc.thePlayer.moveForward > 0F) forward = 0.5F;

        if (mc.thePlayer.moveStrafing > 0F) rotationYaw -= 90F * forward;

        if (mc.thePlayer.moveStrafing < 0F) rotationYaw += 90F * forward;

        return rotationYaw;
    }


}
