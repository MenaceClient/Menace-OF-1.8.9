package dev.menace.scripting.mappings;

import net.minecraft.client.Minecraft;

public class PlayerMap {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static double getX() {
        return mc.thePlayer.posX;
    }
    public static double getY() {
        return mc.thePlayer.posY;
    }
    public static double getZ() {
        return mc.thePlayer.posZ;
    }

    public static void setX(double x) {
        mc.thePlayer.setPositionAndUpdate(x, mc.thePlayer.posY, mc.thePlayer.posZ);
    }
    public static void setY(double y) {
        mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, y, mc.thePlayer.posZ);
    }
    public static void setZ(double z) {
        mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY, z);
    }

    public static void setPos(double x, double y, double z) {
        mc.thePlayer.setPositionAndUpdate(x, y, z);
    }

    public static double getMotionX() {
        return mc.thePlayer.motionX;
    }

    public static double getMotionY() {
        return mc.thePlayer.motionY;
    }

    public static double getMotionZ() {
        return mc.thePlayer.motionZ;
    }

    public static void setMotionX(double x) {
        mc.thePlayer.motionX = x;
    }

    public static void setMotionY(double y) {
        mc.thePlayer.motionY = y;
    }

    public static void setMotionZ(double z) {
        mc.thePlayer.motionZ = z;
    }

    public static double getHurtTime() {
        return mc.thePlayer.hurtTime;
    }

    public static boolean getOnGround() {
        return mc.thePlayer.onGround;
    }

    public static void setOnGround(boolean onGround) {
        mc.thePlayer.onGround = onGround;
    }

    public static boolean isSprinting() {
        return mc.thePlayer.isSprinting();
    }

    public static void setSprinting(boolean sprinting) {
        mc.thePlayer.setSprinting(sprinting);
    }

    public static boolean isSneaking() {
        return mc.thePlayer.isSneaking();
    }

    public static void setSneaking(boolean sneaking) {
        mc.thePlayer.setSneaking(sneaking);
    }

    public static float getRotationYaw() {
        return mc.thePlayer.rotationYaw;
    }

    public static void setRotationYaw(float yaw) {
        mc.thePlayer.rotationYaw = yaw;
    }

    public static float getRotationPitch() {
        return mc.thePlayer.rotationPitch;
    }

    public static void setRotationPitch(float pitch) {
        mc.thePlayer.rotationPitch = pitch;
    }

    public static double getHealth() {
        return mc.thePlayer.getHealth();
    }

    public static void jump() {
        mc.thePlayer.jump();
    }

}
