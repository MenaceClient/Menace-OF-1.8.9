package dev.menace.event.events;

import dev.menace.event.Event;

public class EventAll extends Event {

    Event event;

    public EventAll(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

}
