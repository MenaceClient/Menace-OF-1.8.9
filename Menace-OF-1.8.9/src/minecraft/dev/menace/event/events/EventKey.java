package dev.menace.event.events;

import dev.menace.event.Event;
import net.minecraft.client.settings.KeyBinding;

public class EventKey extends Event {
    private int key;

    public EventKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
