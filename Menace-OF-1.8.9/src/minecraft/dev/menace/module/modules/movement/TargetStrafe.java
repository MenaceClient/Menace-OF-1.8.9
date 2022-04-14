package dev.menace.module.modules.movement;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.viaversion.viaversion.util.MathUtil;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.Event3D;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.BoolSetting;
import dev.menace.utils.entity.EntityUtils;
import dev.menace.utils.entity.self.PlayerUtils;
import dev.menace.utils.entity.self.Rotations;
import dev.menace.utils.misc.MathUtils;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.block.BlockAir;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

public class TargetStrafe extends Module {
	private boolean direction = true;
	private float yaw = 0f;
	
	//Settings
	public BoolSetting rotate;

	public TargetStrafe() {
		super("TargetStrafe", 0, Category.MOVEMENT);
	}

	@Override
	public void setup() {
		rotate = new BoolSetting("Rotate", this, true);
		this.rSetting(rotate);
	}
	
	
	@EventTarget
	public void onPreMotion(EventPreMotionUpdate event) {
		if (MC.gameSettings.keyBindLeft.isKeyDown()) {
            direction = true;
        } else if (MC.gameSettings.keyBindRight.isKeyDown()) {
            direction = false;
        } else if (MC.thePlayer.isCollidedHorizontally) {
            direction = !direction;
        }
	}
	
	@EventTarget
	public void strafe(EventMove event) {
		Entity target = null;
		if (Menace.instance.moduleManager.killAuraModule.isToggled()) {
			target = Menace.instance.moduleManager.killAuraModule.attacked;
		} else if (Menace.instance.moduleManager.testAuraModule.isToggled()) {
			target = Menace.instance.moduleManager.testAuraModule.target;
		}
		
        if(target == null)
            return;
		
		if (canStrafe(target)) {
			yaw = Rotations.getRotations(target)[0];
            PlayerUtils.setSpeed(event, (double) PlayerUtils.getSpeed(), yaw, direction ? 1.0 : -1.0, (MC.thePlayer.getDistanceToEntity(target) <= 3) ?  0.0 : 1.0);
        }
	}
	
	private boolean canStrafe(Entity target) {
        return target != null && (Menace.instance.moduleManager.killAuraModule.isToggled() || Menace.instance.moduleManager.testAuraModule.isToggled()) && MC.theWorld != null;
    }
    
    @EventTarget
    public void onRender3DEvent(Event3D event) {
		Entity target = null;
		if (Menace.instance.moduleManager.killAuraModule.isToggled()) {
			target = Menace.instance.moduleManager.killAuraModule.attacked;
		} else if (Menace.instance.moduleManager.testAuraModule.isToggled()) {
			target = Menace.instance.moduleManager.testAuraModule.target;
		}
		
		if (target == null) {
			return;
		}

        final Color theme = Color.RED;
        final Color color = new Color(theme.getRed(), theme.getGreen(), theme.getBlue(), 62);
        circle(target, 3 - 0.5, color);
    }
    
    private void circle(final Entity entity, final double rad, Color color) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glHint(3153, 4354);
        GL11.glDepthMask(false);
        GlStateManager.disableCull();
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * MC.timer.renderPartialTicks - MC.getRenderManager().viewerPosX;
        final double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * MC.timer.renderPartialTicks - MC.getRenderManager().viewerPosY) + 0.01;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * MC.timer.renderPartialTicks - MC.getRenderManager().viewerPosZ;

        for (int i = 0; i <= 90; ++i) {
            RenderUtils.color(color);

            GL11.glVertex3d(x + rad * Math.cos(i * 6.283185307179586 / 45.0), y, z + rad * Math.sin(i * 6.283185307179586 / 45.0));
        }

        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GlStateManager.enableCull();
        GL11.glDisable(2848);
        GL11.glDisable(2848);
        GL11.glEnable(2832);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
        GL11.glColor3f(255, 255, 255);
    }
}

