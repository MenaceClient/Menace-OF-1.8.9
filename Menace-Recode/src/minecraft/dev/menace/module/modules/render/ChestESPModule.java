package dev.menace.module.modules.render;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventRender3D;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.world.BlockUtils;
import net.minecraft.init.Blocks;

import java.awt.*;

public class ChestESPModule extends BaseModule {

    public ChestESPModule() {
        super("ChestESP", Category.RENDER, 0);
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {

        BlockUtils.searchForBlocksInRadius(Blocks.chest, 30).forEach(blockPos -> {
            RenderUtils.drawBlock(blockPos, new Color(255, 0, 0, 255), 1);
        });

    }

}
