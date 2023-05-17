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
import dev.menace.utils.player.PacketUtils;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;

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
        mode = new ListSetting("Mode", true, "BHop", new String[] {"BHop", "Strafe", "LowHop", "BlocksMC", "WTF", "NCPLowHop", "Experimental"});
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
        mc.timer.timerSpeed = 1.0F;
        this.setDisplayName(mode.getValue());
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindJump.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindJump);
        mc.timer.timerSpeed = 1.0f;
        mc.thePlayer.speedInAir = 0.02f;
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mode.getValue().equalsIgnoreCase("WTF")) {
            if (!MovementUtils.shouldMove()) {
                return;
            }

            if (mc.thePlayer.onGround) {
                mc.timer.timerSpeed = 2f;
                mc.thePlayer.jump();
            } else {
                mc.timer.timerSpeed = 0.98f;
                mc.thePlayer.motionY = -0.427;
            }

        }
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
        } else if(mode.getValue().equalsIgnoreCase("BlocksMC")) {
            if(!MovementUtils.shouldMove()) {
                mc.timer.timerSpeed = 1.0f;
                return;
            }

            mc.gameSettings.keyBindJump.pressed = false;
            if(mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                    if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 0) {
                        MovementUtils.strafe(0.5893f);
                    } else if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 1) {
                        MovementUtils.strafe(0.6893f);
                    }
                } else {
                    MovementUtils.strafe(0.485f);
                }
            }
            MovementUtils.strafe();
        } else if (mode.getValue().equalsIgnoreCase("NCPLowHop")) {
            //Method by Exterminate (Lowest lowhop ever, trust)
            if (!MovementUtils.shouldMove()) return;

            //Anti Retard
            mc.gameSettings.keyBindJump.pressed = false;

            if (mc.thePlayer.onGround) {
                mc.timer.timerSpeed = 0.95f;
                mc.thePlayer.jump();
                if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                    if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 0) {
                        MovementUtils.strafe(0.58f);
                    } else if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 1) {
                        MovementUtils.strafe(0.67f);
                    }
                } else {
                    MovementUtils.strafe(0.485f);
                }
            } else if (mc.thePlayer.motionY < 0.16 && mc.thePlayer.motionY > 0.0) {
                mc.thePlayer.motionY = -0.1;
            } else if (mc.thePlayer.motionY < 0.0 && mc.thePlayer.motionY > -0.3) {
                mc.timer.timerSpeed = 1.2F;
            }
            MovementUtils.strafe();
        }
    }

    public void velocityBoost(S12PacketEntityVelocity packetEntityVelocity) {
        if (mode.getValue().equalsIgnoreCase("BlocksMC")) {
            if (packetEntityVelocity.getEntityID() == mc.thePlayer.getEntityId() && mc.thePlayer.hurtTime > 0 && mc.thePlayer.hurtTime < 5) {
                //MovementUtils.strafe(0.7f);
            }
        }
    }

    @EventTarget
    public void onMove(EventMove event) {
        if (mode.getValue().equalsIgnoreCase("LowHop")) {
            if (!MovementUtils.shouldMove()) return;
            mc.gameSettings.keyBindJump.pressed = false;
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                mc.thePlayer.motionY = 0.0;
                MovementUtils.strafe(0.61F);
                event.setY(0.41999998688698);
            }
            MovementUtils.strafe();
        } else if (mode.getValue().equalsIgnoreCase("Experimental")) {
            if (!MovementUtils.shouldMove()) return;
            mc.gameSettings.keyBindJump.pressed = false;
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                mc.thePlayer.motionY = 0.0;
                event.setY(0.1);
                MovementUtils.strafe(0.5f);
            }
            MovementUtils.strafe();
        }
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            flagCount++;

            if (flagCount >= 3 && autoDisable.getValue()) {
                ChatUtils.message("Toggled Speed due to flag.");
                this.toggle();
            }
        }
    }
}
