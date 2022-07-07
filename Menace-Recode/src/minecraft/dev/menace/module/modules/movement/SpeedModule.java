package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.SliderSetting;
import dev.menace.utils.player.MovementUtils;

public class SpeedModule extends BaseModule {

    SliderSetting speed;

    public SpeedModule() {
        super("Speed", Category.MOVEMENT, 0);
    }

    @Override
    public void setup() {
        speed = new SliderSetting("Speed", true, 1, 1, 10, 0.1, false);
        this.rSetting(speed);
        super.setup();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!MovementUtils.shouldMove()) return;
        if (MC.thePlayer.onGround) {
            MC.thePlayer.jump();
            MovementUtils.strafe(speed.getValue() == 1 ? MovementUtils.getSpeed() : (speed.getValueF() / 10));
        }

        MovementUtils.strafe();
    }

}
