package dev.menace.ui.hud.options;

import dev.menace.Menace;
import dev.menace.utils.render.font.MenaceFontRenderer;

public abstract class BaseOption {

    private final String name;
    MenaceFontRenderer fontRenderer = Menace.instance.sfPro;

    public BaseOption(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void render(double posX, double posY);

    public abstract void mouseClicked(double posX, double posY, int mouseX, int mouseY, int mouseButton);

    public void keyTyped(char typedChar, int keyCode) {

    }

    public void update() {

    }

    public abstract int getHeight();

}
