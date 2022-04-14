package dev.menace.utils.misc;

public class TickTimer {

    private static int tick = 0;

    public static void update() {
        tick++;
    }

    public static void reset() {
        tick = 0;
    }

    public static boolean hasTimePassed(int ticks) {
        return tick >= ticks;
    }
	
}
