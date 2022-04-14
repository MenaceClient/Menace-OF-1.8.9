package dev.menace.module.modules.movement;

import java.util.ArrayList;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.StringSetting;
import dev.menace.utils.entity.self.PlayerUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

public class LongJump extends Module {

	//Settings
	StringSetting mode;
	
	public LongJump() {
		super("LongJump", 0, Category.MOVEMENT);
	}
	
	@Override
	public void setup() {
		ArrayList<String> options = new ArrayList<String>();
		options.add("Vanilla");
		options.add("Verus");
		mode = new StringSetting("Mode", this, "Vanilla", options);
		this.rSetting(mode);
	}
	
	@Override
	public void onEnable() {
		if (mode.getString().equalsIgnoreCase("Verus")) {
			MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
			MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 1.5, MC.thePlayer.posZ), 1, new ItemStack(Blocks.stone.getItem(MC.theWorld, new BlockPos(-1, -1, -1))), 0, 0.94f, 0));
            double x = MC.thePlayer.posX;
            double y = MC.thePlayer.posY;
            double z = MC.thePlayer.posZ;
            MC.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 3.35, z, false));
            MC.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
            MC.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
            MC.thePlayer.motionX = 0;
            MC.thePlayer.motionY = 0;
            MC.thePlayer.motionZ = 0;
            MC.thePlayer.setPosition(MC.thePlayer.posX, MC.thePlayer.posY + 0.42, MC.thePlayer.posZ);
            MC.thePlayer.posY += .6;
		}
		super.onEnable();
	}
	
	@EventTarget
	public void onPreMotion(EventPreMotionUpdate event) {
		if (mode.getString().equalsIgnoreCase("Vanilla")) {
			MC.thePlayer.jumpMovementFactor = 0.07f;
		}
		
		if (mode.getString().equalsIgnoreCase("Verus")) {
            if (MC.thePlayer.motionY > .5) {
                MC.thePlayer.motionY = -.4F;
                this.toggle();
                MC.timer.timerSpeed = 1F;
            }
            if (MC.thePlayer.hurtTime != 0) {
                if (MC.thePlayer.onGround) {
                    MC.thePlayer.jump();
                } else {
                	MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
                	MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 1.5, MC.thePlayer.posZ), 1, new ItemStack(Blocks.stone.getItem(MC.theWorld, new BlockPos(-1, -1, -1))), 0, 0.94f, 0));
                    MC.thePlayer.motionY = .6F;
                    PlayerUtils.strafe(6);
                }
            }
		}
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		MC.timer.timerSpeed = 1F;
		MC.thePlayer.jumpMovementFactor = 0.02f;
	}

}
