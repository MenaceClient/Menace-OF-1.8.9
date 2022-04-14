package dev.menace.module.modules.misc;

import java.util.ArrayList;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.BoolSetting;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.world.BlockUtils;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class Anticheat extends Module {

	//Settings
	BoolSetting showFlags;
	
	public Anticheat() {
		super("AntiCheat", 0, Category.MISC);
	}
	
	@Override
	public void setup() {
		showFlags = new BoolSetting("Showflags", this, false);
		this.rSetting(showFlags);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		for (Entity e : MC.theWorld.loadedEntityList) {
			if (e instanceof EntityPlayer && e != MC.thePlayer) check((EntityPlayer) e);
		}
	}
	
	public void check(EntityPlayer p) {

		if (p.capabilities.isCreativeMode || p.reported) return;
		
		if (p.flags >=300) {
			reportPlayer(p);
		}
		
		if (flight1(p)) flagPlayer(p, "flight 1");
		
		if (flight2(p)) flagPlayer(p, "flight 2");
		
		if (nofall(p)) flagPlayer(p, "nofall");
		
		if (jesus(p)) flagPlayer(p, "jesus");
		
		if (step(p)) flagPlayer(p, "step");
		
	}	
	
	public boolean flight1(EntityPlayer p) {
		if (p.capabilities.isFlying) return true;
		else return false;
	}
	
	public boolean flight2(EntityPlayer p) {
		if (!p.onGround && p.motionY == 0f && !(p.jumpTicks > 0)) return true;
		else return false;
	}
	
	public boolean nofall(EntityPlayer p) {
		if (p.onGround && BlockUtils.getBlock(new BlockPos(p.posX, p.posY - 1, p.posZ)) instanceof BlockAir && !p.isSneaking()) return true;
		else return false;
	}
	
	public boolean jesus(EntityPlayer p) {
		if (p.motionY == 0f && BlockUtils.getBlock(new BlockPos(p.posX, p.posY - 1, p.posZ)) instanceof BlockLiquid) return true;
		else return false;
	}
	
	public boolean step(EntityPlayer p) {
		if (p.stepHeight > 1f) return true;
		else return false;
	}
	
	public void reportPlayer(EntityPlayer p) {
		//ChatUtils.out("[Menace Anticheat] " + p.getName() + " has been detected using " + flag);
		ChatUtils.ac(p.getName() + " has been detected cheating");
		p.reported = true;
	}
	
	public void flagPlayer(EntityPlayer p, String flag) {
		if (showFlags.isChecked()) {
			ChatUtils.ac(p.getName() + " has been flagged for " + flag);
		}
		p.flags += 1;
	}
}
