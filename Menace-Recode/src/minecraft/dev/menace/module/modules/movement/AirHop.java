package dev.menace.module.modules.movement;

import dev.menace.module.BaseModule;
import dev.menace.module.Category;

public class AirHop extends BaseModule {
    public AirHop() {
        super("AirHop", "hops in the air", Category.MOVEMENT, 0);
    }
    @Override
    public void onUpdate() {
        if(!this.isToggled()) return;
        if(mc.gameSettings.keyBindJump.isPressed()){
            mc.thePlayer.motionX *= 1.5;
            mc.thePlayer.motionY = 0.4;
            mc.thePlayer.motionZ *= 1.5;
            mc.thePlayer.onGround =  true;
        }
        super.onUpdate();
    }

    @Override
    public String getValue() {
        return null;
    }
}
