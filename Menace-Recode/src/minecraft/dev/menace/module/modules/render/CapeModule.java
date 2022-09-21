package dev.menace.module.modules.render;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.DontSaveState;
import dev.menace.module.settings.ListSetting;
import net.minecraft.util.ResourceLocation;

@DontSaveState
public class CapeModule extends BaseModule {

    ListSetting capes;

    public CapeModule() {
        super("Cape", Category.RENDER, 0);
    }

    @Override
    public void setup() {
        capes = new ListSetting("Capes", true, "Technoblade", new String[] {"Technoblade", "Menace", "Menace2", "Verus", "HackDonalds", "Laugh"});
        this.rSetting(capes);
        super.setup();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.thePlayer.getLocationOfCape() == null) {
            mc.thePlayer.setLocationOfCape(new ResourceLocation("menace/capes/" + capes.getValue() + ".png"));
        }
    }

    @Override
    public void onDisable() {
        mc.thePlayer.setLocationOfCape(null);
        super.onDisable();
    }
}
