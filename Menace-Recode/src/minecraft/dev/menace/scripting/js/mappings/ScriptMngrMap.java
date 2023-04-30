package dev.menace.scripting.js.mappings;

import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;

@JSMapping
public class ScriptMngrMap {

    String name;
    String version;
    String[] authors;

    public ScriptMngrMap(String name, String version, String[] authors) {
        this.name = name;
        this.version = version;
        this.authors = authors;
    }

    @MappedName(47)
    public ScriptMap getScript() {
        System.out.println("[Menace] ScriptManager: Loaded " + name + " v" + version + " by " + authors[0] + ".");
        return new ScriptMap(name);
    }

}
