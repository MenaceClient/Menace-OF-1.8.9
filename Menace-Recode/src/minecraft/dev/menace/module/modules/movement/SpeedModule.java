package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventPreMotion;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.DontSaveState;
import dev.menace.module.modules.movement.speed.SpeedBase;
import dev.menace.module.modules.movement.speed.SpeedMode;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.world.TimerHandler;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.util.ArrayList;

@DontSaveState
public class SpeedModule extends BaseModule {

    private int flagCount;

    ListSetting mode;
    ListSetting vanillaMode;
    ListSetting verusMode;
    ListSetting ncpMode;
    ListSetting otherMode;
    public SliderSetting speed;
    ToggleSetting autoDisable;
    public ListSetting timerMode;

    private SpeedBase speedBase;

    public SpeedModule() {
        super("Speed", "Zoom", Category.MOVEMENT, 0);
    }

    @Override
    public void setup() {
        ArrayList<String> values = new ArrayList<>();
        for (SpeedMode.SpeedType fm : SpeedMode.SpeedType.values()) {
            values.add(fm.getName());
        }
        mode = new ListSetting("Mode", true, "BHop", values.toArray(new String[] {}));

        ArrayList<String> vanillaValues = new ArrayList<>();
        ArrayList<String> verusValues = new ArrayList<>();
        ArrayList<String> ncpValues = new ArrayList<>();
        ArrayList<String> otherValues = new ArrayList<>();
        for (SpeedMode fm : SpeedMode.values()) {
            switch (fm.getType())
            {
                case VANILLA:
                    vanillaValues.add(fm.getName());
                    break;
                case VERUS:
                    verusValues.add(fm.getName());
                    break;
                case NCP:
                    ncpValues.add(fm.getName());
                    break;
                case OTHER:
                    otherValues.add(fm.getName());
                    break;
            }
        }

        vanillaMode = new ListSetting("Vanilla Mode", true, "BHop", vanillaValues.toArray(new String[] {})) {
            @Override
            public void constantCheck() {
                this.setVisible(mode.getValue().equals("Vanilla"));
                super.constantCheck();
            }
        };
        verusMode = new ListSetting("Verus Mode", true, "LowHop", verusValues.toArray(new String[] {})) {
            @Override
            public void constantCheck() {
                this.setVisible(mode.getValue().equals("Verus"));
                super.constantCheck();
            }
        };
        ncpMode = new ListSetting("NCP Mode", true, "LowHop", ncpValues.toArray(new String[] {})) {
            @Override
            public void constantCheck() {
                this.setVisible(mode.getValue().equals("NCP"));
                super.constantCheck();
            }
        };
        otherMode = new ListSetting("Other Mode", true, "BlocksMC", otherValues.toArray(new String[] {})) {
            @Override
            public void constantCheck() {
                this.setVisible(mode.getValue().equals("Other"));
                super.constantCheck();
            }
        };

        speed = new SliderSetting("Speed", true, 1, 1, 10, true);
        autoDisable = new ToggleSetting("AutoDisable", true, true);
        timerMode = new ListSetting("Timer Mode", true, "Standard", new String[] {"Standard", "PacketBalance", "None"}) {
            @Override
            public void constantCheck() {
                this.setVisible(mode.getValue().equalsIgnoreCase("NCP") && ncpMode.getValue().equalsIgnoreCase("LowHop"));
                super.constantCheck();
            }
        };
        this.rSetting(mode);
        this.rSetting(vanillaMode);
        this.rSetting(verusMode);
        this.rSetting(ncpMode);
        this.rSetting(otherMode);
        this.rSetting(speed);
        this.rSetting(autoDisable);
        this.rSetting(timerMode);
        super.setup();
    }

    @Override
    public void onEnable() {
        flagCount = 0;
        mc.timer.timerSpeed = 1.0F;

        for (SpeedMode sm : SpeedMode.values()) {
            if (mode.getValue().equalsIgnoreCase("Vanilla") && sm.getName().equalsIgnoreCase(vanillaMode.getValue()) && sm.getType() == SpeedMode.SpeedType.VANILLA) {
                this.setDisplayName(sm.getName());
                speedBase = sm.getSpeed();
                break;
            } else if (mode.getValue().equalsIgnoreCase("Verus") && sm.getName().equalsIgnoreCase(verusMode.getValue()) && sm.getType() == SpeedMode.SpeedType.VERUS) {
                this.setDisplayName(sm.getName());
                speedBase = sm.getSpeed();
                break;
            } else if (mode.getValue().equalsIgnoreCase("NCP") && sm.getName().equalsIgnoreCase(ncpMode.getValue()) && sm.getType() == SpeedMode.SpeedType.NCP) {
                this.setDisplayName(sm.getName());
                speedBase = sm.getSpeed();
                break;
            } else if (mode.getValue().equalsIgnoreCase("Other") && sm.getName().equalsIgnoreCase(otherMode.getValue()) && sm.getType() == SpeedMode.SpeedType.OTHER) {
                this.setDisplayName(sm.getName());
                speedBase = sm.getSpeed();
                break;
            }
        }

        super.onEnable();
        speedBase.onEnable();
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindJump.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindJump);
        TimerHandler.resetTimer();
        mc.thePlayer.speedInAir = 0.02f;
        super.onDisable();
        speedBase.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        speedBase.onUpdate();
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        speedBase.onPreMotion(event);
    }

    @EventTarget
    public void onMove(EventMove event) {
        speedBase.onMove(event);
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
