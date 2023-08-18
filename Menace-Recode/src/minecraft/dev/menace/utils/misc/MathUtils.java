package dev.menace.utils.misc;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class MathUtils {

	private static final Random rand = new Random();
	
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
        return f > 180F ? 360F - f : f;
    }

    public static float clamp(float angle, float min, float max) {
        return Math.max(min, Math.min(max, angle));
    }

    public static double precisionRound (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    /***
     * Interpolating method
     * @param start start of the interval
     * @param end end of the interval
     * @param count count of output interpolated numbers
     * @return array of interpolated number with specified count
     */
    public static double[] interpolate(double start, double end, int count) {
        if (count < 2) {
            throw new IllegalArgumentException("interpolate: illegal count!");
        }
        double[] array = new double[count + 1];
        for (int i = 0; i <= count; ++ i) {
            array[i] = start + i * (end - start) / count;
        }
        return array;
    }

    public static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public static double snapToStep(double value, double valueStep) {
        if (valueStep > 0.0F)
            value = valueStep * (double) Math.round(value / valueStep);

        return value;
    }
	
}
