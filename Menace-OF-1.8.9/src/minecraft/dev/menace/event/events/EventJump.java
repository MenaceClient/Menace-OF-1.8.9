package dev.menace.event.events;

import dev.menace.event.Event;

public class EventJump extends Event {
    private double upwardsMotion;

    public EventJump(double upwardsMotion) {
        this.upwardsMotion = upwardsMotion;
    }

    public double getUpwardsMotion() {
        return upwardsMotion;
    }
    
    public void setUpwardsMotion(double upwardsMotion) {
    	this.upwardsMotion = upwardsMotion;
    }
}
