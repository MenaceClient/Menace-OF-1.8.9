package dev.menace.event.events;

import dev.menace.event.Event;
import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;
import net.minecraft.entity.Entity;

@JSMapping(109)
public class EventAttack extends Event {

    private Entity entity;
    private boolean preAttack;

    public EventAttack(Entity entity, boolean preAttack) {
        this.entity = entity;
        this.preAttack = preAttack;
    }

    @MappedName(48)
    public Entity getEntity() {
        return entity;
    }

    @MappedName(49)
    public boolean isPreAttack() {
        return preAttack;
    }

}
