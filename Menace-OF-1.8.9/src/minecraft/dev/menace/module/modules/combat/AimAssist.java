package dev.menace.module.modules.combat;

import org.lwjgl.input.Keyboard;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventLeftClick;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.*;
import dev.menace.utils.entity.EntityUtils;
import dev.menace.utils.entity.self.CombatUtils;
import dev.menace.utils.entity.self.Rotations;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class AimAssist extends Module {
	
    private EntityLivingBase target;
    
    //Settings
    public DoubleSetting smooth;
    public DoubleSetting degrees;
    public DoubleSetting range;
    

	public AimAssist() {
		super("AimAssist", 0, Category.COMBAT);
	}
	
	@Override
	public void setup() {
		smooth = new DoubleSetting("Smooth", this, 5, 1, 30);
		degrees = new DoubleSetting("Degrees", this, 180, 0, 360);
		range = new DoubleSetting("Range", this, 5, 1, 7);
		this.rSetting(smooth);
        this.rSetting(degrees);
        this.rSetting(range);
	}
	
	
	@EventTarget
    public void onUpdate(EventPreMotionUpdate event) {
        if (MC.thePlayer.isEntityAlive()) {
            for (Object entity : MC.theWorld.loadedEntityList) {
                if (entity instanceof EntityLivingBase) {
                    if (CombatUtils.isEntityValid((Entity)entity) && ((EntityLivingBase)entity).isEntityAlive()) {
                        this.target = (EntityLivingBase)entity;
                    }
                    else {
                        this.target = null;
                    }
                    if (this.target == null) {
                        continue;
                    }
                    final EntityPlayerSP player4;
                    final EntityPlayerSP entityPlayerSP;
                    final EntityPlayerSP player3 = entityPlayerSP = (player4 = MC.thePlayer);
                    MC.thePlayer.rotationPitch += (float)(CombatUtils.getPitchChange(this.target) / smooth.getValue());
                    final EntityPlayerSP player6;
                    final EntityPlayerSP entityPlayerSP2;
                    final EntityPlayerSP player5 = entityPlayerSP2 = (player6 = MC.thePlayer);
                    MC.thePlayer.rotationYaw += (float)(CombatUtils.getYawChange(this.target) / smooth.getValue());
                }
            }
        }
    }
    
    

}
