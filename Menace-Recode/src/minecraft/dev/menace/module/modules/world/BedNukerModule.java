package dev.menace.module.modules.world;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.world.BlockUtils;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.EnumFacing;

public class BedNukerModule extends BaseModule {

    SliderSetting radius;
    ToggleSetting swing;

    public BedNukerModule() {
        super("BedNuker", "Automatically breaks beds through walls", Category.WORLD, 0);
    }

    @Override
    public void setup() {
        radius = new SliderSetting("Radius", true, 3, 1, 5, true);
        swing = new ToggleSetting("Swing", true, true);
        this.rSetting(radius);
        this.rSetting(swing);
        super.setup();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {

        BlockUtils.searchForBlocksInRadius(Blocks.bed, radius.getValue()).forEach(blockPos -> {
            if (swing.getValue()) {
                mc.thePlayer.swingItem();
            } else {
                PacketUtils.sendPacket(new C0APacketAnimation());
            }
            PacketUtils.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
            PacketUtils.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
        });

    }

}
