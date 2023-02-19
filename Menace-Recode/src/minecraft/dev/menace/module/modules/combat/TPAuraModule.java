package dev.menace.module.modules.combat;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventRender3D;
import dev.menace.event.events.EventPreMotion;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventWorldChange;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.SliderSetting;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.player.PathFindingUtils;
import dev.menace.utils.player.PlayerUtils;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TPAuraModule extends BaseModule {

    SliderSetting cps;
    SliderSetting targetAmt;
    SliderSetting range;
    private ArrayList<Vec3>[] paths = new ArrayList[5];
    public List<EntityLivingBase> targets = new ArrayList<>();
    private final MSTimer cpstimer = new MSTimer();
    public final MSTimer timer = new MSTimer();

    public TPAuraModule() {
        super("TPAura", Category.COMBAT, 0);
    }

    @Override
    public void setup() {
        cps = new SliderSetting("CPS", true, 5, 1, 20, true);
        targetAmt = new SliderSetting("Targets", true, 1, 1, 5, true);
        range = new SliderSetting("Range", true, 300, 50, 500, 10, true);
        this.rSetting(cps);
        this.rSetting(targetAmt);
        this.rSetting(range);
        super.setup();
    }

    @Override
    public void onDisable() {
        paths = new ArrayList[5];
        targets = new ArrayList<>();
        super.onDisable();
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        this.targets = this.getTargets();
        if (this.cpstimer.hasTimePassed((long)(1000.0 / this.cps.getValue())) && this.targets.size() > 0) {
            for (int i = 0; i < ((this.targets.size() > 1) ? this.targetAmt.getValue() : this.targets.size()); ++i) {
                ArrayList<Vec3> path;
                final EntityLivingBase target = this.targets.get(i);
                if (mc.thePlayer.getDistanceToEntity(target) > this.range.getValue()) {
                    return;
                }
                final Vec3 topFrom = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                final Vec3 to = new Vec3(target.posX, target.posY, target.posZ);
                path = PathFindingUtils.computePath(topFrom, to);
                this.paths[i] = path;
                for (final Vec3 pathElm : path) {
                    PacketUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));
                }
                mc.thePlayer.swingItem();
                mc.playerController.attackEntity(mc.thePlayer, target);
                //mc.thePlayer.playSound("player.hurt", 1.0f, 1.0f);
                Collections.reverse(path);
                for (final Vec3 pathElm : path) {
                    PacketUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));
                }
                final float[] rots = PlayerUtils.getRotations(target);
                event.setYaw(rots[0]);
                event.setPitch(rots[1]);
            }
            this.cpstimer.reset();
        } else if (this.targets.size() == 0) {
            paths = new ArrayList[5];
        }
    }

    @EventTarget
    public void on3D(EventRender3D event) {
        if (!targets.isEmpty()) {
            for (ArrayList<Vec3> path : paths) {
                if (path != null && !path.isEmpty()) {
                    renderPath(path);
                }
            }
        }
    }

    @EventTarget
    public void onRecievePacket(@NotNull EventReceivePacket event) {
        /*if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            event.cancel();
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
            final Vec3 topFrom = new Vec3(packet.getX(), packet.getY(), packet.getZ());
            final Vec3 to = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
            ArrayList<Vec3> path = PathFindingUtils.computePath(topFrom, to);
            for (final Vec3 pathElm : path) {
                PacketUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));
            }
        }*/
    }
    @EventTarget
    public void onWorldChange(EventWorldChange event) {
        ChatUtils.message("Toggled TPAura due to world change");
        this.toggle();
    }

    private void renderPath(ArrayList<Vec3> path) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND);
        RenderUtils.lineWidth(2);
        RenderUtils.color4f(255, 255, 255, 255);
        RenderUtils.glBegin(3);
        int i = 0;
        for (Vec3 vec : path) {
            RenderUtils.putVertex3d(RenderUtils.getRenderPos(vec.xCoord, vec.yCoord, vec.zCoord));
            i++;
        }
        RenderUtils.glEnd();
        RenderUtils.color4f(255, 255, 255, 255);
        RenderUtils.glBegin(3);
        i = 0;
        for (Vec3 vec : path) {
            RenderUtils.putVertex3d(RenderUtils.getRenderPos(vec.xCoord, vec.yCoord, vec.zCoord));
            i++;
        }
        RenderUtils.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
    }

    public boolean canAttack(final EntityLivingBase player) {
        return player != mc.thePlayer && player instanceof EntityPlayer;
    }

    private List<EntityLivingBase> getTargets() {
        final List<EntityLivingBase> targets = new ArrayList<>();
        for (final Object o3 : mc.theWorld.getLoadedEntityList()) {
            if (o3 instanceof EntityLivingBase) {
                final EntityLivingBase entity = (EntityLivingBase)o3;
                if (!this.canAttack(entity)) {
                    continue;
                }
                targets.add(entity);
            }
        }
        targets.sort((o1, o2) -> (int)(o1.getDistanceToEntity(mc.thePlayer) * 1000.0f - o2.getDistanceToEntity(mc.thePlayer) * 1000.0f));
        return targets;
    }

}
