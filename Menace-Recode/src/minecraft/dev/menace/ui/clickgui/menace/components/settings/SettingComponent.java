package dev.menace.ui.clickgui.menace.components.settings;

import dev.menace.Menace;
import dev.menace.module.settings.Setting;
import dev.menace.utils.render.font.MenaceFontRenderer;

public class SettingComponent {

    public Setting setting;
    protected final MenaceFontRenderer fontRenderer;

    public SettingComponent(Setting s) {
        setting = s;
        fontRenderer = Menace.instance.sfPro;
    }

    public void draw(int mouseX, int mouseY, int x, int y) {

    }

    public void mouseClicked(int mouseX, int mouseY, int x, int y, int mouseButton) {

    }

    public int getHeight() {
        return 0;
    }

}
