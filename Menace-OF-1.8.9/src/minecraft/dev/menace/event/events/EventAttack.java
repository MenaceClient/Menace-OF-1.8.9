package dev.menace.event.events;

import dev.menace.event.Event;
import net.minecraft.entity.Entity;

public class EventAttack extends Event {
    private Entity targetEntity;

    public EventAttack(Entity targetEntity) {
        this.targetEntity = targetEntity;
    }

    public Entity getTargetEntity() {
        return targetEntity;
    }
}
