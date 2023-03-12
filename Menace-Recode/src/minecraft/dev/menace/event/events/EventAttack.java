package dev.menace.event.events;

import dev.menace.event.Event;
import net.minecraft.entity.Entity;

public class EventAttack extends Event {

    private Entity entity;
    private boolean preAttack;

    public EventAttack(Entity entity, boolean preAttack) {
        this.entity = entity;
        this.preAttack = preAttack;
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean isPreAttack() {
        return preAttack;
    }

}
