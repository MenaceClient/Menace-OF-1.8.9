package dev.menace.scripting;

import dev.menace.Menace;
import dev.menace.event.Event;
import dev.menace.event.events.*;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.JSRemapper;
import dev.menace.scripting.js.mappings.*;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.security.StringGrabber;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        //Remap Script
        script = JSRemapper.remap(script);

        scriptEngine.eval(script);

    }

    private void addVars() {

        //Funny Obf thing mby
        Map<Class<?>, String> classes = new HashMap<>();
        classes.put(ScriptMngrMap.class, "ScriptManager");
        classes.put(ScriptMap.class, "Script");
        classes.put(ModuleMap.class, "Module");
        classes.put(SliderSetting.class, "SliderSetting");
        classes.put(ToggleSetting.class, "BooleanSetting");
        classes.put(ListSetting.class, "ListSetting");
        classes.put(PlayerMap.class, "Player");
        classes.put(EventListMap.class, "EventList");
        classes.put(ChatUtils.class, "ChatUtils");
        classes.put(MovementUtils.class, "MovementUtils");
        classes.put(RenderUtils.class, "RenderUtils");
        classes.put(PacketUtilsMap.class, "PacketUtils");

        //Events
        classes.put(EventAttack.class, StringGrabber.getString(EventAttack.class.getAnnotation(JSMapping.class).value()));
        classes.put(EventChatOutput.class, StringGrabber.getString(EventChatOutput.class.getAnnotation(JSMapping.class).value()));
        classes.put(EventCollide.class, StringGrabber.getString(EventCollide.class.getAnnotation(JSMapping.class).value()));
        classes.put(EventConnection.class, StringGrabber.getString(EventConnection.class.getAnnotation(JSMapping.class).value()));
        classes.put(EventDeath.class, StringGrabber.getString(EventDeath.class.getAnnotation(JSMapping.class).value()));
        classes.put(EventJump.class, StringGrabber.getString(EventJump.class.getAnnotation(JSMapping.class).value()));
        classes.put(EventKey.class, StringGrabber.getString(EventKey.class.getAnnotation(JSMapping.class).value()));
        classes.put(EventMouse.class, StringGrabber.getString(EventMouse.class.getAnnotation(JSMapping.class).value()));
        classes.put(EventMove.class, StringGrabber.getString(EventMove.class.getAnnotation(JSMapping.class).value()));
        classes.put(EventPostMotion.class, StringGrabber.getString(EventPostMotion.class.getAnnotation(JSMapping.class).value()));
        classes.put(EventPreMotion.class, StringGrabber.getString(EventPreMotion.class.getAnnotation(JSMapping.class).value()));
        classes.put(EventReceivePacket.class, StringGrabber.getString(EventReceivePacket.class.getAnnotation(JSMapping.class).value()));
        classes.put(EventRender2D.class, StringGrabber.getString(EventRender2D.class.getAnnotation(JSMapping.class).value()));
        classes.put(EventRender3D.class, StringGrabber.getString(EventRender3D.class.getAnnotation(JSMapping.class).value()));
        //classes.put(EventRenderNametag.class, StringGrabber.getString(EventRenderNametag.class.getAnnotation(JSMapping.class).value()));
        classes.put(EventSendPacket.class, StringGrabber.getString(EventSendPacket.class.getAnnotation(JSMapping.class).value()));
        classes.put(EventSlowdown.class, StringGrabber.getString(EventSlowdown.class.getAnnotation(JSMapping.class).value()));
        //classes.put(EventSpawnEntity.class, StringGrabber.getString(EventSpawnEntity.class.getAnnotation(JSMapping.class).value()));
        classes.put(EventStep.class, StringGrabber.getString(EventStep.class.getAnnotation(JSMapping.class).value()));
        classes.put(EventTeleport.class, StringGrabber.getString(EventTeleport.class.getAnnotation(JSMapping.class).value()));
        classes.put(EventUpdate.class, StringGrabber.getString(EventUpdate.class.getAnnotation(JSMapping.class).value()));
        classes.put(EventWorldChange.class, StringGrabber.getString(EventWorldChange.class.getAnnotation(JSMapping.class).value()));

        //Im retarded and should have just done it with scriptEngine.put("name", object); but too late now
        for (Map.Entry<Class<?>, String> entry : classes.entrySet()) {
            scriptEngine.put(entry.getValue(), StaticClass.forClass(entry.getKey()));
        }

        //Not obfed
        scriptEngine.put("GL11", StaticClass.forClass(org.lwjgl.opengl.GL11.class));

    }

}
