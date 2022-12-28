package dev.menace.module.modules.combat;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventPostMotion;
import dev.menace.event.events.EventPreMotion;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventWorldChange;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.DontSaveState;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.MathUtils;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.player.PlayerUtils;
import dev.menace.utils.player.RayCastUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@DontSaveState
public class KillAuraModule extends BaseModule {

	MSTimer delayTimer = new MSTimer();
	public final ArrayList<Entity> botlist = new ArrayList<>();
	public EntityLivingBase target;
	long delay;
	boolean blocking;
	public boolean shouldFakeBlock;
	float[] lastRotations = new float[2];
	Random rand = new Random();
	public SliderSetting reach;
	public SliderSetting minCPS;
	public SliderSetting maxCPS;
	SliderSetting ticksExisted;
	SliderSetting fov;
	public ListSetting mode;
	ListSetting filter;
	ListSetting autoblock;
	public ToggleSetting keepSprint;
	ToggleSetting noswing;
	ToggleSetting throughwalls;
	ToggleSetting ininv;
	ToggleSetting raycast;
	ToggleSetting autoDisable;
	ToggleSetting antiBot;
	ToggleSetting players;
	ToggleSetting hostiles;
	ToggleSetting passives;
	ToggleSetting invisibles;

	public KillAuraModule() {
		super("KillAura", Category.COMBAT, 0);
	}

	@Override
	public void setup() {
		reach = new SliderSetting("Reach", true, 3, 1, 7, 0.1, false);
		minCPS = new SliderSetting("MinCPS", true, 10, 1, 20, true) {
			@Override
			public void constantCheck() {
				if (this.getValue() > Menace.instance.moduleManager.killAuraModule.maxCPS.getValue()) {
					this.setValue(Menace.instance.moduleManager.killAuraModule.maxCPS.getValue());
				}
			}
		};
		maxCPS = new SliderSetting("MaxCPS", true, 10, 1, 20, true) {
			@Override
			public void constantCheck() {
				if (this.getValue() < Menace.instance.moduleManager.killAuraModule.minCPS.getValue()) {
					this.setValue(Menace.instance.moduleManager.killAuraModule.minCPS.getValue());
				}
			}
		};
		ticksExisted = new SliderSetting("TicksExisted", true, 10, 0 , 100, true);
		fov = new SliderSetting("FOV", true, 360, 0, 360, false);
		filter = new ListSetting("Filter", true, "Health", new String[] {"Health", "Distance", "Angle", "TicksExisted"});
		autoblock = new ListSetting("AutoBlock", true, "None", new String[] {"None", "Fake", "Vanilla"});
		keepSprint = new ToggleSetting("KeepSprint", true, true);
		noswing = new ToggleSetting("NoSwing", true, false);
		throughwalls = new ToggleSetting("ThroughWalls", true, false);
		ininv = new ToggleSetting("InInventory", true, false);
		raycast = new ToggleSetting("RayCast", true, false);
		autoDisable = new ToggleSetting("AutoDisable", true, true);
		antiBot = new ToggleSetting("AntiBot", true, true);
		players = new ToggleSetting("Players", true, true);
		hostiles = new ToggleSetting("Hostiles", true, true);
		passives = new ToggleSetting("Passives", true, false);
		invisibles = new ToggleSetting("Invisibles", true, false);
		this.rSetting(reach);
		this.rSetting(minCPS);
		this.rSetting(maxCPS);
		this.rSetting(ticksExisted);
		this.rSetting(fov);
		this.rSetting(filter);
		this.rSetting(autoblock);
		this.rSetting(keepSprint);
		this.rSetting(noswing);
		this.rSetting(throughwalls);
		this.rSetting(ininv);
		this.rSetting(raycast);
		this.rSetting(autoDisable);
		this.rSetting(antiBot);
		this.rSetting(players);
		this.rSetting(hostiles);
		this.rSetting(passives);
		this.rSetting(invisibles);
		super.setup();
	}

	@Override
	public void onEnable() {
		delayTimer.reset();
		delay = (long) randomBetween(minCPS.getValue(), maxCPS.getValue());
		lastRotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
		super.onEnable();
	}

	@Override
	public void onDisable() {
		shouldFakeBlock = false;
		super.onDisable();
	}

	@EventTarget
	public void onWorldChange(EventWorldChange event) {
		if (autoDisable.getValue()) {
			ChatUtils.message("Toggled Killaura due to world change");
			this.toggle();
		}
	}

	@EventTarget
	public void onPre(EventPreMotion event) {

		List<EntityLivingBase> targets = filter(mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).map(EntityLivingBase.class::cast).collect(Collectors.toList()));

		Comparator<EntityLivingBase> comparator = null;
		switch (filter.getValue()) {
			case "Health":
				comparator = Comparator.comparingDouble(EntityLivingBase::getHealth);
				break;
			case "Distance":
				comparator = Comparator.comparingDouble(entity -> mc.thePlayer.getDistanceToEntity(entity));
				break;
			case "Angle":
				comparator = Comparator.comparingDouble(entity -> MathUtils.getAngleDifference(mc.thePlayer.rotationYaw, PlayerUtils.getRotations(entity)[0]));
				break;
			case "TicksExisted":
				comparator = Comparator.comparingInt(entity -> entity.ticksExisted);
				break;
		}

