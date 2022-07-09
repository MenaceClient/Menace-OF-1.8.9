/* package dev.menace.module.modules.player;

import dev.menace.event.EventTarget;
import dev.menace.module.BaseModule;
import dev.menace.module.modules.movement.SpeedModule;
import net.minecraft.block.BlockLiquid;
import dev.menace.module.Category;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

import static dev.menace.module.ModuleManager.mc;

public class JesusModule extends BaseModule {
    public JesusModule() {
        super("Jesus", Category.PLAYER, 0);
    }
}


    @EventTarget
    public void onPreUpdate(MotionUpdateEvent event) {
        if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
            if (!mc.thePlayer.movementInput().sneak() && mc.player.isInLiquid() && neededLevel(event.getX(), event.getY(), event.getZ())) {
                mc.thePlayer.motionY = 0.12;
            }
        }
    }

    private boolean waterUpper(MotionUpdateEvent event) {
        return mc.theWorld.getBlockState(new BlockPos(event.getX(),
                mc.thePlayer.getEntityBoundingBox().maxY - 1,
                event.getZ())).getBlock() instanceof BlockLiquid;
    }

    private boolean neededLevel(double x, double y, double z) {
        return (int) mc.theWorld.getBlockState(new BlockPos(x, y, z)).getProperties().get(BlockLiquid.LEVEL) < (isToggled(SpeedModule.class); ? 2 : 4);
    }
}

 */

