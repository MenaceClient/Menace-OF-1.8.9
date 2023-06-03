package dev.menace.module.settings;

public class DividerSetting extends Setting {
    private boolean open = false;
    public DividerSetting(String name, boolean visible) {
        super(name, visible);
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

}
