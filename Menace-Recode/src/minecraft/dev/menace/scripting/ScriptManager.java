package dev.menace.scripting;

import dev.menace.Menace;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.ui.hud.BaseElement;
import dev.menace.ui.hud.HUDManager;
import dev.menace.utils.file.FileManager;

import javax.script.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ScriptManager {

    private final ArrayList<Script> scripts = new ArrayList<>();

    public ScriptManager() {

        for (File file : Objects.requireNonNull(FileManager.getScriptFolder().listFiles())) {
            if (file.exists() && !file.isDirectory() && file.getName().endsWith(".js")) {
                try {
                    scripts.add(new Script(file));
                } catch (ScriptException | IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void reloadScripts() {
        scripts.clear();

        //Clear modules
        for (BaseModule module : Menace.instance.moduleManager.getModulesByCategory(Category.SCRIPTS)) {
            Menace.instance.moduleManager.removeModule(module);
        }

        //Clear HUD Elements
        HUDManager.hudElements.removeIf(element -> element instanceof ScriptElement);


        for (File file : Objects.requireNonNull(FileManager.getScriptFolder().listFiles())) {
            if (file.exists() && !file.isDirectory() && file.getName().endsWith(".js")) {
                try {
                    scripts.add(new Script(file));
                } catch (ScriptException | IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
