package dev.menace.module.modules.player;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.*;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.player.PlayerUtils;
import dev.menace.utils.world.BlockUtils;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoFallModule extends BaseModule {

	ListSetting mode;
	boolean shouldSpoof;
	private float lastTickFallDist, fallDist;
	//Test
	private int test = 0;

	//Water Clutch
	private boolean placedWater = false;
	private BlockPos waterPos;
	private int lookTicks = 0;
	private int bucketSlot = -1;
	private int oldSlot = -1;

	public NoFallModule() {
		super("NoFall", "Cancels fall damage", Category.PLAYER, 0);
	}

	@Override
	public void setup() {
		mode = new ListSetting("Mode", true, "SpoofGround", new String[] {"SpoofGround", "NoGround", "Verus", "Vulcan", /*"Matrix",*/ "Hypixel", "Experimental"});
		this.rSetting(mode);
		super.setup();
	}

	@Override
	public void onEnable() {
		shouldSpoof = false;
		placedWater = false;
		waterPos = null;
		lookTicks = 0;
		bucketSlot = -1;
		oldSlot = -1;
		test = 0;
		super.onEnable();
	}

	@EventTarget
	public void onEvent(EventUpdate e) {
		if(mode.getValue().equalsIgnoreCase("Hypixel")) {
			if (mc.thePlayer.fallDistance > 3) {
				PacketUtils.sendPacket(new C03PacketPlayer(true));
				//shouldSpoof = true;
			}
			if (mc.thePlayer.fallDistance > 4 && mc.thePlayer.motionY < -0.1) {
				mc.thePlayer.setSprinting(!mc.thePlayer.isSprinting());
				mc.thePlayer.setPosition(mc.thePlayer.lastTickPosX, mc.thePlayer.posY, mc.thePlayer.lastTickPosZ);
			}
		}
	}

	@EventTarget
	public void onCollide(EventCollide event) {
		this.setDisplayName(mode.getValue());

		if (mode.getValue().equalsIgnoreCase("Verus") && mc.thePlayer.fallDistance > 2.5) {
			if (Menace.instance.moduleManager.flightModule.isToggled())
				return;
			event.setBoundingBox(new AxisAlignedBB(-2, -1, -2, 2, 1, 2).offset(event.getPosX(), event.getPosY(), event.getPosZ()));

		}
	}

	@EventTarget
	public void onMove(EventMove event) {
		if (mode.getValue().equalsIgnoreCase("Vulcan")) {

			if (mc.thePlayer.fallDistance == 0)
				fallDist = 0;

			fallDist += mc.thePlayer.fallDistance - lastTickFallDist;
			lastTickFallDist = mc.thePlayer.fallDistance;

			double num = 0.015625; //Funny Number

			double mathGround = Math.round(event.getY() / num) * num;

			if (fallDist > 1.3) {
				event.setX(0);
				event.setZ(0);
			}

			if (fallDist > 1.3 && mc.thePlayer.ticksExisted % 5 == 0) {
				mc.thePlayer.setPosition(mc.thePlayer.lastTickPosX, mc.thePlayer.posY + mathGround, mc.thePlayer.lastTickPosZ);
				event.setY(mathGround);

				mathGround = Math.round(event.getY() / num) * num;
				if (Math.abs(mathGround - event.getY()) < 0.01) {
					if (mc.thePlayer.motionY < -0.4) mc.thePlayer.motionY = -0.4;
					shouldSpoof = true;
				}
			}
		}
	}

	@EventTarget
	public void onPreMotion(EventPreMotion event) {
		if (mode.getValue().equalsIgnoreCase("Experimental")) {
			double groundDist = mc.thePlayer.posY - PlayerUtils.calculateGround();

			//Find Bucket Slot if not found
			Item itemToFind = (!placedWater) ? Items.water_bucket : Items.bucket;
			if ((bucketSlot == -1 || mc.thePlayer.inventory.getStackInSlot(bucketSlot) == null || mc.thePlayer.inventory.getStackInSlot(bucketSlot).getItem() != itemToFind)) {
				for (int i = 0; i < 9; i++) {
					if (mc.thePlayer.inventory.getStackInSlot(i) != null && mc.thePlayer.inventory.getStackInSlot(i).getItem() == itemToFind) {
						bucketSlot = i;
						break;
					}
				}

				if (bucketSlot != -1 && (mc.thePlayer.inventory.getStackInSlot(bucketSlot) == null || mc.thePlayer.inventory.getStackInSlot(bucketSlot).getItem() != itemToFind)) {
					bucketSlot = -1;
				}
			}

			//Place the water
			if (PlayerUtils.isBlockUnder() && groundDist < 4 && mc.thePlayer.fallDistance > 3 && bucketSlot != -1 && !placedWater) {
				mc.thePlayer.rotationPitch = 90;
				if (groundDist < 2) {
					BlockPos ground = new BlockPos(mc.thePlayer.posX, Math.round(PlayerUtils.calculateGround()), mc.thePlayer.posZ);
					oldSlot = mc.thePlayer.inventory.currentItem;
					mc.thePlayer.inventory.currentItem = bucketSlot;
					if (PlayerUtils.placeBlockSimple(ground)) {
						mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
					}
					waterPos = ground;
					placedWater = true;
				}
			}

			//Remove the water
			if (mc.thePlayer.isInWater() && mc.thePlayer.fallDistance == 0 && bucketSlot != -1 && placedWater && waterPos != null) {
				if (lookTicks >= 10) {
					if (PlayerUtils.placeBlockSimple(waterPos)) {
						mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
					}
					mc.thePlayer.inventory.currentItem = oldSlot;
					waterPos = null;
					placedWater = false;
					lookTicks = 0;
					bucketSlot = -1;
					oldSlot = -1;
				}else {
					mc.thePlayer.rotationYaw = PlayerUtils.getDirectionToBlock(waterPos.getX(), waterPos.getY(), waterPos.getZ(), EnumFacing.UP)[0];
					mc.thePlayer.rotationPitch = PlayerUtils.getDirectionToBlock(waterPos.getX(), waterPos.getY(), waterPos.getZ(), EnumFacing.UP)[1];
					lookTicks++;
				}
			}

			//Fail safe reset
			/*if (!mc.thePlayer.isInWater() && mc.thePlayer.onGround && mc.thePlayer.fallDistance == 0 && placedWater && waterPos != null) {
				waterPos = null;
				placedWater = false;
				lookTicks = 0;
				oldSlot = -1;
			}*/
		} else if (mode.getValue().equalsIgnoreCase("Matrix")) {
			if (mc.thePlayer.fallDistance == 0) {
				fallDist = 0;
				test = 0;
			}

			fallDist += mc.thePlayer.fallDistance - lastTickFallDist;
			lastTickFallDist = mc.thePlayer.fallDistance;

			if (PlayerUtils.isBlockUnder()) {
				if (fallDist > 2) {
					//Slow down the player
					mc.thePlayer.motionX *= 0.1;
					mc.thePlayer.motionY *= 0.1;
					mc.thePlayer.motionZ *= 0.1;
					ChatUtils.message("Matrix: " + fallDist + " " + MovementUtils.getSpeed());
				}

				if (fallDist > 3.5 && MovementUtils.getSpeed() < 0.2) {
					event.setOnGround(true);
					fallDist = 0;
					//test++;
				}
			}
		}
	}

	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		if (mode.getValue().equalsIgnoreCase("NoGround") && event.getPacket() instanceof C03PacketPlayer && !mc.thePlayer.isInWater()) {
			((C03PacketPlayer)event.getPacket()).setOnGround(false);
		}

		if (event.getPacket() instanceof C03PacketPlayer && (mode.getValue().equalsIgnoreCase("SpoofGround") && mc.thePlayer.fallDistance - mc.thePlayer.motionY > 3 || shouldSpoof)) {
			((C03PacketPlayer)event.getPacket()).setOnGround(true);
			shouldSpoof = false;
		}
	}

}
