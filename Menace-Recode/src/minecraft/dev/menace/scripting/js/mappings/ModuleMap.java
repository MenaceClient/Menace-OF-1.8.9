package dev.menace.scripting.js.mappings;

import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.scripting.ScriptModule;
import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.LinkedHashMap;

@JSMapping
public class ModuleMap {

    public ScriptModule module;
    public LinkedHashMap<String, ScriptObjectMirror> events = new LinkedHashMap<>();

    public ModuleMap() {}

    public ModuleMap(ScriptModule module) {
        this.module = module;
        module.setModuleMap(this);
    }

    @MappedName(10)
    public SliderSetting addSliderSetting(String name, boolean visible, double value, double min, double max, boolean intOnly) {
        SliderSetting setting = new SliderSetting(name, visible, value, min, max, intOnly);
        module.rSetting(setting);
        return setting;
    }

    @MappedName(11)
    public ToggleSetting addBooleanSetting(String name, boolean visible, boolean value) {
        ToggleSetting setting = new ToggleSetting(name, visible, value);
        module.rSetting(setting);
        return setting;
    }

    @MappedName(12)
    public ListSetting addListSetting(String name, boolean visible, String value, String[] options) {
        ListSetting setting = new ListSetting(name, visible, value, options);
        module.rSetting(setting);
        return setting;
    }

    @MappedName(13)
    public void hook(String event, ScriptObjectMirror callback) {
        events.put(event, callback);
    }

    @MappedName(14)
    public void toggle(boolean enabled) {
        module.setToggled(enabled);
    }

}
