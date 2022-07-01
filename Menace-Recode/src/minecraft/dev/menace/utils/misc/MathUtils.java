package dev.menace.utils.misc;

import java.util.Random;

public class MathUtils {

	private static Random rand = new Random();
	
	public static int randInt(int start, int end) {
		return end - start <= 0 ? start : start + rand.nextInt(end - start);
	}
	
	public static long randLong(final long startInclusive, final long endInclusive) {
        if(startInclusive == endInclusive || endInclusive - startInclusive <= 0F)
            return startInclusive;

        return (long) (startInclusive + ((endInclusive - startInclusive) * Math.random()));
    }

    public static float randFloat(final float startInclusive, final float endInclusive) {
        if(startInclusive == endInclusive || endInclusive - startInclusive <= 0F)
            return startInclusive;

        return (float) (startInclusive + ((endInclusive - startInclusive) * Math.random()));
    }
	
	public static float getAngleDifference(float dir, float yaw) {
        float f = Math.abs(yaw - dir) % 360F;
        float dist = f > 180F ? 360F - f : f;
        return dist;
    }
	
}
