package dev.menace.ui.hud.elements;

import dev.menace.Menace;
import dev.menace.ui.hud.BaseElement;

public class UserInfoElement extends BaseElement {

    public UserInfoElement() {
        super(0.5, 0.5, true);
    }

    @Override
    public void render() {
        this.drawString(Menace.instance.user.getUsername() + " | " + Menace.instance.user.getUID() + " | " + Menace.instance.buildName, this.getAbsoluteX(), this.getAbsoluteY());
    }

    @Override
    public void renderDummy() {
        this.drawString(Menace.instance.user.getUsername() + " | " + Menace.instance.user.getUID() + " | " + Menace.instance.buildName, this.getAbsoluteX(), this.getAbsoluteY());
    }

    @Override
    public int getWidth() {
        return this.getStringWidth(Menace.instance.user.getUsername() + " | " + Menace.instance.user.getUID() + " | " + Menace.instance.buildName);
    }

    @Override
    public int getHeight() {
        return this.getFontHeight();
    }
}
