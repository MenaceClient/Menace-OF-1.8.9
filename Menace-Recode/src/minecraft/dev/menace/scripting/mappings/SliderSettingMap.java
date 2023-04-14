package dev.menace.scripting.mappings;

import dev.menace.module.settings.SliderSetting;

public class SliderSettingMap {

    private final SliderSetting setting;

    public SliderSettingMap(String name, boolean visible, double value, double min, double max, boolean intOnly) {
        setting = new SliderSetting(name, visible, value, min, max, intOnly);
    }

    public SliderSettingMap(String name, boolean visible, double value, double min, double max, double increment, boolean intOnly) {
        setting = new SliderSetting(name, visible, value, min, max, increment, intOnly);
    }

    public double getMin() {
        return setting.getMin();
    }

    public double getMax() {
        return setting.getMax();
    }

    public boolean onlyInt() {
        return setting.onlyInt();
    }

    public double getValue() {
        return setting.getValue();
    }

    public float getValueF() {
        return (float) setting.getValueF();
    }

    public long getValueL() {
        return (long) setting.getValueL();
    }

    public int getValueI() {
        return (int) setting.getValueI();
    }

    public void setValue(double value) {
        setting.setValue(value);
    }

    public double getIncrement() {
        return setting.getIncrement();
    }

    public SliderSetting getSetting() {
        return setting;
    }

}
