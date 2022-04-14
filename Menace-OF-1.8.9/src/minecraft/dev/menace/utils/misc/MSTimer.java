/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package dev.menace.utils.misc;

public class MSTimer {
    private long time = -1L;

    public boolean hasTimePassed(long ms) {
        return System.currentTimeMillis() >= time + ms;
    }

    public long hasTimeLeft(long ms) {
        return ms + time - System.currentTimeMillis();
    }

    public long timePassed() {
        return System.currentTimeMillis() - time;
    }

    public void reset() {
        time = System.currentTimeMillis();
    }
}
