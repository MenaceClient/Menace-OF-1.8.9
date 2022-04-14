package dev.menace.utils.entity.self;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import dev.menace.Menace;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class CombatUtils
{
	static Minecraft MC = Minecraft.getMinecraft();
	
    
    
    public static float getPitchChange(final Entity local_01) {
        final double local_2 = local_01.posX - MC.thePlayer.posX;
        final double local_3 = local_01.posZ - MC.thePlayer.posZ;
        final double local_4 = local_01.posY - 2.2 + local_01.getEyeHeight() - MC.thePlayer.posY;
        final double local_5 = MathHelper.sqrt_double(local_2 * local_2 + local_3 * local_3);
        final double local_6 = -Math.toDegrees(Math.atan(local_4 / local_5));
        return -MathHelper.wrapAngleTo180_float(MC.thePlayer.rotationPitch - (float)local_6) - 2.5f;
    }
    
    public static float getYawChange(final Entity local_01) {
        final double local_2 = local_01.posX - MC.thePlayer.posX;
        final double local_3 = local_01.posZ - MC.thePlayer.posZ;
        double local_4 = 0.0;
        if (local_3 < 0.0 && local_2 < 0.0) {
            local_4 = 90.0 + Math.toDegrees(Math.atan(local_3 / local_2));
        }
        else if (local_3 < 0.0 && local_2 > 0.0) {
            local_4 = -90.0 + Math.toDegrees(Math.atan(local_3 / local_2));
        }
        else {
            local_4 = Math.toDegrees(-Math.atan(local_2 / local_3));
        }
        return MathHelper.wrapAngleTo180_float(-(MC.thePlayer.rotationYaw - (float)local_4));
    }
    
    public static boolean isEntityValid(final Entity local_01) {
        if (local_01 instanceof EntityLivingBase) {
            if (!MC.thePlayer.isEntityAlive() || !((EntityLivingBase)local_01).isEntityAlive() || local_01.getDistanceToEntity(MC.thePlayer) > (MC.thePlayer.canEntityBeSeen(local_01) ? Menace.instance.moduleManager.aimAssistModule.range.getValue() : 3.0)) {
                return false;
            }
            final double local_2 = local_01.posX - MC.thePlayer.posX;
            final double local_3 = local_01.posZ - MC.thePlayer.posZ;
            final double local_4 = MC.thePlayer.posY + MC.thePlayer.getEyeHeight() - (local_01.posY + local_01.getEyeHeight());
            final double local_5 = Math.sqrt(local_2 * local_2 + local_3 * local_3);
            final float local_6 = (float)(Math.atan2(local_3, local_2) * 180.0 / 3.141592653589793) - 90.0f;
            final float local_7 = (float)(Math.atan2(local_4, local_5) * 180.0 / 3.141592653589793);
            final double local_8 = Rotations.getDistanceBetweenAngles(local_6, MC.thePlayer.rotationYaw % 360.0f);
            final double local_9 = Rotations.getDistanceBetweenAngles(local_7, MC.thePlayer.rotationPitch % 360.0f);
            final double local_10 = Math.sqrt(local_8 * local_8 + local_9 * local_9);
            if (local_10 > Menace.instance.moduleManager.aimAssistModule.degrees.getValue()) {
                return false;
            }
        }
        return false;
    }
}