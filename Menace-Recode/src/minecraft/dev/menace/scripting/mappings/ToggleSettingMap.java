package dev.menace.scripting.mappings;

import dev.menace.module.settings.ToggleSetting;

public class ToggleSettingMap {

    private final ToggleSetting setting;

    public ToggleSettingMap(String name, boolean visible, boolean toggled) {
        setting = new ToggleSetting(name, visible, toggled);
    }

    public boolean getValue() {
        return setting.getValue();
    }

    public void setValue(boolean toggled) {
        setting.setValue(toggled);
    }

    public ToggleSetting getSetting() {
        return setting;
    }

}
