package dev.menace.ui.clickgui.dropdown.components.settings;

import dev.menace.Menace;
import dev.menace.module.settings.Setting;
import dev.menace.utils.render.font.MenaceFontRenderer;

public abstract class DropdownSetting {

    public Setting setting;
    protected int width = 125;
    protected final MenaceFontRenderer fontRenderer;

    public DropdownSetting(Setting setting) {
        this.setting = setting;
        fontRenderer = Menace.instance.sfPro;
    }

    public abstract void render(int x, int y, int mouseX, int mouseY);

    public abstract void mouseClicked(int x, int y, int mouseX, int mouseY, int mouseButton);

    public abstract int getHeight();

}
