package dev.menace.module.modules.player;

import dev.menace.Menace;
import dev.menace.event.Event;
import dev.menace.event.EventTarget;
import dev.menace.event.events.*;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.player.PacketUtils;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

public class NoSlowModule extends BaseModule {

    private static final BlockPos field_179726_a = new BlockPos(-1, -1, -1);

    ListSetting mode;

    public NoSlowModule() {
        super("NoSlow", Category.PLAYER, 0);
    }

    @Override
    public void setup() {
        mode = new ListSetting("Mode", true, "Vanilla", new String[] {"Vanilla", "OldNCP", "Hypixel", "NCP"});
        this.rSetting(mode);
        super.setup();
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        this.setDisplayName(mode.getValue());

        if (mc.thePlayer.isBlocking() && MovementUtils.isMoving() && mode.getValue().equalsIgnoreCase("OldNCP")) {
            PacketUtils.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        //kys
        if(mode.getValue().equalsIgnoreCase("Hypixel") && mc.thePlayer.isBlocking() && MovementUtils.isMoving()) {
            if (event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
                C08PacketPlayerBlockPlacement packet = (C08PacketPlayerBlockPlacement) event.getPacket();
                if (packet.getPosition() == field_179726_a) {
                    packet.setPosition(new BlockPos(-0. + Math.random(), -0. + Math.random(), -0. + Math.random()));
                }
            }
        }
    }

    @EventTarget
    public void onRecievePacket(EventReceivePacket event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if((mode.getValue().equalsIgnoreCase("Hypixel") || mode.getValue().equalsIgnoreCase("NCP")) && (mc.thePlayer.isBlocking() || mc.thePlayer.isEating()) && MovementUtils.isMoving() && event.getPacket() instanceof S30PacketWindowItems) {
            PacketUtils.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onPost(EventPostMotion event) {
        if (mc.thePlayer.isBlocking()  && MovementUtils.isMoving() && mode.getValue().equalsIgnoreCase("OldNCP")) {
            PacketUtils.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
        }
    }

    @EventTarget
    public void onSlowdown(EventSlowdown event) {
        event.cancel();
    }
}
