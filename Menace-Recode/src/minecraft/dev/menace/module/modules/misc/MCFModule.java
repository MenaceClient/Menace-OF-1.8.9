package dev.menace.module.modules.misc;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventMouse;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.player.RayCastUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;

public class MCFModule extends BaseModule {
    public MCFModule() {
        super("MCF", Category.MISC, 0);
    }

    @EventTarget
    public void onMouseClick(EventMouse event) {
        MovingObjectPosition mouseOver = RayCastUtils.getMouseOver(5);
        if (mouseOver != null && mouseOver.entityHit instanceof EntityLivingBase) {
            Menace.instance.friendManager.addFriend(mouseOver.entityHit.getName());
        }
    }

}
