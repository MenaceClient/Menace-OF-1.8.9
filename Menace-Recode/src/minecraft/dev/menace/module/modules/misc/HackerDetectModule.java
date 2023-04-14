package dev.menace.module.modules.misc;

import dev.menace.anticheat.HackerDetect;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;

public class HackerDetectModule extends BaseModule {

    public HackerDetectModule() {
        super("MenaceNCP", Category.MISC, 0);
    }

    @EventTarget
    public void onEvent(EventUpdate event) {
        HackerDetect.updateChecks();
    }

}
