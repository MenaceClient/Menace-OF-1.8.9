package dev.menace.event.events;

import dev.menace.event.Event;
import dev.menace.scripting.js.JSMapping;
import dev.menace.scripting.js.MappedName;

@JSMapping(92)
public class EventChatOutput extends Event {
	
	private String message;
	
	public EventChatOutput(String message) {
		this.message = message;
	}

	@MappedName(50)
	public String getMessage() {
		return message;
	}

	@MappedName(51)
	public void setMessage(String message) {
		this.message = message;
	}
	
}
