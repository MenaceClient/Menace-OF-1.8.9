package dev.menace.event.events;

import dev.menace.event.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

public class EventDeath extends Event {

	private EntityPlayer entity;
	private DamageSource damageSource;
	
	public EventDeath(EntityPlayer entity, DamageSource damageSource) {
		this.entity = entity;
		this.damageSource = damageSource;
	}

	public EntityPlayer getEntity() {
		return entity;
	}

	public DamageSource getDamageSource() {
		return damageSource;
	}
	
}
