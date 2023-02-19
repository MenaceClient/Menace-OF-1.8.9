package dev.menace.scripting.mappings;

public class ScriptMngrMap {

    String name;
    String version;
    String[] authors;

    public ScriptMngrMap(String name, String version, String[] authors) {
        this.name = name;
        this.version = version;
        this.authors = authors;
    }

    public ScriptMap getScript() {
        System.out.println("[Menace] ScriptManager: Loaded " + name + " v" + version + " by " + authors[0] + ".");
        return new ScriptMap(name);
    }

}
