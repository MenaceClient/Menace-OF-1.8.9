package dev.menace.event.events;

import dev.menace.event.Event;

public class EventMouse extends Event {

    private int button;

    public EventMouse(int button) {
        this.button = button;
    }

    public int getButton() {
        return button;
    }

}

