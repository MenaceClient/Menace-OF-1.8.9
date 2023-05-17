package dev.menace.ui.hud.elements;

import dev.menace.ui.hud.BaseElement;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.awt.*;
import java.util.ArrayList;

public class PingElement extends BaseElement {

    ArrayList<Integer> pingList = new ArrayList<>();

    public PingElement() {
        super(0.5, 0.5, true);
    }

    @Override
    public void render() {
        if (mc.theWorld == null || mc.thePlayer == null) {
            pingList.clear();
            return;
        }

        final NetworkPlayerInfo you = this.mc.getNetHandler().getPlayerInfo(this.mc.thePlayer.getUniqueID());

        if (you == null) {
            pingList.clear();
            return;
        }
        this.drawString("Ping: " + you.getResponseTime() + "ms", this.getAbsoluteX(), this.getAbsoluteY());

        if (pingList.size() > 100) {
            pingList.remove(0);
        }

        /*RenderUtils.drawHollowRect(this.getAbsoluteX(), this.getAbsoluteY(), this.getAbsoluteX() + 100, this.getAbsoluteY() + 50, 1, -1);

        pingList.add(you.getResponseTime());

        if (pingList.size() > 100) {
            pingList.remove(0);
        }

        //draw average line
        int average = 0;
        for (int i : pingList) {
            average += i;
        }
        average /= pingList.size();



        RenderUtils.drawHorizontalLine(this.getAbsoluteX() + 1, this.getAbsoluteX() + 98, averageY, Color.GREEN.getRGB());*/
    }

    @Override
    public void renderDummy() {
        this.drawString("Ping: 255ms", this.getAbsoluteX(), this.getAbsoluteY());
    }

    @Override
    public int getWidth() {
        return this.getStringWidth("Ping: 255ms");
    }

    @Override
    public int getHeight() {
        return this.getFontHeight();
    }
}
