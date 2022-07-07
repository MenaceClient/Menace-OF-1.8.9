package dev.menace.event.events;

import dev.menace.event.Event;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

public class EventDeath extends Event {

	private EntityLivingBase entity;
	private EntityPlayer attacker;
	
	public EventDeath(EntityPlayer attacker, EntityLivingBase entity) {
		this.entity = entity;
		this.attacker = attacker;
	}

	public EntityLivingBase getEntity() {
		return entity;
	}

	public EntityPlayer getAttacker() {
		return attacker;
	}
	
}
