package dev.menace.module.modules.misc;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventMouse;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.RayCastUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;

public class MCFModule extends BaseModule {
    public MCFModule() {
        super("MCF", "Middle Click someone to add them as a friend", Category.MISC, 0);
    }

    @EventTarget
    public void onMouseClick(EventMouse event) {
        MovingObjectPosition mouseOver = RayCastUtils.getMouseOver(5);
        if (event.getButton() == 2 && mouseOver != null && mouseOver.entityHit instanceof EntityLivingBase) {
            if (!Menace.instance.friendManager.isFriend(mouseOver.entityHit.getName())) {
                Menace.instance.friendManager.addFriend(mouseOver.entityHit.getName());
                ChatUtils.message("Added " + mouseOver.entityHit.getName() + " to friends list.");
            } else {
                Menace.instance.friendManager.removeFriend(mouseOver.entityHit.getName());
                ChatUtils.message("Removed " + mouseOver.entityHit.getName() + " from friends list.");
            }
        }
    }

}
