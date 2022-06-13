package dev.menace.event.events;

import dev.menace.event.Event;

public class EventChatOutput extends Event {
	
	private String message;
	
	public EventChatOutput(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
}
