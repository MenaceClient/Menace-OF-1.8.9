package dev.menace.scripting.js.mappings;

import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;
import net.minecraft.client.Minecraft;

@JSMapping
public class PlayerMap {

    private static final Minecraft mc = Minecraft.getMinecraft();

    @MappedName(19)
    public static double getX() {
        return mc.thePlayer.posX;
    }
    @MappedName(20)
    public static double getY() {
        return mc.thePlayer.posY;
    }
    @MappedName(21)
    public static double getZ() {
        return mc.thePlayer.posZ;
    }

    @MappedName(22)
    public static void setX(double x) {
        mc.thePlayer.setPositionAndUpdate(x, mc.thePlayer.posY, mc.thePlayer.posZ);
    }
    @MappedName(23)
    public static void setY(double y) {
        mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, y, mc.thePlayer.posZ);
    }
    @MappedName(24)
    public static void setZ(double z) {
        mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY, z);
    }

    @MappedName(25)
    public static void setPos(double x, double y, double z) {
        mc.thePlayer.setPositionAndUpdate(x, y, z);
    }

    @MappedName(26)
    public static double getMotionX() {
        return mc.thePlayer.motionX;
    }

    @MappedName(27)
    public static double getMotionY() {
        return mc.thePlayer.motionY;
    }

    @MappedName(28)
    public static double getMotionZ() {
        return mc.thePlayer.motionZ;
    }

    @MappedName(29)
    public static void setMotionX(double x) {
        mc.thePlayer.motionX = x;
    }

    @MappedName(30)
    public static void setMotionY(double y) {
        mc.thePlayer.motionY = y;
    }

    @MappedName(31)
    public static void setMotionZ(double z) {
        mc.thePlayer.motionZ = z;
    }

    @MappedName(32)
    public static double getHurtTime() {
        return mc.thePlayer.hurtTime;
    }

    @MappedName(33)
    public static boolean isOnGround() {
        return mc.thePlayer.onGround;
    }

    @MappedName(34)
    public static void setOnGround(boolean onGround) {
        mc.thePlayer.onGround = onGround;
    }

    @MappedName(35)
    public static boolean isSprinting() {
        return mc.thePlayer.isSprinting();
    }

    @MappedName(36)
    public static void setSprinting(boolean sprinting) {
        mc.thePlayer.setSprinting(sprinting);
    }

    @MappedName(37)
    public static boolean isSneaking() {
        return mc.thePlayer.isSneaking();
    }

    @MappedName(38)
    public static void setSneaking(boolean sneaking) {
        mc.thePlayer.setSneaking(sneaking);
    }

    @MappedName(39)
    public static float getRotationYaw() {
        return mc.thePlayer.rotationYaw;
    }

    @MappedName(40)
    public static void setRotationYaw(float yaw) {
        mc.thePlayer.rotationYaw = yaw;
    }

    @MappedName(41)
    public static float getRotationPitch() {
        return mc.thePlayer.rotationPitch;
    }

    @MappedName(42)
    public static void setRotationPitch(float pitch) {
        mc.thePlayer.rotationPitch = pitch;
    }

    @MappedName(43)
    public static double getHealth() {
        return mc.thePlayer.getHealth();
    }

    @MappedName(44)
    public static void jump() {
        mc.thePlayer.jump();
    }

}
