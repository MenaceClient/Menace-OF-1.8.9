package dev.menace.module.settings;

public class DividerSetting extends Setting {
    private boolean open = false;
    private final Setting[] settings;
    public DividerSetting(String name, boolean visible, Setting... settings) {
        super(name, visible);
        this.settings = settings;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Setting[] getSettings() {
        return settings;
    }
}
