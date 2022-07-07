package dev.menace.ui.hud.elements;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventDeath;
import dev.menace.ui.hud.BaseElement;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.LocalTime;

public class GameStatsElement extends BaseElement {

    public MSTimer timer = new MSTimer();
    public int kills = 0;

    public GameStatsElement() {
        super(0.5, 0.5, true);
    }

    public void start() {
        Menace.instance.eventManager.register(this);
    }

    public void stop() {
        Menace.instance.eventManager.unregister(this);
    }

    public void reset() {
        timer.reset();
        kills = 0;
    }

    @Override
    public void render() {
        RenderUtils.drawRoundedRect(this.getAbsoluteX(), this.getAbsoluteY(), this.getAbsoluteX() + this.getStringWidth("Welcome back " + Menace.instance.user.getUsername() + " [" + Menace.instance.user.getUID() + "]") + 3, this.getAbsoluteY() + 50, 5, new Color(0, 0, 0, 100).getRGB());

        this.drawString("Welcome back " + Menace.instance.user.getUsername() + " [" + Menace.instance.user.getUID() + "]", this.getAbsoluteX() + 2, this.getAbsoluteY() + 2, Color.white.getRGB());

        LocalTime lt = LocalTime.ofSecondOfDay(timer.timePassed() / 1000);
        String second = lt.getSecond() < 10 ? "0" + lt.getSecond() : String.valueOf(lt.getSecond());
        String hour = lt.getHour() != 0 ? lt.getHour() + ":" : "";
        this.drawString("PlayTime: " + hour + lt.getMinute() + ":" + second, this.getAbsoluteX() + 2, this.getAbsoluteY() + this.getFontHeight() + 2, Color.white.getRGB());

        this.drawString("Kills: " + kills, this.getAbsoluteX() + 2, this.getAbsoluteY() + (this.getFontHeight() * 2) + 2, Color.white.getRGB());
    }

    @Override
    public void renderDummy() {

    }

    @Override
    public int getWidth() {
        return this.getStringWidth("Welcome back " + Menace.instance.user.getUsername() + " [" + Menace.instance.user.getUID() + "]") + 3;
    }

    @Override
    public int getHeight() {
        return 50;
    }
}
