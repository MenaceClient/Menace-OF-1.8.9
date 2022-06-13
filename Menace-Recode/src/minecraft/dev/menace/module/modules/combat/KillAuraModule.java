package dev.menace.module.modules.combat;

import java.util.Comparator;

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
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;

public class KillAuraModule extends BaseModule {

	MSTimer delayTimer = new MSTimer();
	EntityLivingBase target;
	long nextDelay;
	
	SliderSetting reach;
	public SliderSetting minDelay;
    public SliderSetting maxDelay;
	SliderSetting fov;
	ListSetting mode;
	ListSetting attackevent;
	ListSetting filter;
	ListSetting rotation;
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
		reach = new SliderSetting("Reach", true, 3, 1, 7, false);
		minDelay = new SliderSetting("Delay Min", true, 90, 100, 1000, 100, true) {
            @Override
            public void constantCheck() {
                if (Menace.instance.moduleManager.killAuraModule.maxDelay.getValue() < this.getValue()) {
                    this.setValue(Menace.instance.moduleManager.killAuraModule.maxDelay.getValue());
                }
            }
        };
        maxDelay = new SliderSetting("Delay Max", true, 100, 100, 1000, 100, true) {
            @Override
            public void constantCheck() {
                if (Menace.instance.moduleManager.killAuraModule.minDelay.getValue() > this.getValue()) {
                    this.setValue(Menace.instance.moduleManager.killAuraModule.minDelay.getValue());
                }
            }
        };
		fov = new SliderSetting("FOV", true, 360, 0, 360, false);
		mode = new ListSetting("Mode", true, "Single", new String[] {"Single", "Multi"});
		attackevent = new ListSetting("Attack Event", true, "Pre", new String[] {"Pre", "Post"});
		filter = new ListSetting("Filter", true, "Health", new String[] {"Health", "Distance", "Angle", "TicksExsted"});
		rotation = new ListSetting("Rotation", true, "None", new String[] {"None", "Basic"});
		noswing = new ToggleSetting("NoSwing", true, false);
		throughwalls = new ToggleSetting("ThroughWalls", true, false);
		ininv = new ToggleSetting("InInventory", true, false);
		raycast = new ToggleSetting("RayCast", true, false);
		players = new ToggleSetting("Players", true, true);
		hostiles = new ToggleSetting("Hostiles", true, true);
		passives = new ToggleSetting("Passives", true, false);
		invisibles = new ToggleSetting("Invisibles", true, false);
		this.rSetting(reach);
		this.rSetting(minDelay);
        this.rSetting(maxDelay);
		this.rSetting(fov);
		this.rSetting(mode);
		this.rSetting(attackevent);
		this.rSetting(filter);
		this.rSetting(rotation);
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
		target = null;
		nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		target = null;
		super.onDisable();
	}
	
	@EventTarget
	public void onPreMotion(EventPreMotion event) {
		getTarget();
		if (rotation.getValue().equalsIgnoreCase("Basic") && target != null) {
			event.setYaw(PlayerUtils.getRotations(target)[0]);
			event.setPitch(PlayerUtils.getRotations(target)[1]);
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
		Comparator<Entity> entityFilter = null;
		switch (filter.getValue()) {
		
		case "Health" :
			entityFilter = (e1, e2) -> (int)((EntityLivingBase)e1).getHealth() - (int)((EntityLivingBase)e2).getHealth();
			break;
			
		case "Distance" :
			entityFilter = (e1, e2) -> (int)MC.thePlayer.getDistanceToEntity(e1) - (int)MC.thePlayer.getDistanceToEntity(e2);
			break;
			
		case "Angle" : 
			entityFilter = (e1, e2) -> (int)MathUtils.getAngleDifference(MC.thePlayer.rotationYaw, PlayerUtils.getRotations(e1)[0]) - (int)MathUtils.getAngleDifference(MC.thePlayer.rotationYaw, PlayerUtils.getRotations(e2)[0]);
			break;
		
		case "TicksExisted" : 
			entityFilter = (e1, e2) -> ((EntityLivingBase)e1).ticksExisted - ((EntityLivingBase)e2).ticksExisted;
			break;
			
		default :
			entityFilter = (e1, e2) -> (int)((EntityLivingBase)e1).getHealth() - (int)((EntityLivingBase)e2).getHealth();
			break;
			
		}

		if (MC.theWorld.loadedEntityList.stream()
				.filter(e -> isValid(e)).sorted(entityFilter).toArray().length > 0) {
			target = (EntityLivingBase) MC.theWorld.loadedEntityList.stream()
					.filter(e -> isValid(e)).sorted(entityFilter).toArray()[0];
		} else {
			target = null;
		}
	}
	
	public void attack() {		
		if (!delayTimer.hasTimePassed(nextDelay) || target == null) {
			return;
		}
		
		if (raycast.isToggled()) {
            final MovingObjectPosition objectMouseOver = RayCastUtils.getMouseOver(MC.thePlayer.prevRotationYaw, MC.thePlayer.prevRotationPitch, reach.getValueF());
            if (objectMouseOver.entityHit != null && target != objectMouseOver.entityHit && objectMouseOver.entityHit instanceof EntityLivingBase) {
                target = (EntityLivingBase) objectMouseOver.entityHit;
            }
        }
		
		if (noswing.isToggled()) {
			PacketUtils.addToSendQueue(new C0APacketAnimation());
		} else {
			MC.thePlayer.swingItem();
		}
		
		switch (mode.getValue()) {
		
		case "Single" :
			MC.playerController.attackEntity(MC.thePlayer, target);
			break;
			
		case "Multi" :
			MC.theWorld.loadedEntityList.stream()
			.filter(e -> isValid(e)).forEach(e -> {
				MC.playerController.attackEntity(MC.thePlayer, e);
			});
			break;
		
		default :
			break;
		}
		
		delayTimer.reset();	
		nextDelay = MathUtils.randLong(minDelay.getValueL(), maxDelay.getValueL());
	}
	
	private boolean isValid(Entity e) {
		if (e instanceof EntityLivingBase 
				&& e != MC.thePlayer 
				&& e != Menace.instance.moduleManager.blinkModule.fp
				&& MC.thePlayer.getDistanceToEntity(e) <= reach.getValue()
				&& isInFOV(e, fov.getValue())
				&& !e.isDead
				&& ((EntityLivingBase)e).getHealth() > 0
				&& (!(e instanceof EntityPlayer) || players.isToggled())
				&& (!(e instanceof EntityMob) || hostiles.isToggled())
				&& (!(e instanceof EntityAnimal) || passives.isToggled())
				&& (!((EntityLivingBase)e).isInvisible() || invisibles.isToggled())
				&& (MC.thePlayer.canEntityBeSeen(e) || throughwalls.isToggled())
				&& (ininv.isToggled() || !(MC.currentScreen instanceof Gui))) {
			return true;
		}
		return false;
	}
	
	private boolean isInFOV(Entity entity, double angle) {
        angle *= .5D;
        double angleDiff = MathUtils.getAngleDifference(MC.thePlayer.rotationYaw, PlayerUtils.getRotations(entity)[0]);
        return (angleDiff > 0 && angleDiff < angle) || (-angle < angleDiff && angleDiff < 0);
    }
}
