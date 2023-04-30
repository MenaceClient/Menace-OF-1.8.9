package dev.menace.event.events;

import dev.menace.event.Event;
import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

@JSMapping(94)
public class EventDeath extends Event {

	private EntityLivingBase entity;
	private EntityPlayer attacker;
	
	public EventDeath(EntityPlayer attacker, EntityLivingBase entity) {
		this.entity = entity;
		this.attacker = attacker;
	}

	@MappedName(48)
	public EntityLivingBase getEntity() {
		return entity;
	}

	@MappedName(59)
	public EntityPlayer getAttacker() {
		return attacker;
	}
	
}
