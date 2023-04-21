package dev.menace.module.config;

import dev.menace.utils.file.FileManager;
import dev.menace.utils.misc.ChatUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class ConfigManager {

    private Config loadedConfig;

    private List<Config> configs;

    public ConfigManager() {
    }

    public void reload() {
        configs = grabConfigs();
    }

    private List<Config> grabConfigs() {
        List<Config> list = new ArrayList<>();

        for (File file : Objects.requireNonNull(FileManager.getConfigFolder().listFiles())) {
            String fileName = file.getName().replaceAll(".json", "");
            if (!fileName.equals("Binds")) {
                list.add(new Config(fileName));
            }
        }

       return list;
    }

    public List<Config> getConfigs() {
        reload();
        return configs;
    }

    public Config getLoadedConfig() {
        return loadedConfig;
    }

    public void setLoadedConfig(Config loadedConfig) {
        this.loadedConfig = loadedConfig;
    }

    public Optional<Config> getConfigByName(String name) {
        return getConfigs().stream().filter(c -> c.getName().equalsIgnoreCase(name)).findFirst();
    }

}
