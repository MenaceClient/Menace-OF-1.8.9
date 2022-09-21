package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventPreMotion;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.MovementUtils;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.MathHelper;
import org.jetbrains.annotations.NotNull;

public class SpeedModule extends BaseModule {

    private int flagCount;

    ListSetting mode;
    SliderSetting speed;
    ToggleSetting autoDisable;

    public SpeedModule() {
        super("Speed", Category.MOVEMENT, 0);
    }

    @Override
    public void setup() {
        mode = new ListSetting("Mode", true, "BHop", new String[] {"BHop", "Strafe", "LowHop"});
        speed = new SliderSetting("Speed", true, 1, 1, 10, true);
        autoDisable = new ToggleSetting("AutoDisable", true, true);
        this.rSetting(mode);
        this.rSetting(speed);
        this.rSetting(autoDisable);
        super.setup();
    }

    @Override
    public void onEnable() {
        flagCount = 0;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindJump.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindJump);
        super.onDisable();
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (mode.getValue().equalsIgnoreCase("Strafe")) {
            if (!MovementUtils.shouldMove()) {
                return;
            }
            mc.gameSettings.keyBindJump.pressed = false;
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                MovementUtils.strafe();
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

    @EventTarget
    public void onMove(EventMove event) {
        if (mode.getValue().equalsIgnoreCase("LowHop")) {
            if (!MovementUtils.isMoving()) return;
            mc.gameSettings.keyBindJump.pressed = false;
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                mc.thePlayer.motionY = 0.0;
                MovementUtils.strafe(0.61F);
                event.setY(0.41999998688698);
            }
            MovementUtils.strafe();
        }

    }

    @EventTarget
    public void onRecievePacket(@NotNull EventReceivePacket event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            flagCount++;

            if (flagCount >= 3) {
                this.toggle();
            }
        }
    }

}