		targets.sort(comparator);

		if (!targets.isEmpty()) {
			EntityLivingBase target = targets.get(0);

			if (autoblock.getValue().equalsIgnoreCase("Fake")) {
				shouldFakeBlock = true;
			} else if (autoblock.getValue().equalsIgnoreCase("Vanilla")) {
				block();
			}

			if (raycast.getValue()) {
				final MovingObjectPosition objectMouseOver = RayCastUtils.getMouseOver(reach.getValueF());
				assert objectMouseOver != null;
				if (target != objectMouseOver.entityHit && objectMouseOver.entityHit instanceof EntityLivingBase) {
					target = (EntityLivingBase) objectMouseOver.entityHit;
				}
			}

			event.setYaw(PlayerUtils.getFixedRotation(PlayerUtils.getRotations(PlayerUtils.getCenter(target.getEntityBoundingBox())), lastRotations)[0]);
			event.setPitch(PlayerUtils.getFixedRotation(PlayerUtils.getRotations(PlayerUtils.getCenter(target.getEntityBoundingBox())), lastRotations)[1]);

			if (delayTimer.hasTimePassed(1000 / delay)) {

				if (noswing.getValue()) {
					PacketUtils.sendPacket(new C0APacketAnimation());
				} else {
					mc.thePlayer.swingItem();
				}

				mc.playerController.attackEntity(mc.thePlayer, target);
				delay = (long) randomBetween(minCPS.getValue(), maxCPS.getValue());
				delayTimer.reset();

			}
		} else {
			lastRotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
			shouldFakeBlock = false;
		}
	}

	public List<EntityLivingBase> filter(List<EntityLivingBase> targets) {
		targets = targets.stream().filter(entity -> entity != mc.thePlayer).collect(Collectors.toList());
		targets = targets.stream().filter(entity -> !entity.isDead && entity.getHealth() > 0).collect(Collectors.toList());
		targets = targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) <= reach.getValue()).collect(Collectors.toList());
		targets = targets.stream().filter(entity -> mc.thePlayer.canEntityBeSeen(entity) || throughwalls.getValue()).collect(Collectors.toList());
		targets = targets.stream().filter(entity -> !entity.isInvisible() || invisibles.getValue()).collect(Collectors.toList());
		targets = targets.stream().filter(entity -> entity.ticksExisted >= ticksExisted.getValue()).collect(Collectors.toList());
		targets = targets.stream().filter(entity -> isInFOV(entity, fov.getValue())).collect(Collectors.toList());
		targets = targets.stream().filter(entity -> mc.currentScreen == null || ininv.getValue()).collect(Collectors.toList());
		targets = targets.stream().filter(entity -> !(entity instanceof EntityPlayer) || players.getValue()).collect(Collectors.toList());
		targets = targets.stream().filter(entity -> !(entity instanceof EntityMob) || hostiles.getValue()).collect(Collectors.toList());
		targets = targets.stream().filter(entity -> !(entity instanceof EntityAnimal) || passives.getValue()).collect(Collectors.toList());
		targets = targets.stream().filter(entity -> !isBot(entity)).collect(Collectors.toList());
		return targets;
	}

	public double randomBetween(final double min, final double max) {
		return min + (rand.nextDouble() * (max - min));
	}

	private void block() {
		if (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) return;
		mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
		//PacketUtils.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
		blocking = true;
	}

	private void unblock() {
		if (!blocking) return;
		mc.playerController.onStoppedUsingItem(mc.thePlayer);
		//PacketUtils.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
		blocking = false;
	}

	private boolean isInFOV(Entity entity, double angle) {
		angle *= .5D;
		double angleDiff = MathUtils.getAngleDifference(mc.thePlayer.rotationYaw, PlayerUtils.getRotations(entity)[0]);
		return (angleDiff > 0 && angleDiff < angle) || (-angle < angleDiff && angleDiff < 0);
	}

	@EventTarget
	public void onSendPacket(@NotNull EventReceivePacket event) {
		if (event.getPacket() instanceof S0CPacketSpawnPlayer) {
			S0CPacketSpawnPlayer packet = (S0CPacketSpawnPlayer) event.getPacket();
			double posX = packet.getX() / 32D;
			double posY = packet.getY() / 32D;
			double posZ = packet.getZ() / 32D;

			double diffX = mc.thePlayer.posX - posX;
			double diffY = mc.thePlayer.posY - posY;
			double diffZ = mc.thePlayer.posZ - posZ;

			double dist = Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);

			if (dist <= 17D && posX != mc.thePlayer.posX && posY != mc.thePlayer.posY && posZ != mc.thePlayer.posZ) {
				botlist.add(mc.theWorld.getEntityByID(packet.getEntityID()));
			}
		}
	}

	private boolean isBot(Entity e) {
		if (!Menace.instance.moduleManager.killAuraModule.antiBot.getValue()) {
			return false;
		} else {
			return botlist.contains(e);
		}
	}
}