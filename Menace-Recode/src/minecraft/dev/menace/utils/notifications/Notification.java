package dev.menace.utils.notifications;

import dev.menace.utils.timer.MSTimer;

import java.awt.*;

public class Notification {

    private final String title;
    private final String contents;
    private final long time;
    private final Color color;
    MSTimer timer = new MSTimer();

    public Notification(String title, String contents, long time,  Color color) {
        this.title = title;
        this.contents = contents;
        this.time = time;
        this.color = color;
        timer.reset();
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public Color getColor() {
        return color;
    }

    public long getTime() {
        return time;
    }

    public MSTimer getTimer() {
        return timer;
    }
}
