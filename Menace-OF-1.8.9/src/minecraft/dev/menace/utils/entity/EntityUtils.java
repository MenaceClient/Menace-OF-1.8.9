package dev.menace.utils.entity;

import dev.menace.Menace;
import dev.menace.utils.entity.self.PlayerUtils;
import dev.menace.utils.entity.self.Rotations;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;

public class EntityUtils {
	
	static Minecraft MC = Minecraft.getMinecraft();
	
    public static EntityLivingBase getClosest(double range) {
        double dist = range;
        EntityLivingBase target = null;
        for (Object object : MC.theWorld.loadedEntityList) {
            Entity entity = (Entity) object;
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) entity;
                if (PlayerUtils.canAttack(player)) {
                    double currentDist = MC.thePlayer.getDistanceToEntity(player);
                    if (currentDist <= dist) {
                        dist = currentDist;
                        target = player;
                    }
                }
            }
        }
        return target;
    }
    
    public static EntityLivingBase getTarget() {
    	return getClosest(MC.playerController.getBlockReachDistance());
    }
    
    public static float getHealth(EntityLivingBase entity){
        return (entity == null || entity.isDead)? 0f : entity.getHealth();
    }

}
