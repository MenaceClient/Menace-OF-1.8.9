package dev.menace.module.config;

import dev.menace.Menace;
import dev.menace.module.ModuleManager;

public class Config {
    public String name;
    private boolean loaded = false;

    public Config(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void load() {
        if (this.loaded) return;

        if (Menace.instance.configManager.getLoadedConfig() != null) {
            Menace.instance.configManager.getLoadedConfig().setLoaded(false);
        }
        Menace.instance.configManager.setLoadedConfig(this);
        this.loaded = true;

        Menace.instance.moduleManager.loadModules(this.name);
    }


}