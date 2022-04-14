package dev.menace.utils.misc;

import java.util.Random;

public class RandomUtils {

	static Random random = new Random();
	
    public static int nextInt(int startInclusive, int endExclusive) {
        return (endExclusive - startInclusive <= 0) ? startInclusive : startInclusive + random.nextInt(endExclusive - startInclusive);
    }
	
}
