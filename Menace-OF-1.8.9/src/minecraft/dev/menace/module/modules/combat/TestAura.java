package dev.menace.module.modules.combat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventPostMotionUpdate;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.modules.render.Freecam;
import dev.menace.module.settings.BoolSetting;
import dev.menace.module.settings.DoubleSetting;
import dev.menace.module.settings.StringSetting;
import dev.menace.utils.entity.self.PlayerUtils;
import dev.menace.utils.entity.self.Rotations;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.MSTimer;
import dev.menace.utils.misc.MathUtils;
import dev.menace.utils.misc.RayCastUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.BlockPos;

public class TestAura extends Module {

	MSTimer switchTimer = new MSTimer();
	MSTimer timer = new MSTimer();
	private float[] lastRotations = new float[2];
	private boolean rotated = false;
	public Entity target;
	boolean rng;

	//Settings
	DoubleSetting cps;
	DoubleSetting reach;
	DoubleSetting fov;
	StringSetting blockmode;
	BoolSetting throughwalls;
	BoolSetting openInv;
	BoolSetting lock;
	BoolSetting players;
	BoolSetting animals;
	BoolSetting monsters;
	BoolSetting invisibles;
	BoolSetting teams;

	public TestAura() {
		super("TestAura", 0, Category.COMBAT);
	}

	@Override
	public void setup() {
		ArrayList<String> modes = new ArrayList<String>();
		modes.add("Vanilla");
		modes.add("Interact");
		modes.add("Unblock");
		modes.add("Verus");
		modes.add("NCP");
		modes.add("Fake");
		modes.add("None");
		cps = new DoubleSetting("CPS", this, 14, 2, 20);
		reach = new DoubleSetting("Reach", this, 3.5, 2, 7);
		fov = new DoubleSetting("FOV", this, 360, 15, 360);
		blockmode = new StringSetting("BlockMode", this, "Vanilla", modes);
		throughwalls = new BoolSetting("ThroughWalls", this, false);
		openInv = new BoolSetting("OpenInv", this, false);
		lock = new BoolSetting("Lock", this, true);
		players = new BoolSetting("Players", this, true);
		animals = new BoolSetting("Animals", this, false);
		monsters = new BoolSetting("Monsters", this, false);
		invisibles = new BoolSetting("Invisibles", this, false);
		teams = new BoolSetting("Teams", this, false);
		this.rSetting(cps);
		this.rSetting(reach);
		this.rSetting(fov);
		this.rSetting(blockmode);
		this.rSetting(throughwalls);
		this.rSetting(openInv);
		this.rSetting(lock);
		this.rSetting(players);
		this.rSetting(animals);
		this.rSetting(monsters);
		this.rSetting(invisibles);
		this.rSetting(teams);
	}

	@Override
	public void onEnable() {
		if (MC.theWorld == null) {
			this.toggle();
			return;
		}
		this.switchTimer.reset();
		this.timer.reset();
		this.rotated = false;
		this.target = null;
		this.rng = true;
		super.onEnable();
	}

	@Override
	public void onDisable() {
		this.target = null;
		KillAura.blocking = false;
		super.onDisable();
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (!blockmode.getString().equalsIgnoreCase("None") && this.target != null) {
			KillAura.blocking = true;
		} else {
			KillAura.blocking = false;
		}
	}

	public boolean isValid(final Entity e) {
		if (e instanceof EntityLivingBase &&
				/*!FriendManager.isFriend(e.getName()) &&*/
				(this.invisibles.isChecked() || !(e.isInvisible()))
				&& (this.players.isChecked() || !(e instanceof EntityPlayer))
				&& (this.animals.isChecked() || !(e instanceof EntityAnimal))
				&& (this.monsters.isChecked() || !(e instanceof EntityMob))
				&& e != MC.thePlayer && !(e instanceof EntityVillager)
				&& MC.thePlayer.getDistanceToEntity(e) <= this.reach.getValue()
				&& !e.getName().contains("#")  && (!this.teams.isChecked() ||
						!e.getDisplayName().getFormattedText().startsWith("§" + MC.thePlayer.getDisplayName().getFormattedText().charAt(1)))
				&& !e.getName().toLowerCase().contains("shop") && !e.getName().toLowerCase().contains("upgrades") 
				&& (this.throughwalls.isChecked() || MC.thePlayer.canEntityBeSeen(e)) && (this.openInv.isChecked() 
						|| !(MC.currentScreen instanceof Gui) && !e.isDead)) {
			for (String id : AntiBot.bots) {
				if (String.valueOf(e.getEntityId()).equalsIgnoreCase(id)) {
					return false;
				}
			}

			return PlayerUtils.isInFOV((EntityLivingBase) e, this.fov.getValue());
		} else {
			return false;
		}
	}

