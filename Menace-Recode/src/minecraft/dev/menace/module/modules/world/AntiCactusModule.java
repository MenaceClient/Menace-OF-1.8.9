package dev.menace.module.modules.world;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventCollide;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

public class AntiCactusModule extends BaseModule {
    public AntiCactusModule() {
        super("AntiCactus", Category.PLAYER, 0);
    }

    @EventTarget
    public void onCollide(@NotNull EventCollide event) {
        if (event.getBlock() == Blocks.cactus) {
            event.setBoundingBox(Blocks.bedrock.getCollisionBoundingBox(mc.theWorld, new BlockPos(event.getPosX(), event.getPosY(), event.getPosZ()), Blocks.bedrock.getDefaultState()));
        }
    }

}
