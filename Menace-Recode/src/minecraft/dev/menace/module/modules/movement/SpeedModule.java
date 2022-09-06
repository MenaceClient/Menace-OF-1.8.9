package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.utils.player.MovementUtils;
import net.minecraft.client.settings.GameSettings;

public class SpeedModule extends BaseModule {

    ListSetting mode;
    SliderSetting speed;

    public SpeedModule() {
        super("Speed", Category.MOVEMENT, 0);
    }

    @Override
    public void setup() {
        mode = new ListSetting("Mode", true, "BHop", new String[] {"BHop", "Strafe"});
        speed = new SliderSetting("Speed", true, 1, 1, 10, true);
        this.rSetting(mode);
        this.rSetting(speed);
        super.setup();
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindJump.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindJump);
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mode.getValue().equalsIgnoreCase("Strafe")) {
            if (!MovementUtils.shouldMove()) {
                return;
            }
            mc.gameSettings.keyBindJump.pressed = false;
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                MovementUtils.strafe(MovementUtils.getSpeed());
            }
            MovementUtils.strafe();
        } else if (mode.getValue().equalsIgnoreCase("BHop")) {
            if (!MovementUtils.shouldMove()) return;
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                MovementUtils.strafe(speed.getValueF() / 2);
            }
            MovementUtils.strafe();
        }
    }

}