	private List<Entity> getEntities() {
		final List<Entity> entityList = new ArrayList<Entity>();
		final Iterator iterator = (Iterator)this.MC.theWorld.loadedEntityList.iterator();
		while ((iterator).hasNext()) {
			final Object e = ((Iterator<Object>)iterator).next();
			if (e instanceof Entity && this.isValid((Entity)e)) {
				entityList.add((Entity)e);
			}
		}
		entityList.sort((entity1, entity2) -> Float.compare(entity1.getDistanceToEntity(this.MC.thePlayer), entity2.getDistanceToEntity(this.MC.thePlayer)));
		return entityList;
	}

	private void switchTargets() {
		final List<Entity> entities = this.getEntities();
		entities.sort((entity1, entity2) -> Float.compare(entity1.getDistanceToEntity(this.MC.thePlayer), entity2.getDistanceToEntity(this.MC.thePlayer)));
		if (entities == null || entities.isEmpty()) {
			return;
		}
		if (this.isValid(entities.get(0))) {
			this.target = entities.get(0);
		}
		this.switchTimer.reset();
	}

	private void attack() {
		if (this.MC.thePlayer.isBlocking() && this.blockmode.getString().equalsIgnoreCase("Unblock")) {
			MC.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
		} else if (this.MC.thePlayer.isBlocking() && this.blockmode.getString().equalsIgnoreCase("Verus") && MC.thePlayer.ticksExisted % 60 == 0) {
			MC.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
		} else if (this.blockmode.getString().equalsIgnoreCase("NCP")) {
			MC.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f));
		}
		this.MC.thePlayer.swingItem();
        MC.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
		MC.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.CRIT);
		MC.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.CRIT_MAGIC);
	}
	
	@EventTarget
	public void onPreMotion(EventPreMotionUpdate event) {
		if (this.getEntities() == null || this.getEntities().isEmpty()) {
			return;
		}
		this.target = this.getEntities().get(0);
		if (this.target == null) {
			return;
		}
		if (!this.isValid(this.target)) {
			return;
		}
		final float[] rots = Rotations.getRotations(Rotations.getCenter(target.getEntityBoundingBox()));
		final float[] rotations = new float[2];
		rotations[0] = rots[0] + MathUtils.getRandom(-15, 15);
		rotations[1] = rots[1] + MathUtils.getRandom(1, 30);
		event.setYaw(rotations[0]);
		event.setPitch(rotations[1]);
		MC.thePlayer.rotationYawHead = rotations[0];
		MC.thePlayer.renderYawOffset = rotations[0];
        if (Menace.instance.moduleManager.targetStrafeModule.isToggled() && Menace.instance.moduleManager.targetStrafeModule.rotate.isChecked()) {
        	MC.thePlayer.rotationYaw = rots[0];	
        }
	}

	@EventTarget
	public void onPostMotion(EventPostMotionUpdate event) {
		
		if (this.target != null &&  this.target.isDead || this.target != null && this.target.getDistanceToEntity(MC.thePlayer) > reach.getValue()) {
			this.target = null;
		}
		
		if (this.getEntities() == null || this.getEntities().isEmpty()) {
			return;
		}
		this.target = this.getEntities().get(0);
		if (this.target == null) {
			return;
		}

		if (!this.isValid(this.target)) {
			this.target = null;
			this.switchTargets();
			return;
		}
		if (this.lock.isChecked()) {
			final float[] rotations = Rotations.getRotations(Rotations.getCenter(target.getEntityBoundingBox()));
			this.MC.thePlayer.rotationYaw = rotations[0];
			this.MC.thePlayer.rotationPitch = rotations[1];
		}
		if (this.MC.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
			if (this.blockmode.getString().equalsIgnoreCase("Vanilla") || this.blockmode.getString().equalsIgnoreCase("Unblock")) {
				this.MC.playerController.sendUseItem(this.MC.thePlayer, this.MC.theWorld, this.MC.thePlayer.getCurrentEquippedItem());  
			} else if (this.blockmode.getString().equalsIgnoreCase("Interact") || this.blockmode.getString().equalsIgnoreCase("Verus")) {
				MC.thePlayer.setItemInUse(MC.thePlayer.getHeldItem(), (int) (Math.random() * 100)); 
			}
		}
		if (!this.timer.hasTimePassed(1000L / (long)this.cps.getValue())) {
			return;
		}
		this.attack();
		this.timer.reset();
	}

}
