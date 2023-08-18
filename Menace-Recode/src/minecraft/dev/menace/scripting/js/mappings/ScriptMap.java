package dev.menace.scripting.js.mappings;

import dev.menace.scripting.ScriptModule;
import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;

@JSMapping
public class ScriptMap {

    String scriptName;

    public ScriptMap() {}

    public ScriptMap(String scriptName) {
        this.scriptName = scriptName;
    }

    @MappedName(45)
    public ModuleMap registerModule(String name, String description) {
        ScriptModule module = new ScriptModule(name, description);
        return new ModuleMap(module);
    }

}
