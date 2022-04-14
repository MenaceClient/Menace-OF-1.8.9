package dev.menace.utils.misc;

public class MiscUtils {

    public static float getAngleDifference(float dir, float yaw) {
        float f = Math.abs(yaw - dir) % 360F;
        float dist = f > 180F ? 360F - f : f;
        return dist;
    }
	
}
