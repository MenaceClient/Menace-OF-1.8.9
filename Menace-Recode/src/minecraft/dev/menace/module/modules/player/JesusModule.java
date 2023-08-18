package dev.menace.module.modules.player;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventPreMotion;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class JesusModule extends BaseModule {

    ListSetting mode;

    public JesusModule() {
        super("Jesus", "Allows you to walk on water (The same hack Jesus used ~2000 years ago)", Category.PLAYER, 0);
    }

    @Override
    public void setup() {
        mode = new ListSetting("Mode", true, "Collide", new String[]{"Collide", "Motion"});
        this.rSetting(mode);
        super.setup();
    }

    @EventTarget
    public void onCollide(EventCollide event) {
        if (event.getBlock() instanceof BlockLiquid && mode.getValue().equalsIgnoreCase("Collide")) {
            event.setBoundingBox(Blocks.bedrock.getCollisionBoundingBox(mc.theWorld, new BlockPos(event.getPosX(), event.getPosY(), event.getPosZ()), Blocks.bedrock.getDefaultState()));
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (mode.getValue().equalsIgnoreCase("Motion")) {
            if (mc.thePlayer.isInWater() || mc.thePlayer.isInLava()) {
                mc.thePlayer.motionY = 0.1;
            }
        }
    }

}
