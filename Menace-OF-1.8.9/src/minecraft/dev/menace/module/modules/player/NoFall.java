package dev.menace.module.modules.player;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.modules.movement.flys.FlightMode;
import dev.menace.module.settings.StringSetting;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class NoFall extends Module{

	private boolean needSpoof = false;
	private boolean packet = false;
	private float lastTickFallDist, fallDist;
	
	//Settings
	StringSetting mode;
	
	public NoFall() {
		super("NoFall", Keyboard.KEY_NONE, Category.PLAYER);
	}
	
	@Override
	public void setup() {
		ArrayList<String> options = new ArrayList<String>();
		options.add("SpoofGround");
		options.add("NoGround");
		options.add("Verus");
		options.add("Verus2");
		options.add("Verus3");
		options.add("Vulcan");
		mode = new StringSetting("Mode", this, "SpoofGround", options);
		this.rSetting(mode);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		this.setDisplayName("NoFall §7[" + mode.getString() + "]");
		MC.timer.timerSpeed = 1F;
		needSpoof = false;
		packet = false;
	}
	
	@Override
	public void onDisable() {
		MC.timer.timerSpeed = 1F;
		super.onDisable();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		
		if (mode.getString().equalsIgnoreCase("verus")) {
			if (Menace.instance.moduleManager.flightModule.isToggled() && Menace.instance.moduleManager.flightModule.flightMode != FlightMode.JETPACK)
				return;
			
			if (MC.thePlayer.fallDistance - MC.thePlayer.motionY > 3) {
				packet = true;
				//MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 1.5, MC.thePlayer.posZ), 1, new ItemStack(Blocks.stone.getItem(MC.theWorld, new BlockPos(-1, -1, -1))), 0f, 0.94f, 0f));
                MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
                MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 1.5, MC.thePlayer.posZ), 1, new ItemStack(Blocks.stone.getItem(MC.theWorld, new BlockPos(-1, -1, -1))), 0f, 0.94f, 0f));
				packet = false;
				MC.thePlayer.motionY = 0.1;
                MC.thePlayer.fallDistance = 0.0f;
                MC.thePlayer.motionX *= 1.3;
                MC.thePlayer.motionZ *= 1.3;
                needSpoof = true;
            }
		}
		
		if (mode.getString().equalsIgnoreCase("verus2")) {
			if (Menace.instance.moduleManager.flightModule.isToggled())
				return;
			
			if (MC.thePlayer.fallDistance - MC.thePlayer.motionY > 4) {
                needSpoof = true;
                MC.thePlayer.motionY = -10;
                MC.thePlayer.fallDistance = 0.0f;
                MC.thePlayer.motionX *= 0.6;
                MC.thePlayer.motionZ *= 0.6;
            }
		}
		
		if (mode.getString().equalsIgnoreCase("verus3")) {
            if (MC.thePlayer.fallDistance > 3) {
                MC.timer.timerSpeed = .4F;
                packet = true;
                MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
                MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 1.5, MC.thePlayer.posZ), 1, new ItemStack(Blocks.stone.getItem(MC.theWorld, new BlockPos(-1, -1, -1))), 0f, 0.94f, 0f));
                packet = false;
                //MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer(true));
                needSpoof = true;
                MC.thePlayer.fallDistance = 0;
            } else {
                MC.timer.timerSpeed = 1F;
            }
		}
		
	}
	
	@EventTarget
	public void onPreMotion(EventPreMotionUpdate event) {
		if (mode.getString().equalsIgnoreCase("vulcan")) {
			
	        if (MC.thePlayer.fallDistance == 0)
	            fallDist = 0;

	        fallDist += MC.thePlayer.fallDistance - lastTickFallDist;
	        lastTickFallDist = MC.thePlayer.fallDistance;
			
	        fallDist += MC.thePlayer.fallDistance - lastTickFallDist;
	        lastTickFallDist = MC.thePlayer.fallDistance;
			
            double mathGround = Math.round(event.y / 0.015625) * 0.015625;

            if (fallDist > 1.3 && MC.thePlayer.ticksExisted % 15 == 0) {
                MC.thePlayer.setPosition(MC.thePlayer.posX, mathGround, MC.thePlayer.posZ);
                event.y = mathGround;

                mathGround = Math.round(event.y / 0.015625) * 0.015625;
                if (Math.abs(mathGround - event.y) < 0.01) {
                    if (MC.thePlayer.motionY < -0.4) MC.thePlayer.motionY = -0.4;

                    //needSpoof = true;
                    MC.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                    MC.timer.timerSpeed = 0.9f;
                }
            } else if (MC.timer.timerSpeed == 0.9f) {
                MC.timer.timerSpeed = 1;
            }
		}
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		
		if (needSpoof && event.getPacket() instanceof C03PacketPlayer) {
			((C03PacketPlayer)event.getPacket()).setOnGround(true);
            needSpoof = false;
		}
		
		if (mode.getString().equalsIgnoreCase("NoGround") && event.getPacket() instanceof C03PacketPlayer && !MC.thePlayer.isInWater()) {
			((C03PacketPlayer)event.getPacket()).setOnGround(false);
		}
		
		if (mode.getString().equalsIgnoreCase("SpoofGround") && event.getPacket() instanceof C03PacketPlayer && MC.thePlayer.fallDistance - MC.thePlayer.motionY > 3) {
			((C03PacketPlayer)event.getPacket()).setOnGround(true);
		}
		
		if (event.getPacket() instanceof C02PacketUseEntity && ((C02PacketUseEntity)event.getPacket()).getAction() == C02PacketUseEntity.Action.ATTACK && packet) {
			event.setCancelled(true);
		}
	}

}
