package dev.menace.scripting.js;

import dev.menace.Menace;
import dev.menace.event.Event;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.scripting.js.mappings.*;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.security.StringGrabber;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JSRemapper {

    public static String remap(String script) {
        //script = remapEvents(script);
        script = remapScriptMngr(script);
        script = remapScript(script);
        script = remapModule(script);
        script = remapSliderSetting(script);
        script = remapToggleSetting(script);
        script = remapListSetting(script);
        script = remapPlayer(script);
        script = remapEventList(script);
        script = remapChatUtils(script);
        script = remapMovementUtils(script);
        script = remapRenderUtils(script);
        script = remapPacketUtils(script);
        return script;
    }

    /*private static String remapEvents(String script) {
        for (Class<? extends Event> clazz : Menace.instance.eventManager.getClasses()) {
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(MappedName.class)) {
                    MappedName mappedName = method.getAnnotation(MappedName.class);
                    String eventName = clazz.getAnnotation(JSMapping.class).value();
                }
            }
        }
        return script;
    }*/

    private static String remapScriptMngr(String script) {
        for (Method method : ScriptMngrMap.class.getMethods()) {
            if (method.isAnnotationPresent(MappedName.class)) {
                MappedName mappedName = method.getAnnotation(MappedName.class);
                script = script.replaceAll("ScriptManager." + StringGrabber.getString(mappedName.value()), "ScriptManager." + method.getName());
            }
        }
        return script;
    }

    private static String remapScript(String script) {
        for (Method method : ScriptMap.class.getMethods()) {
            if (method.isAnnotationPresent(MappedName.class)) {
                MappedName mappedName = method.getAnnotation(MappedName.class);
                script = script.replaceAll("Script." + StringGrabber.getString(mappedName.value()), "Script." + method.getName());
            }
        }
        return script;
    }

    private static String remapModule(String script) {
        for (Method method : ModuleMap.class.getMethods()) {
            if (method.isAnnotationPresent(MappedName.class)) {
                MappedName mappedName = method.getAnnotation(MappedName.class);
                script = script.replaceAll("Module." + StringGrabber.getString(mappedName.value()), "Module." + method.getName());
            }
        }
        return script;
    }

    private static String remapSliderSetting(String script) {
        for (Method method : SliderSetting.class.getMethods()) {
            if (method.isAnnotationPresent(MappedName.class)) {
                MappedName mappedName = method.getAnnotation(MappedName.class);
                script = script.replaceAll("SliderSetting." + StringGrabber.getString(mappedName.value()), "SliderSetting." + method.getName());
            }
        }
        return script;
    }

    private static String remapToggleSetting(String script) {
        for (Method method : ToggleSetting.class.getMethods()) {
            if (method.isAnnotationPresent(MappedName.class)) {
                MappedName mappedName = method.getAnnotation(MappedName.class);
                script = script.replaceAll("BooleanSetting." + StringGrabber.getString(mappedName.value()), "BooleanSetting." + method.getName());
            }
        }
        return script;
    }

    private static String remapListSetting(String script) {
        for (Method method : ListSetting.class.getMethods()) {
            if (method.isAnnotationPresent(MappedName.class)) {
                MappedName mappedName = method.getAnnotation(MappedName.class);
                script = script.replaceAll("ListSetting." + StringGrabber.getString(mappedName.value()), "ListSetting." + method.getName());
            }
        }
        return script;
    }

    private static String remapPlayer(String script) {
        for (Method method : PlayerMap.class.getMethods()) {
            if (method.isAnnotationPresent(MappedName.class)) {
                MappedName mappedName = method.getAnnotation(MappedName.class);
                script = script.replaceAll("Player." + StringGrabber.getString(mappedName.value()), "Player." + method.getName());
            }
        }
        return script;
    }

    private static String remapEventList(String script) {
        for (Field field : EventListMap.class.getFields()) {
            if (field.isAnnotationPresent(MappedName.class)) {
                MappedName mappedName = field.getAnnotation(MappedName.class);
                script = script.replaceAll("EventList." + StringGrabber.getString(mappedName.value()).toUpperCase(), "EventList." + field.getName());
            }
        }
        return script;
    }

    private static String remapChatUtils(String script) {
        for (Method method : ChatUtils.class.getMethods()) {
            if (method.isAnnotationPresent(MappedName.class)) {
                MappedName mappedName = method.getAnnotation(MappedName.class);
                script = script.replaceAll("ChatUtils." + StringGrabber.getString(mappedName.value()), "ChatUtils." + method.getName());
            }
        }
        return script;
    }

    private static String remapMovementUtils(String script) {
        for (Method method : MovementUtils.class.getMethods()) {
            if (method.isAnnotationPresent(MappedName.class)) {
                MappedName mappedName = method.getAnnotation(MappedName.class);
                script = script.replaceAll("MovementUtils." + StringGrabber.getString(mappedName.value()), "MovementUtils." + method.getName());
            }
        }
        return script;
    }

    private static String remapRenderUtils(String script) {
        for (Method method : RenderUtils.class.getMethods()) {
            if (method.isAnnotationPresent(MappedName.class)) {
                MappedName mappedName = method.getAnnotation(MappedName.class);
                script = script.replaceAll("RenderUtils." + StringGrabber.getString(mappedName.value()), "RenderUtils." + method.getName());
            }
        }
        return script;
    }

    private static String remapPacketUtils(String script) {
        for (Method method : PacketUtilsMap.class.getMethods()) {
            if (method.isAnnotationPresent(MappedName.class)) {
                MappedName mappedName = method.getAnnotation(MappedName.class);
                script = script.replaceAll("PacketUtils." + StringGrabber.getString(mappedName.value()), "PacketUtils." + method.getName());
            }
        }
        return script;
    }

}
