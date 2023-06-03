package dev.menace.event.events;

import dev.menace.event.Event;
import net.minecraft.entity.Entity;

public class EventSpawnEntity extends Event {

    Entity entity;

    public EventSpawnEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

}
