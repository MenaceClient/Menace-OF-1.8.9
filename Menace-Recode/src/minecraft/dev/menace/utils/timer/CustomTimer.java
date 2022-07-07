package dev.menace.utils.timer;

public class CustomTimer {

    private long time = -1L;

    public boolean hasTimePassed(long ms) {
        return time >= ms;
    }

    public long timePassed() {
        return time;
    }

    public void reset() {
        time = 0;
    }

    public void update() {
        time++;
    }

}
