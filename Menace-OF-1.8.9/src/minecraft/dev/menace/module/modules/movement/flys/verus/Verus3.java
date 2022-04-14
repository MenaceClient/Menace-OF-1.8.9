package dev.menace.module.modules.movement.flys.verus;

import java.awt.Color;

import dev.menace.Menace;
import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventJump;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import dev.menace.gui.hud.ScreenPosition;
import dev.menace.module.modules.movement.flys.FlightBase;
import dev.menace.utils.entity.self.PlayerUtils;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.MSTimer;
import dev.menace.utils.misc.notif.Notification;
import dev.menace.utils.misc.notif.TimeredNotification;
import net.minecraft.block.BlockAir;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Verus3 extends FlightBase {
	public boolean flyable = false;
	public boolean damage = false;
	public MSTimer timer = new MSTimer();
	private boolean shot;
	private double count;
	float oldYaw;
	float oldPitch;
	int C03Count = 0;
	private boolean C03Sent = false;
	int oldSlot = -1;
	boolean doUp;

	@Override
	public void onEnable() {

		if (!MC.thePlayer.onGround) {
			Menace.instance.moduleManager.flightModule.toggle();
			Menace.instance.notificationManager.addNotification(new Notification("You can only enable this fly on the ground.", Color.YELLOW, 1000L));
			return;
		}
		
		if (Menace.instance.moduleManager.flightModule.dmgMode.getString().equalsIgnoreCase("Basic")) {
			MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY + 3.25, MC.thePlayer.posZ, false));
			MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY, MC.thePlayer.posZ, false));
			MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY, MC.thePlayer.posZ, true));
			MC.thePlayer.motionX = 0.0;
			MC.thePlayer.motionY = 0.0;
			MC.thePlayer.motionZ = 0.0;
			flyable = true;
			MC.timer.timerSpeed = 0.5f;
			timer.reset();
		} else if (Menace.instance.moduleManager.flightModule.dmgMode.getString().equalsIgnoreCase("Bypass")) {
			
			C03Count = 0;
			C03Sent = false;
			damage = false;
			
		} else if (Menace.instance.moduleManager.flightModule.dmgMode.getString().equalsIgnoreCase("Bow")) {
			damage = false;
			oldYaw = MC.thePlayer.rotationYaw;
			oldPitch = MC.thePlayer.rotationPitch;
		} else {
			damage = false;
		}

	}

	@Override
	public void onPreMotion(EventPreMotionUpdate event) {
		if (!Menace.instance.moduleManager.flightModule.dmgMode.getString().equalsIgnoreCase("Bow") || damage || shot) return;
		/*
		if (!(MC.thePlayer.getHeldItem().getItem() instanceof ItemBow)) {
			for (int i = 0; i <= 9; i++) {
				if (MC.thePlayer.inventory.getStackInSlot(i).getItem() != null && MC.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBow) {
					oldSlot = MC.thePlayer.inventory.currentItem;
					MC.thePlayer.inventory.currentItem = i;
					break;
				}
			}
		}*/
		
		if (MC.thePlayer.getHeldItem() != null &&
				MC.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
			Packet C07 = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN);
			Packet C08 = new C08PacketPlayerBlockPlacement(MC.thePlayer.inventory.getCurrentItem());
			MC.thePlayer.rotationPitch = -90f;
			count++;
			if (count >= 4) {
				MC.thePlayer.sendQueue.addToSendQueue(C07);
				count = 0;
			} else if (count == 1) {
				MC.thePlayer.sendQueue.addToSendQueue(C08);
			}
		}
	}

	@Override
	public void onDisable() {
		damage = false;
		flyable = false;
		shot = false;
		C03Sent = false;
		MC.timer.timerSpeed = 1f;
		doUp = false;
	}

	@Override
	public void onUpdate() {

		if (Menace.instance.moduleManager.flightModule.dmgMode.getString().equalsIgnoreCase("Bypass") && C03Count >= 4 && !damage && C03Sent == false) {
			Menace.instance.notificationManager.addNotification(new Notification("Damaging", 1000L));
			MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
			MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 1.5, MC.thePlayer.posZ), 1, new net.minecraft.item.ItemStack(Blocks.stone.getItem(MC.theWorld, new BlockPos(-1, -1, -1))), 0f, 0.94f, 0f));
			MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY + 3.05, MC.thePlayer.posZ, false));
			MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY, MC.thePlayer.posZ, false));
			MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY+0.41999998688697815, MC.thePlayer.posZ, true));
			MC.thePlayer.motionX = 0.0;
			MC.thePlayer.motionY = 0.0;
			MC.thePlayer.motionZ = 0.0;
			MC.timer.timerSpeed = 0.5f;
			timer.reset();
			C03Sent = true;
		}
		
		if (Menace.instance.moduleManager.flightModule.dmgMode.getString().equalsIgnoreCase("Bow") && !shot && !damage) {
			for (Entity e : MC.theWorld.loadedEntityList) {
				if (e instanceof EntityArrow && ((EntityArrow)e).shootingEntity == MC.thePlayer && !((EntityArrow)e).inGround) {
					shot = true;
					MC.thePlayer.rotationPitch = oldPitch;
					MC.thePlayer.rotationYaw = oldYaw;
					if (oldSlot != -1) {
						MC.thePlayer.inventory.currentItem = oldSlot;
					}
				}
			}
		}

		if (!damage && MC.thePlayer.hurtTime > 0) {
			damage = true;
			MC.thePlayer.motionX = 0.0;
			MC.thePlayer.motionY = 0.0;
			MC.thePlayer.motionZ = 0.0;
			flyable = true;
			doUp = true;
			MC.timer.timerSpeed = 0.5f;
			timer.reset();
			Menace.instance.notificationManager.addNotification(new TimeredNotification("$TIMER$ms till slowdown", timer, 1500L));
			//MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY + 0.5, MC.thePlayer.posZ, true));
		}

		if (!damage) {
			return;
		}

		if (timer.hasTimePassed(1500) && flyable) {
			if (flyable) {
				PlayerUtils.strafe(0.48f);
			}
			Menace.instance.notificationManager.addNotification(new Notification("Slowing down fly", 1000L));
			flyable = false;
		}

		if (!flyable) {
			MC.gameSettings.keyBindJump.pressed = false;
			if (MC.thePlayer.onGround) {
				MC.thePlayer.jump();
				PlayerUtils.strafe(0.48F);
			} else {
				PlayerUtils.strafe();
			}
			return;
		}

		if (timer.hasTimePassed(300)) {
			MC.timer.timerSpeed = 1f;
		}

		if (flyable && timer.hasTimePassed(100)) {
			PlayerUtils.strafe((float) Menace.instance.moduleManager.flightModule.speed.getValue());
			if (MC.thePlayer.isCollidedHorizontally) flyable = false;
		} else if (!timer.hasTimePassed(100)) {
			MC.thePlayer.motionX = 0.0;
			MC.thePlayer.motionY = 0.0;
			MC.thePlayer.motionZ = 0.0;
		}
	}

	@Override
	public void onSendPacket(EventSendPacket event) {
		
		if ((event.getPacket() instanceof C03PacketPlayer ||
				event.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition ||
				event.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook ||
				event.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook)
				&& Menace.instance.moduleManager.flightModule.dmgMode.getString().equalsIgnoreCase("Bypass")
				&& !damage && C03Sent == false) {
			event.setCancelled(true);
			C03Count++;
		}
		
		if (event.getPacket() instanceof C03PacketPlayer && damage && flyable) {
			((C03PacketPlayer) event.getPacket()).setOnGround(true);
		}
	}

	@Override
	public void onCollision(EventCollide event) {
		if (event.getBlock() instanceof BlockAir && event.getPosY() <= this.launchY && damage) {
			event.setBoundingBox(AxisAlignedBB.fromBounds(event.getPosX(), event.getPosY(), event.getPosZ(), event.getPosX() + 1.0, this.launchY, event.getPosZ() + 1.0));
		}
	}

	@Override
	public void onJump(EventJump event) {
		if (flyable) {
			event.setCancelled(true);
		}
	}

	@Override
	public void onMove(EventMove event) {
		
		if (doUp) {
			event.y = 0.5;
			this.launchY += 0.5;
			doUp = false;
		}
		
		if (flyable && MC.gameSettings.keyBindJump.isKeyDown()) {
			event.y = 0.5;
			this.launchY += 0.5;
		}
		
		if (!damage) {
			event.setCancelled(true);
		}
	}
}
