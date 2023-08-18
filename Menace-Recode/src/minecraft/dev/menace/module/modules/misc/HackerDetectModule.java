package dev.menace.module.modules.misc;

import dev.menace.anticheat.HackerDetect;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ToggleSetting;

public class HackerDetectModule extends BaseModule {

    public ToggleSetting showVl;

    public HackerDetectModule() {
        super("MenaceNCP", "Gotta catch them all", Category.MISC, 0);
    }

    @Override
    public void setup() {
        showVl = new ToggleSetting("Show VL", true, true);
        this.rSetting(showVl);
        super.setup();
    }

    @EventTarget
    public void onEvent(EventUpdate event) {
        HackerDetect.updateChecks();
    }

}
