package dev.menace.ui.hud.elements;

import dev.menace.ui.hud.BaseElement;
import dev.menace.utils.render.RenderUtils;

import java.awt.*;

public class NotificationElement extends BaseElement {
    public NotificationElement() {
        super(0.5, 0.5, true);
    }

    @Override
    public void render() {
        RenderUtils.drawRoundedRect((float)this.getAbsoluteX(), (float)this.getAbsoluteY(), (float)this.getAbsoluteX() + 10f,
                (float)this.getAbsoluteY() + 5f, 3f, Color.black.getRGB());
    }

    @Override
    public void renderDummy() {

    }

    @Override
    public int getWidth() {
        return 30;
    }

    @Override
    public int getHeight() {
        return 10;
    }
}
