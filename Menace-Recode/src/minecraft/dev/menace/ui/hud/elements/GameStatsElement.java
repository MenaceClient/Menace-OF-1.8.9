package dev.menace.ui.hud.elements;

import dev.menace.Menace;
import dev.menace.event.events.EventRender2D;
import dev.menace.ui.hud.BaseElement;
import dev.menace.ui.hud.options.BooleanOption;
import dev.menace.ui.hud.options.ColorSelectOption;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.time.LocalTime;

public class GameStatsElement extends BaseElement {

    private Color color;
    private boolean customFont;

    public MSTimer timer;
    public int kills = 0;

    public GameStatsElement() {
        timer = new MSTimer();
        reset();
    }

    public void reset() {
        timer.reset();
        kills = 0;
    }

    @Override
    public void setup() {
        this.addOption(new BooleanOption("Custom Font", false) {
            @Override
            public void update() {
                GameStatsElement.this.customFont = this.getValue();
            }
        });
        this.addOption(new ColorSelectOption("Color", Color.red) {
            @Override
            public void update() {
                GameStatsElement.this.color = this.getColor();
            }
        });
    }

    @Override
    public void render() {
        RenderUtils.drawRoundedRect((float) this.getPosX(), (float) this.getPosY(), (float) (this.getPosX() + getWidth()), (float) (this.getPosY() + getHeight()), 5, new Color(0, 0, 0, 120).getRGB());

        this.drawString("Statistics", this.getPosX() + 50, this.getPosY() + 3, Color.WHITE.getRGB(), this.customFont);

        RenderUtils.drawRect(this.getPosX(), this.getPosY() + 15, this.getPosX() + 150, this.getPosY() + 17, color.getRGB());

        String username = Menace.instance.moduleManager.securityFeaturesModule.isToggled() ? Menace.instance.user.getUsername() : Minecraft.getMinecraft().getSession().getUsername();

        this.drawString("Username: " + username, this.getPosX() + 4, this.getPosY() + 20, Color.WHITE.getRGB(), this.customFont);

        LocalTime lt = LocalTime.ofSecondOfDay(timer.timePassed() / 1000);
        String second = lt.getSecond() < 10 ? "0" + lt.getSecond() : String.valueOf(lt.getSecond());
        String hour = lt.getHour() != 0 ? lt.getHour() + ":" : "";
        this.drawString("PlayTime: " + hour + lt.getMinute() + ":" + second, this.getPosX() + 4, this.getPosY() + this.getFontHeight(customFont) + 22, Color.WHITE.getRGB(), this.customFont);

        this.drawString("Kills: " + kills, this.getPosX() + 4, this.getPosY() + (this.getFontHeight(customFont) * 2) + 25, Color.WHITE.getRGB(), this.customFont);

    }

    @Override
    public void renderDummy() {
        RenderUtils.drawRoundedRect((float) this.getPosX(), (float) this.getPosY(), (float) (this.getPosX() + getWidth()), (float) (this.getPosY() + getHeight()), 5, new Color(0, 0, 0, 120).getRGB());

        this.drawString("Statistics", this.getPosX() + 50, this.getPosY() + 3, Color.WHITE.getRGB(), this.customFont);

        RenderUtils.drawRect(this.getPosX(), this.getPosY() + 15, this.getPosX() + 150, this.getPosY() + 17, color.getRGB());

        this.drawString("Username: " + Menace.instance.user.getUsername(), this.getPosX() + 4, this.getPosY() + 20, Color.WHITE.getRGB(), this.customFont);

        this.drawString("PlayTime: 1:00:00", this.getPosX() + 4, this.getPosY() + this.getFontHeight(customFont) + 22, Color.WHITE.getRGB(), this.customFont);

        this.drawString("Kills: 69", this.getPosX() + 4, this.getPosY() + (this.getFontHeight(customFont) * 2) + 25, Color.WHITE.getRGB(), this.customFont);
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
