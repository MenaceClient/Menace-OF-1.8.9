package dev.menace.utils.notifications;

import dev.menace.Menace;
import dev.menace.utils.render.animtion.Animate;
import dev.menace.utils.render.animtion.Easing;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class Notification {

    private final String title;
    private final String contents;
    private final long time;
    private final Color color;
    private final Animate animation;
    MSTimer timer = new MSTimer();

    public Notification(String title, String contents, long time,  Color color) {
        this.title = title;
        this.contents = contents;
        this.time = time;
        this.color = color;
        this.animation = new Animate().setSpeed(300).setMin(0).setMax(this.getStringWidth(contents)).setEase(Easing.LINEAR).setReversed(false);
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

    public Animate getAnimation() {
        return animation;
    }

    private int getStringWidth(String string) {
        if (Menace.instance.moduleManager.hudModule.customFont.getValue()) {
            return Menace.instance.sfPro.getStringWidth(string);
        } else {
            return Minecraft.getMinecraft().fontRendererObj.getStringWidth(string);
        }
    }
}
