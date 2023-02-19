package dev.menace.module.modules.movement;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventRender3D;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventPreMotion;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.player.PlayerUtils;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.entity.EntityLivingBase;

import java.awt.*;

public class TargetStrafeModule extends BaseModule {

    private boolean direction = true;

    public TargetStrafeModule() {
        super("TargetStrafe", Category.MOVEMENT, 0);
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        if (mc.gameSettings.keyBindLeft.isKeyDown()) {
            direction = true;
        } else if (mc.gameSettings.keyBindRight.isKeyDown()) {
            direction = false;
        } else if (mc.thePlayer.isCollidedHorizontally) {
            direction = !direction;
        }
    }

    @EventTarget
    public void onMove(EventMove event) {

        EntityLivingBase target = Menace.instance.moduleManager.killAuraModule.trget;

        if (!Menace.instance.moduleManager.killAuraModule.isToggled() || target == null) {
            return;
        }

        MovementUtils.setSpeed(event, MovementUtils.getSpeed(), PlayerUtils.getRotations(target)[0], direction ? 1 : -1, mc.thePlayer.getDistanceToEntity(target) <= (Menace.instance.moduleManager.killAuraModule.reach.getValue() - 0.5) ?  0.0 : 1.0);

    }

    @EventTarget
    public void onRender3DEvent(EventRender3D event) {


        if (!Menace.instance.moduleManager.killAuraModule.isToggled() || Menace.instance.moduleManager.killAuraModule.trget == null) {
            return;
        }

        final Color theme = Color.RED;
        final Color color = new Color(theme.getRed(), theme.getGreen(), theme.getBlue(), 62);
        RenderUtils.circle(Menace.instance.moduleManager.killAuraModule.trget, Menace.instance.moduleManager.killAuraModule.reach.getValue() - 1, color);
    }

}
