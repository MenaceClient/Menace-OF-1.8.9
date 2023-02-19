package dev.menace.scripting;

import dev.menace.Menace;
import dev.menace.event.Event;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.scripting.mappings.*;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.render.RenderUtils;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import net.minecraft.client.Minecraft;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Script {

    private final ScriptEngine scriptEngine;
    private final Invocable invocable;
    private final File scriptFile;

    public Script(File file) throws IOException, ScriptException {
        this.scriptEngine = new NashornScriptEngineFactory().getScriptEngine();
        this.invocable = (Invocable) scriptEngine;
        this.scriptFile = file;
        addVars();

        Path path = Paths.get(scriptFile.getPath());
        String script = new String(Files.readAllBytes(path));

        script = script.replaceAll("import\\s.*;", "");


        scriptEngine.eval(script);

    }

    private void addVars() {
        scriptEngine.put("ScriptManager", StaticClass.forClass(ScriptMngrMap.class));
        scriptEngine.put("Script", StaticClass.forClass(ScriptMap.class));
        scriptEngine.put("Module", StaticClass.forClass(ModuleMap.class));
        scriptEngine.put("HudElement", StaticClass.forClass(ElementMap.class));
        scriptEngine.put("SliderSetting", StaticClass.forClass(SliderSetting.class));
        scriptEngine.put("BooleanSetting", StaticClass.forClass(ToggleSetting.class));
        scriptEngine.put("ListSetting", StaticClass.forClass(ListSetting.class));
        scriptEngine.put("Player", StaticClass.forClass(PlayerMap.class));
        scriptEngine.put("EventList", StaticClass.forClass(EventListMap.class));
        scriptEngine.put("ChatUtils", StaticClass.forClass(ChatUtils.class));
        scriptEngine.put("MovementUtils", StaticClass.forClass(MovementUtils.class));
        scriptEngine.put("RenderUtils", StaticClass.forClass(RenderUtils.class));
        scriptEngine.put("mc", StaticClass.forClass(Minecraft.getMinecraft().getClass()));

        //List all Event classes and bind them
        for (Class<? extends Event> clazz : Menace.instance.eventManager.getClasses()) {
            scriptEngine.put(clazz.getSimpleName(), StaticClass.forClass(clazz));
        }

    }

}
