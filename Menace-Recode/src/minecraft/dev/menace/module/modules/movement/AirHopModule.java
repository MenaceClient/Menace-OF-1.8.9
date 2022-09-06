package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;

public class AirHopModule extends BaseModule {
    public AirHopModule() {
        super("AirHop", Category.MOVEMENT, 0);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if(mc.gameSettings.keyBindJump.isPressed()){
            mc.thePlayer.motionX *= 1.5;
            mc.thePlayer.motionY = 0.4;
            mc.thePlayer.motionZ *= 1.5;
            mc.thePlayer.onGround =  true;
        }
    }

}
