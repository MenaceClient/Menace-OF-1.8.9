package dev.menace.module.config;

import dev.menace.Menace;
import dev.menace.module.ModuleManager;

public class Config {
    private String name;

    public Config(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void load() {
        if (Menace.instance.configManager.checkLoadedConfig(this)) return;

        Menace.instance.configManager.setLoadedConfig(this);
        Menace.instance.moduleManager.loadModules(this.name);
    }


}