package dev.menace.module.modules.misc;

import dev.menace.event.EventTarget;
import dev.menace.event.events.*;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.modules.misc.disabler.DisablerBase;
import dev.menace.module.modules.misc.disabler.DisablerMode;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;

import java.util.ArrayList;

public class DisablerModule extends BaseModule {

    DisablerBase disabler;


    public ListSetting mode;
    public ToggleSetting silent;
    public SliderSetting pingSpoofDelay;
    public SliderSetting pingSpoofMaxPackets;
    public ToggleSetting pingSpoofTransaction;

    public DisablerModule() {
        super("Disabler", Category.MISC, 0);
    }

    @Override
    public void setup() {
        ArrayList<String> values = new ArrayList<>();
        for (DisablerMode dm : DisablerMode.values()) {
            values.add(dm.getName());
        }
        mode = new ListSetting("Mode", true, "PingSpoof", values.toArray(new String[] {}));
        pingSpoofDelay = new SliderSetting("Delay", true, 300, 100, 1000, true) {
            @Override
            public void constantCheck() {
                this.setVisible(mode.getValue().equalsIgnoreCase("PingSpoof"));
                super.constantCheck();
            }
        };
        pingSpoofMaxPackets = new SliderSetting("MaxPackets", true, 300, 100, 1000, true) {
            @Override
            public void constantCheck() {
                this.setVisible(mode.getValue().equalsIgnoreCase("PingSpoof"));
                super.constantCheck();
            }
        };
        pingSpoofTransaction = new ToggleSetting("Transactions", true, true) {
            @Override
            public void constantCheck() {
                this.setVisible(mode.getValue().equalsIgnoreCase("PingSpoof"));
                super.constantCheck();
            }
        };
        silent = new ToggleSetting("Silent", true, true) {
            @Override
            public void constantCheck() {
                this.setVisible(mode.getValue().equalsIgnoreCase("VerusTransaction"));
                super.constantCheck();
            }
        };
        this.rSetting(mode);
        this.rSetting(pingSpoofDelay);
        this.rSetting(pingSpoofMaxPackets);
        this.rSetting(pingSpoofTransaction);
        this.rSetting(silent);
        super.setup();
    }

    @Override
    public void onEnable() {
        for (DisablerMode dm : DisablerMode.values()) {
            if (dm.getName().equalsIgnoreCase(mode.getValue())) {
                this.setDisplayName(dm.getGoofyName());
                disabler = dm.getDisabler();
            }
        }
        disabler.onEnable();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        disabler.onDisable();
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        disabler.onUpdate();
    }

    @EventTarget
    public void onPacketSend(EventSendPacket event) {
        disabler.onSendPacket(event);
    }

    @EventTarget
    public void onPacketReceive(EventReceivePacket event) {
        disabler.onReceivePacket(event);
    }

    @EventTarget
    public void onWorldChange(EventWorldChange event) {
        disabler.onWorldChange(event);
    }

}