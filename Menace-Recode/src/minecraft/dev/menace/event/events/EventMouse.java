package dev.menace.event.events;

import dev.menace.event.Event;
import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;

@JSMapping(98)
public class EventMouse extends Event {

    private int button;

    public EventMouse(int button) {
        this.button = button;
    }

    @MappedName(63)
    public int getButton() {
        return button;
    }

}

