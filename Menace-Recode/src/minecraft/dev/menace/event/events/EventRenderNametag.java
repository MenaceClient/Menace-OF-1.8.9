package dev.menace.event.events;

import dev.menace.event.Event;
import net.minecraft.entity.Entity;

public class EventRenderNametag extends Event {

    Entity entity;
    double x;
    double y;
    double z;

    public EventRenderNametag(Entity entity, double x, double y, double z) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Entity getEntity() {
        return entity;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

}
