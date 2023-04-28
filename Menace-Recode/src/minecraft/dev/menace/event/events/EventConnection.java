package dev.menace.event.events;

import dev.menace.event.Event;

public class EventConnection extends Event {

    private final State state;

    public EventConnection(State state) {
        this.state = state;

    }

    public State getState() {
        return state;
    }

    public enum State {
        CONNECTING, DISCONNECTING
    }

}
