package dev.menace.module.modules.combat;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventPostMotion;
import dev.menace.event.events.EventPreMotion;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
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
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.MovingObjectPosition;

import java.util.Comparator;
import java.util.List;

public class KillAuraModule extends BaseModule {

	MSTimer delayTimer = new MSTimer();
	MSTimer switchTimer = new MSTimer();
	public EntityLivingBase target;
	long nextDelay;
	boolean blocking;
	public boolean shouldFakeBlock;
	float[] lastRotations = new float[2];
	
	SliderSetting reach;
	public SliderSetting minCPS;
	public SliderSetting maxCPS;
	SliderSetting ticksExisted;
	SliderSetting fov;
	public ListSetting mode;
	SliderSetting switchDelay;
	ListSetting attackevent;
	ListSetting filter;
	ListSetting rotation;
	ListSetting autoblock;
	ToggleSetting noswing;
	ToggleSetting throughwalls;
	ToggleSetting ininv;
	ToggleSetting raycast;
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
		mode = new ListSetting("Mode", true, "Single", new String[] {"Single", "Switch", "Multi"});
		switchDelay = new SliderSetting("Switch Delay", true, 100, 10, 1000, 10, true) {
			@Override
			public void constantCheck() {
				this.setVisible(Menace.instance.moduleManager.killAuraModule.mode.getValue().equalsIgnoreCase("Switch"));
			}
		};
		attackevent = new ListSetting("Attack Event", true, "Pre", new String[] {"Pre", "Post"});
		filter = new ListSetting("Filter", true, "Health", new String[] {"Health", "Distance", "Angle", "TicksExisted"});
		rotation = new ListSetting("Rotation", true, "None", new String[] {"None", "Basic", "Bypass", "Jitter"});
		autoblock = new ListSetting("AutoBlock", true, "None", new String[] {"None", "Fake", "Vanilla"});
		noswing = new ToggleSetting("NoSwing", true, false);
		throughwalls = new ToggleSetting("ThroughWalls", true, false);
		ininv = new ToggleSetting("InInventory", true, false);
		raycast = new ToggleSetting("RayCast", true, false);
		players = new ToggleSetting("Players", true, true);
		hostiles = new ToggleSetting("Hostiles", true, true);
		passives = new ToggleSetting("Passives", true, false);
		invisibles = new ToggleSetting("Invisibles", true, false);
		this.rSetting(reach);
		this.rSetting(minCPS);
		this.rSetting(maxCPS);
		this.rSetting(ticksExisted);
		this.rSetting(fov);
		this.rSetting(mode);
		this.rSetting(switchDelay);
		this.rSetting(attackevent);
		this.rSetting(filter);
		this.rSetting(rotation);
		this.rSetting(autoblock);
		this.rSetting(noswing);
		this.rSetting(throughwalls);
		this.rSetting(ininv);
		this.rSetting(raycast);
		this.rSetting(players);
		this.rSetting(hostiles);
		this.rSetting(passives);
		this.rSetting(invisibles);
		super.setup();
	}
	
	@Override
	public void onEnable() {
		delayTimer.reset();
		switchTimer.reset();
		target = null;
		nextDelay = MathUtils.randLong(minCPS.getValueL(), maxCPS.getValueL());
		blocking = false;
		shouldFakeBlock = false;
		lastRotations[0] = MC.thePlayer.rotationYaw;
		lastRotations[1] = MC.thePlayer.rotationPitch;
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		target = null;
		unblock();
		super.onDisable();
	}
	
	@EventTarget
	public void onPreMotion(EventPreMotion event) {
		getTarget();
		if (rotation.getValue().equalsIgnoreCase("Basic") && target != null) {
			event.setYaw(PlayerUtils.getRotations(target)[0]);
			event.setPitch(PlayerUtils.getRotations(target)[1]);
		} else if (rotation.getValue().equalsIgnoreCase("Jitter") && target != null) {
			float minYaw = PlayerUtils.getRotations(target.boundingBox.minX, target.posY, target.boundingBox.minZ)[0];
			float maxYaw = PlayerUtils.getRotations(target.boundingBox.maxX, target.posY, target.boundingBox.maxZ)[0];
			float yaw = MathUtils.randFloat(minYaw, maxYaw);
			float minPitch = PlayerUtils.getRotations(target.posX, target.boundingBox.minY, target.posZ)[1];
			float maxPitch = PlayerUtils.getRotations(target.posX, target.boundingBox.maxY, target.posZ)[1];
			float pitch = MathUtils.randFloat(minPitch, maxPitch);
			event.setYaw(yaw);
			event.setPitch(pitch);
		} else if (rotation.getValue().equalsIgnoreCase("Bypass") && target != null) {
			event.setYaw(PlayerUtils.getFixedRotation(PlayerUtils.getRotations(PlayerUtils.getCenter(target.getEntityBoundingBox())), lastRotations)[0]);
			event.setPitch(PlayerUtils.getFixedRotation(PlayerUtils.getRotations(PlayerUtils.getCenter(target.getEntityBoundingBox())), lastRotations)[1]);
			lastRotations[0] = event.getYaw();
			lastRotations[1] = event.getPitch();
		}

		if (target != null) {
			block();
		} else {
			unblock();
		}

		if (attackevent.getValue().equalsIgnoreCase("Pre")) {
			attack();
		}
	}
	
	@EventTarget
	public void onPostMotion(EventPostMotion event) {
		if (attackevent.getValue().equalsIgnoreCase("Post")) {
			attack();
		}
	}
	
	public void getTarget() {
		if ((mode.getValue().equalsIgnoreCase("Single") && isValid(target))
		|| (mode.getValue().equalsIgnoreCase("Switch") &&
				!switchTimer.hasTimePassed(switchDelay.getValueL()) && isValid(target))) return;
		Comparator<Entity> entityFilter = null;
		switch (filter.getValue()) {

		case "Health" :
			entityFilter = Comparator.comparingInt(e -> (int) ((EntityLivingBase) e).getHealth());
			break;

		case "Distance" :
			entityFilter = (e1, e2) -> (int)MC.thePlayer.getDistanceToEntity(e1) - (int)MC.thePlayer.getDistanceToEntity(e2);
			break;
			
		case "Angle" : 
			entityFilter = (e1, e2) -> (int)MathUtils.getAngleDifference(MC.thePlayer.rotationYaw, PlayerUtils.getRotations(e1)[0]) - (int)MathUtils.getAngleDifference(MC.thePlayer.rotationYaw, PlayerUtils.getRotations(e2)[0]);
			break;
		
		case "TicksExisted" : 
			entityFilter = Comparator.comparingInt(e -> e.ticksExisted);
			break;
		}

		assert entityFilter != null;
		if (MC.theWorld.loadedEntityList.stream()
				.filter(this::isValid).sorted(entityFilter).toArray().length > 0) {
			target = (EntityLivingBase) MC.theWorld.loadedEntityList.stream()
					.filter(this::isValid).sorted(entityFilter).toArray()[0];
		} else {
			target = null;
		}
	}
	
	public void attack() {
		if (!delayTimer.hasTimePassed(1000 / nextDelay) || target == null || Menace.instance.moduleManager.scaffoldModule.isToggled()) {
			return;
		}
		
		if (raycast.getValue()) {
            final MovingObjectPosition objectMouseOver = RayCastUtils.getMouseOver(MC.thePlayer.prevRotationYaw, MC.thePlayer.prevRotationPitch, reach.getValueF());
			assert objectMouseOver != null;
			if (target != objectMouseOver.entityHit && objectMouseOver.entityHit instanceof EntityLivingBase) {
                target = (EntityLivingBase) objectMouseOver.entityHit;
            }
        }
		
		if (noswing.getValue()) {
			PacketUtils.addToSendQueue(new C0APacketAnimation());
		} else {
			MC.thePlayer.swingItem();
		}

		if (mode.getValue().equalsIgnoreCase("Multi")) {
			MC.theWorld.loadedEntityList.stream()
					.filter(this::isValid).forEach(e -> MC.playerController.attackEntity(MC.thePlayer, e));
		} else {
			MC.playerController.attackEntity(MC.thePlayer, target);
		}
		
		delayTimer.reset();
		nextDelay = MathUtils.randInt(minCPS.getValueI(), maxCPS.getValueI());
	}
	
	private boolean isValid(Entity e) {
		return e instanceof EntityLivingBase
				&& e != MC.thePlayer
				&& e != Menace.instance.moduleManager.blinkModule.fp
				&& MC.thePlayer.getDistanceToEntity(e) <= reach.getValue()
				&& isInFOV(e, fov.getValue())
				&& !e.isDead
				&& e.ticksExisted >= ticksExisted.getValueI()
				&& ((EntityLivingBase) e).getHealth() > 0
				&& (!(e instanceof EntityPlayer) || players.getValue())
				&& (!(e instanceof EntityMob) || hostiles.getValue())
				&& (!(e instanceof EntityAnimal) || passives.getValue())
				&& (!e.isInvisible() || invisibles.getValue())
				&& (MC.thePlayer.canEntityBeSeen(e) || throughwalls.getValue())
				&& (ininv.getValue() || MC.currentScreen == null);
	}

	private void block() {
		if (autoblock.getValue().equalsIgnoreCase("Fake")) shouldFakeBlock = true;
		if (MC.thePlayer.getHeldItem() == null
				|| !(MC.thePlayer.getHeldItem().getItem() instanceof ItemSword)
				|| !autoblock.getValue().equalsIgnoreCase("Vanilla")) return;

		MC.playerController.sendUseItem(MC.thePlayer, MC.theWorld, MC.thePlayer.getHeldItem());
		blocking = true;
	}

	private void unblock() {
		shouldFakeBlock = false;
		if (!blocking) return;
		MC.playerController.onStoppedUsingItem(MC.thePlayer);
		blocking = false;
	}
	
	private boolean isInFOV(Entity entity, double angle) {
        angle *= .5D;
        double angleDiff = MathUtils.getAngleDifference(MC.thePlayer.rotationYaw, PlayerUtils.getRotations(entity)[0]);
        return (angleDiff > 0 && angleDiff < angle) || (-angle < angleDiff && angleDiff < 0);
    }
}
