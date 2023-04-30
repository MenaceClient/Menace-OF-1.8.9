package dev.menace.scripting;

import dev.menace.event.Event;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventAll;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.scripting.js.mappings.ModuleMap;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class ScriptModule extends BaseModule {

    ModuleMap moduleMap;

    public ScriptModule(String name, String description) {
        super(name, description, Category.SCRIPTS, 0);
    }

    public void setModuleMap(ModuleMap moduleMap) {
    	this.moduleMap = moduleMap;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        moduleMap.events.forEach((event, method) -> {
        	if (event.equals("onEnable")) {
        		this.callEvent(method, new EventUpdate());
        	}
        });
    }

    @Override
    public void onDisable() {
        super.onDisable();
        moduleMap.events.forEach((event, method) -> {
            if (event.equals("onDisable")) {
                this.callEvent(method, new EventUpdate());
            }
        });
    }

    @EventTarget
    public void onEvent(EventAll event) {
        moduleMap.events.forEach((eventName, method) -> {
            if (eventName.equals(event.getEvent().getClass().getSimpleName().replaceFirst("E", "e"))) {
                this.callEvent(method, event.getEvent());
            }
        });
    }

    private void callEvent(ScriptObjectMirror method, Event event) {
    	try {
    		method.call(moduleMap, event);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

}
