package dev.menace.utils.player;

import net.minecraft.util.*;

import java.util.List;

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;

public class RayCastUtils
{
    private static Minecraft mc;
    
    static {
        RayCastUtils.mc = Minecraft.getMinecraft();
    }

    public static MovingObjectPosition getMouseOver(final float range) {
        final Entity entity = RayCastUtils.mc.getRenderViewEntity();
        if (entity != null && RayCastUtils.mc.theWorld != null) {
            double reach = RayCastUtils.mc.playerController.getBlockReachDistance();
            MovingObjectPosition objectMouseOver = entity.rayTrace(reach, 1.0f);
            double var4;
            final Vec3 var5 = entity.getPositionEyes(1.0f);
            reach = range;
            var4 = range;
            if (objectMouseOver != null) {
                var4 = objectMouseOver.hitVec.distanceTo(var5);
            }
            final Vec3 var6 = entity.getLook(1.0f);
            final Vec3 var7 = var5.addVector(var6.xCoord * reach, var6.yCoord * reach, var6.zCoord * reach);
            Entity pointedEntity = null;
            Vec3 var8 = null;
            final List<Entity> var10 = RayCastUtils.mc.theWorld.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().addCoord(var6.xCoord * reach, var6.yCoord * reach, var6.zCoord * reach).expand(1.0, 1.0, 1.0));
            double var11 = var4;
            for (final Entity var13 : var10) {
                if (var13.canBeCollidedWith()) {
                    final float var14 = var13.getCollisionBorderSize();
                    final AxisAlignedBB var15 = var13.getEntityBoundingBox().expand(var14, var14, var14);
                    final MovingObjectPosition var16 = var15.calculateIntercept(var5, var7);
                    if (var15.isVecInside(var5)) {
                        if (0.0 < var11 || var11 == 0.0) {
                            pointedEntity = var13;
                            var8 = ((var16 == null) ? var5 : var16.hitVec);
                            var11 = 0.0;
                        }
                    } else if (var16 != null) {
                        final double var17 = var5.distanceTo(var16.hitVec);
                        if (var17 < var11 || var11 == 0.0) {
                            final boolean canRiderInteract = false;
                            if (var13 == entity.ridingEntity && !canRiderInteract) {
                                if (var11 == 0.0) {
                                    pointedEntity = var13;
                                    var8 = var16.hitVec;
                                }
                            } else {
                                pointedEntity = var13;
                                var8 = var16.hitVec;
                                var11 = var17;
                            }
                        }
                    }
                }
            }
            if (pointedEntity != null && (var11 < var4 || RayCastUtils.mc.objectMouseOver == null)) {
                objectMouseOver = new MovingObjectPosition(pointedEntity, var8);
                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    return objectMouseOver;
                }
            }
            return objectMouseOver;
        }
        return null;
    }

}