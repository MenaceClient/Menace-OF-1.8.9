package dev.menace.scripting.mappings;

import dev.menace.module.settings.ListSetting;

public class ListSettingMap {

    private final ListSetting setting;

    public ListSettingMap(String name, boolean visible, String value, String[] options) {
        setting = new ListSetting(name, visible, value, options);
    }

    public String getValue() {
        return setting.getValue();
    }

    public void setValue(String value) {
        setting.setValue(value);
    }

    public String[] getOptions() {
        return setting.getOptions();
    }

    public String getDefaultValue() {
        return setting.getDefaultValue();
    }

    public ListSetting getSetting() {
        return setting;
    }

}
