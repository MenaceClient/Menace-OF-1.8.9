package dev.menace.module.modules.movement.flight.vanilla;

import dev.menace.Menace;
import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventJump;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventPreMotion;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.modules.movement.flight.FlightBase;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.block.BlockAir;
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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class DamageFly extends FlightBase {
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
	int jumpCount = 0;

	@Override
	public void onEnable() {

		if (!mc.thePlayer.onGround) {
			Menace.instance.moduleManager.flightModule.setToggled(false);
			//Menace.instance.notificationManager.addNotification(new Notification("You can only enable this fly on the ground.", Color.YELLOW, 1000L));
			return;
		}
		
		if (Menace.instance.moduleManager.flightModule.dmgMode.getValue().equalsIgnoreCase("Basic")) {
			mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.25, mc.thePlayer.posZ, false));
			mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
			mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
			mc.thePlayer.motionX = 0.0;
			mc.thePlayer.motionY = 0.0;
			mc.thePlayer.motionZ = 0.0;
			flyable = true;
			mc.timer.timerSpeed = 0.5f;
			timer.reset();
		} else if (Menace.instance.moduleManager.flightModule.dmgMode.getValue().equalsIgnoreCase("Verus")) {
			
			C03Count = 0;
			C03Sent = false;
			damage = false;
			
		} else if (Menace.instance.moduleManager.flightModule.dmgMode.getValue().equalsIgnoreCase("Bow")) {
			damage = false;
			oldYaw = mc.thePlayer.rotationYaw;
			oldPitch = mc.thePlayer.rotationPitch;
		} else {
			damage = false;
		}

	}

	@Override
	public void onPreMotion(EventPreMotion event) {
		if (!Menace.instance.moduleManager.flightModule.dmgMode.getValue().equalsIgnoreCase("Bow") || damage || shot) return;	
		if (mc.thePlayer.getHeldItem() != null &&
				mc.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
			C07PacketPlayerDigging C07 = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN);
			C08PacketPlayerBlockPlacement C08 = new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem());
			event.setPitch(-90F);
			count++;
			if (count >= 4) {
				mc.thePlayer.sendQueue.addToSendQueue(C07);
				count = 0;
				shot = true;
				if (oldSlot != -1) {
					mc.thePlayer.inventory.currentItem = oldSlot;
				}
			} else if (count == 1) {
				mc.thePlayer.sendQueue.addToSendQueue(C08);
			}
		}
	}

	@Override
	public void onDisable() {
		damage = false;
		flyable = false;
		shot = false;
		C03Sent = false;
		mc.timer.timerSpeed = 1f;
		doUp = false;
		jumpCount = 0;
	}

	@Override
	public void onUpdate() {

		if (Menace.instance.moduleManager.flightModule.dmgMode.getValue().equalsIgnoreCase("Verus") && C03Count >= 4 && !damage && !C03Sent) {
			//Menace.instance.notificationManager.addNotification(new Notification("Damaging", 1000L));
			PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
			PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.5, mc.thePlayer.posZ), 1, new net.minecraft.item.ItemStack(Blocks.stone.getItem(mc.theWorld, new BlockPos(-1, -1, -1))), 0f, 0.94f, 0f));
			PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.05, mc.thePlayer.posZ, false));
			PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
			PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY+0.41999998688697815, mc.thePlayer.posZ, true));
			mc.thePlayer.motionX = 0.0;
			mc.thePlayer.motionY = 0.0;
			mc.thePlayer.motionZ = 0.0;
			mc.timer.timerSpeed = 0.5f;
			timer.reset();
			C03Sent = true;
		}
		
		if (Menace.instance.moduleManager.flightModule.dmgMode.getValue().equalsIgnoreCase("Jump") && jumpCount < 3 && mc.thePlayer.onGround) {
			mc.thePlayer.jump();
			jumpCount++;
		}

		if (!damage && mc.thePlayer.hurtTime > 0) {
			damage = true;
			mc.thePlayer.motionX = 0.0;
			mc.thePlayer.motionY = 0.0;
			mc.thePlayer.motionZ = 0.0;
			flyable = true;
			doUp = true;
			mc.timer.timerSpeed = 0.5f;
			timer.reset();
			//Menace.instance.notificationManager.addNotification(new TimeredNotification("$TIMER$ms till slowdown", timer, 1500L));
			//PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY + 0.5, MC.thePlayer.posZ, true));
		}

		if (!damage) {
			return;
		}

		if (timer.hasTimePassed(1500) && flyable) {
			MovementUtils.strafe(0.48f);
			//Menace.instance.notificationManager.addNotification(new Notification("Slowing down fly", 1000L));
			flyable = false;
		}
		
		if (!flyable && Menace.instance.moduleManager.flightModule.dmgMode.getValue().equalsIgnoreCase("Verus")) {
			mc.gameSettings.keyBindJump.pressed = false;
			if (mc.thePlayer.onGround) {
				mc.thePlayer.jump();
				MovementUtils.strafe(0.48F);
			} else {
				MovementUtils.strafe();
			}
			return;
		}

		if (timer.hasTimePassed(300)) {
			mc.timer.timerSpeed = 1f;
		}

		if (flyable && timer.hasTimePassed(100)) {
			MovementUtils.strafe((float) Menace.instance.moduleManager.flightModule.speed.getValue());
			if (mc.thePlayer.isCollidedHorizontally && Menace.instance.moduleManager.flightModule.dmgMode.getValue().equalsIgnoreCase("Verus")) flyable = false;
		} else if (!timer.hasTimePassed(100)) {
			mc.thePlayer.motionX = 0.0;
			mc.thePlayer.motionY = 0.0;
			mc.thePlayer.motionZ = 0.0;
		}
	}

	@Override
	public void onSendPacket(EventSendPacket event) {
		
		if ((event.getPacket() instanceof C03PacketPlayer ||
				event.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition ||
				event.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook ||
				event.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook)
				&& Menace.instance.moduleManager.flightModule.dmgMode.getValue().equalsIgnoreCase("Verus")
				&& !damage && !C03Sent) {
			event.setCancelled(true);
			C03Count++;
		}
		
		if (event.getPacket() instanceof C03PacketPlayer && damage && flyable) {
			((C03PacketPlayer) event.getPacket()).setOnGround(true);
		}
		if (Menace.instance.moduleManager.flightModule.dmgMode.getValue().equalsIgnoreCase("Jump") &&
				jumpCount < 3 && event.getPacket() instanceof C03PacketPlayer) {
			((C03PacketPlayer) event.getPacket()).setOnGround(false);
		}
		
	}

	@Override
	public void onCollide(EventCollide event) {
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
			event.setY(0.5);
			this.launchY += 0.5;
			doUp = false;
		}
		
		if (flyable && mc.gameSettings.keyBindJump.isKeyDown()) {
			event.setY(0.5);
			this.launchY += 0.5;
		}
		
		if (!damage) {
			event.setX(0);
			event.setZ(0);
			if (!Menace.instance.moduleManager.flightModule.dmgMode.getValue().equalsIgnoreCase("Jump"))
				event.setY(0);
		}
	}
}
