package dev.menace.event.events;

import dev.menace.event.Event;
import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;

@JSMapping(110)
public class EventConnection extends Event {

    private final State state;

    public EventConnection(State state) {
        this.state = state;

    }

    @MappedName(58)
    public State getState() {
        return state;
    }

    public enum State {
        CONNECTING, DISCONNECTING
    }

}
