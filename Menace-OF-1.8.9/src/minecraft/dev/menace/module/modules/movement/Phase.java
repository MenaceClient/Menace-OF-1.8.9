package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.utils.entity.self.PlayerUtils;

public class Phase extends Module {
    public Phase() {
        super("Phase", 0, Category.MOVEMENT);
    }

    private int reset;
    private double dist = 1D;

    @EventTarget
    public void onUpdate(EventUpdate event) {
    	MC.thePlayer.noClip = true;
        reset -= 1;
        double xOff = 0;
        double zOff = 0;
        double multi = 2.6D;
        double mx = Math.cos(Math.toRadians(MC.thePlayer.rotationYaw + 90F));
        double mz = Math.sin(Math.toRadians(MC.thePlayer.rotationYaw + 90F));
        xOff = MC.thePlayer.moveForward * 2.6D * mx + MC.thePlayer.moveStrafing * 2.6D * mz;
        zOff = MC.thePlayer.moveForward * 2.6D * mz + MC.thePlayer.moveStrafing * 2.6D * mx;
        if(PlayerUtils.isInsideBlock() && MC.thePlayer.isSneaking())
            reset = 1;
        if(reset > 0)
            MC.thePlayer.boundingBox.offsetAndUpdate(xOff, 0, zOff);
    }

    @EventTarget
    public boolean onCollision(EventCollide event) {
        if((PlayerUtils.isInsideBlock() && MC.gameSettings.keyBindJump.isKeyDown()) || (!(PlayerUtils.isInsideBlock()) && event.getBoundingBox() != null && event.getBoundingBox().maxY > MC.thePlayer.boundingBox.minY && MC.thePlayer.isSneaking()))
            event.setBoundingBox(null);
        return true;
        
    }
    
    @Override
    public void onDisable() {
    	super.onDisable();
    	
    	MC.thePlayer.noClip = false;
    }
}