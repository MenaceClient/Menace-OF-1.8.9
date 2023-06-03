package dev.menace.module.modules.player;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.MathUtils;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.player.PlayerUtils;
import dev.menace.utils.world.BlockUtils;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class AntiVoidModule extends BaseModule {

    private double lastOnGroundX;
    private double lastOnGroundY;
    private double lastOnGroundZ;

    //Blink
    private C03PacketPlayer lastPacketSent = new C03PacketPlayer();
    private final Queue<C03PacketPlayer> packetQueue = new ConcurrentLinkedDeque<>();
    DecimalFormat df = new DecimalFormat("#.##");
    private boolean blink = false;

    //Smart Disable
    private boolean scaffoldToggled = false;

    ListSetting mode;
    SliderSetting fallDist;
    ToggleSetting smartDisable;

    public AntiVoidModule() {
        super("AntiVoid", Category.PLAYER, 0);
    }

    @Override
    public void setup() {
        mode = new ListSetting("Mode", true, "Teleport", new String[] {"Teleport", "Stop", "Flag", "Blink", "Scaffold"});
        fallDist = new SliderSetting("Fall Distance", true, 3, 0, 10, false);
        smartDisable = new ToggleSetting("Smart Disable", true, true) {
            @Override
            public void constantCheck() {
                this.setVisible(mode.getValue().equalsIgnoreCase("Blink") || mode.getValue().equalsIgnoreCase("Scaffold"));
                super.constantCheck();
            }
        };
        this.rSetting(mode);
        this.rSetting(fallDist);
        this.rSetting(smartDisable);
        super.setup();
    }

    @Override
    public void onEnable() {
        packetQueue.clear();
        blink = false;
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {

        this.setDisplayName(mode.getValue());

        if (mc.thePlayer.onGround && PlayerUtils.isBlockUnder()) {
            this.lastOnGroundX = mc.thePlayer.posX;
            this.lastOnGroundY = mc.thePlayer.posY;
            this.lastOnGroundZ = mc.thePlayer.posZ;
            if (blink) {
                blink();
            }
        }

        if (scaffoldToggled && Menace.instance.moduleManager.scaffoldModule.isToggled() && smartDisable.getValue() && mc.thePlayer.onGround
        && PlayerUtils.isBlockUnder() && mc.thePlayer.fallDistance == 0) {
            Menace.instance.moduleManager.scaffoldModule.toggle();
            scaffoldToggled = false;
        }

        //TODO: Make settings for this
        if (mode.getValue().equalsIgnoreCase("Blink") && mc.thePlayer.fallDistance > 0 &&
                !PlayerUtils.isBlockUnder() && !mc.thePlayer.onGround) {
            if (!blink) {
                blink = true;
            } else if (mc.thePlayer.fallDistance > 5) {
                MovementUtils.stopHoriz();
                mc.thePlayer.setPositionAndUpdate(lastPacketSent.getPositionX(), lastPacketSent.getPositionY(), lastPacketSent.getPositionZ());
                //mc.thePlayer.setPositionAndUpdate(lastOnGroundX, lastOnGroundY, lastOnGroundZ);
                packetQueue.clear();
                if (!Menace.instance.moduleManager.scaffoldModule.isToggled()) {
                    Menace.instance.moduleManager.scaffoldModule.toggle();
                    scaffoldToggled = true;
                }
                blink = false;
            }
            return;
        }

        if (mc.thePlayer.fallDistance > fallDist.getValue() && !PlayerUtils.isBlockUnder()) {
            switch (mode.getValue()) {
                case "Teleport":
                    if (mc.thePlayer.getDistance(lastOnGroundX, mc.thePlayer.posY, lastOnGroundZ) > 5) {
                        return;
                    }
                    mc.thePlayer.setPositionAndUpdate(lastOnGroundX, lastOnGroundY, lastOnGroundZ);
                    MovementUtils.stop();
                    break;
                case "Flag" :
                    MovementUtils.stop();
                    PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 100, mc.thePlayer.posZ, true));
                    break;
                case "Stop" :
                    MovementUtils.stop();
                    break;
                case "Scaffold" :
                    if (!Menace.instance.moduleManager.scaffoldModule.isToggled()) {
                        MovementUtils.stopHoriz();
                        Menace.instance.moduleManager.scaffoldModule.toggle();
                        scaffoldToggled = true;
                    }
                    break;
            }
        }

    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof C03PacketPlayer) {
            if (blink) {
                event.setCancelled(true);
                packetQueue.add((C03PacketPlayer) event.getPacket());
            } else {
                lastPacketSent = (C03PacketPlayer) event.getPacket();
            }
        }
    }

    private void blink() {
        blink = false;
        while (!packetQueue.isEmpty()) {
            PacketUtils.sendPacketNoEvent(packetQueue.poll());
        }
    }

}
