package dev.menace.ui.hud.elements;

import dev.menace.ui.hud.BaseElement;
import net.minecraft.client.network.NetworkPlayerInfo;

public class PingElement extends BaseElement {

    public PingElement() {
        super(0.5, 0.5, true);
    }

    @Override
    public void render() {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        final NetworkPlayerInfo you = this.mc.getNetHandler().getPlayerInfo(this.mc.thePlayer.getUniqueID());
        if (you == null) return;
        this.drawString("Ping: " + you.getResponseTime() + "ms", this.getAbsoluteX(), this.getAbsoluteY());
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
