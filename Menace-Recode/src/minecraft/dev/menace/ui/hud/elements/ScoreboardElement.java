package dev.menace.ui.hud.elements;

import dev.menace.ui.hud.BaseElement;

public class ScoreboardElement extends BaseElement {

    int width = 0;
    int height = 0;

    public ScoreboardElement() {
        super(0.5, 0.5, true);
    }

    @Override
    public void render() {

    }

    @Override
    public void renderDummy() {
        //this.drawString("Scoreboard", this.getAbsoluteX(), this.getAbsoluteY(), -1);
    }

    @Override
    public int getFontHeight() {
        return super.getFontHeight();
    }

    @Override
    public int getStringWidth(String string) {
        return super.getStringWidth(string);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
