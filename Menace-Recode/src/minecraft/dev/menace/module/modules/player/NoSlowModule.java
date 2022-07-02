package dev.menace.module.modules.player;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventPostMotion;
import dev.menace.event.events.EventPreMotion;
import dev.menace.event.events.EventSlowdown;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.player.PacketUtils;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlowModule extends BaseModule {

    ListSetting mode;

    public NoSlowModule() {
        super("NoSlow", Category.PLAYER, 0);
    }

    @Override
    public void setup() {
        mode = new ListSetting("Mode", true, "Vanilla", new String[] {"Vanilla", "NCP"});
        this.rSetting(mode);
        super.setup();
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        this.setDisplayName(mode.getValue());
        if (MC.thePlayer.isBlocking() && MovementUtils.isMoving() && mode.getValue().equalsIgnoreCase("NCP")) {
            PacketUtils.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }

    @EventTarget
    public void onPost(EventPostMotion event) {
        if (MC.thePlayer.isBlocking()  && MovementUtils.isMoving() && mode.getValue().equalsIgnoreCase("NCP")) {
            PacketUtils.sendPacket(new C08PacketPlayerBlockPlacement(MC.thePlayer.inventory.getCurrentItem()));
        }
    }

    @EventTarget
    public void onSlowdown(EventSlowdown event) {
        event.cancel();
    }
}
