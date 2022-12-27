package dev.menace.module.modules.render;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.SliderSetting;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import org.jetbrains.annotations.NotNull;

public class TimeChangerModule extends BaseModule {

    SliderSetting time;

    public TimeChangerModule() {
        super("TimeChanger", Category.RENDER, 0);
    }

    @Override
    public void setup() {
        time = new SliderSetting("Time", true, 100, 0, 20000, 100, true);
        this.rSetting(time);
        super.setup();
    }

    @EventTarget
    public void onRecieve(@NotNull EventReceivePacket event) {
        if (event.getPacket() instanceof S03PacketTimeUpdate) {
            event.cancel();
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        mc.theWorld.getWorldInfo().setWorldTime(time.getValueL());
    }

}
