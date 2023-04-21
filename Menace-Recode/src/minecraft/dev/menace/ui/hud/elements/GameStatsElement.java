package dev.menace.ui.hud.elements;

import dev.menace.Menace;
import dev.menace.module.modules.render.HUDModule;
import dev.menace.ui.hud.BaseElement;
import dev.menace.utils.render.ColorUtils;
import dev.menace.utils.render.GLSLShader;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.awt.*;
import java.io.IOException;
import java.time.LocalTime;

public class GameStatsElement extends BaseElement {

    public MSTimer timer = new MSTimer();
    public int kills = 0;

    public GameStatsElement() {
        super(0.5, 0.5, true);
    }

    public void start() {
        //mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
    }

    public void stop() {
        //mc.entityRenderer.stopUseShader();
    }

    public void reset() {
        timer.reset();
        kills = 0;
    }

    @Override
    public void render() {
        RenderUtils.drawRoundedRect(this.getAbsoluteX(), this.getAbsoluteY(), this.getAbsoluteX() + this.getStringWidth("") + 150, this.getAbsoluteY() + 60, 5, new Color(0, 0, 0, 120).getRGB());

        this.drawString("Statistics", this.getAbsoluteX() + 50, this.getAbsoluteY() + 3	, Color.white.getRGB());

        HUDModule hudModule = Menace.instance.moduleManager.hudModule;
        int color = hudModule.color.getValue().equalsIgnoreCase("Custom") ? new Color(hudModule.red.getValueI(), hudModule.green.getValueI(), hudModule.blue.getValueI(), hudModule.alpha.getValueI()).getRGB() : ColorUtils.fade(hudModule.rainbowSpeed.getValueF(), -this.getAbsoluteY()).getRGB();
        RenderUtils.drawRect(this.getAbsoluteX(), this.getAbsoluteY() + 15, this.getAbsoluteX() + 150, this.getAbsoluteY() + 17, color);

        String username = Menace.instance.moduleManager.securityFeaturesModule.isToggled() ? Menace.instance.user.getUsername() : Minecraft.getMinecraft().getSession().getUsername();

        this.drawString("Username: " + username, this.getAbsoluteX() + 4, this.getAbsoluteY() + 20, Color.white.getRGB());

        LocalTime lt = LocalTime.ofSecondOfDay(timer.timePassed() / 1000);
        String second = lt.getSecond() < 10 ? "0" + lt.getSecond() : String.valueOf(lt.getSecond());
        String hour = lt.getHour() != 0 ? lt.getHour() + ":" : "";
        this.drawString("PlayTime: " + hour + lt.getMinute() + ":" + second, this.getAbsoluteX() + 4, this.getAbsoluteY() + this.getFontHeight() + 22, Color.white.getRGB());

        this.drawString("Kills: " + kills, this.getAbsoluteX() + 4, this.getAbsoluteY() + (this.getFontHeight() * 2) + 25, Color.white.getRGB());
    }

    @Override
    public void renderDummy() {

    }

    @Override
    public int getWidth() {
        return 150;
    }

    @Override
    public int getHeight() {
        return 60;
    }
}
