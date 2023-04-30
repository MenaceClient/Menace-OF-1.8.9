package dev.menace.event.events;

import dev.menace.event.Event;
import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;

@JSMapping(96)
public class EventKey extends Event {

	private final int key;
	
	public EventKey(int key) {
		this.key = key;
	}

	@MappedName(62)
	public int getKey() {
		return key;
	}
	
}
