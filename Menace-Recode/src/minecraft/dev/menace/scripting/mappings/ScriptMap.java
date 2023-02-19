package dev.menace.scripting.mappings;

import dev.menace.scripting.ScriptElement;
import dev.menace.scripting.ScriptModule;

public class ScriptMap {

    String scriptName;

    public ScriptMap() {}

    public ScriptMap(String scriptName) {
        this.scriptName = scriptName;
    }

    public ModuleMap registerModule(String name, String description) {
        ScriptModule module = new ScriptModule(name, description);
        return new ModuleMap(module);
    }

    public ElementMap registerHudElement(int posX, int posY, int width, int height, boolean visible) {
        ScriptElement element = new ScriptElement(posX, posY, visible);
        element.setWidth(width);
        element.setHeight(height);
        return new ElementMap(element, scriptName);
    }

}
