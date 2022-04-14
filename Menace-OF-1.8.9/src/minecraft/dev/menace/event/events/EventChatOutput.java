package dev.menace.event.events;

import dev.menace.event.Event;
import java.util.Objects;

public class EventChatOutput extends Event {
	private final String originalMessage;
	private String message;
	
	public EventChatOutput(String message)
	{
		this.message = Objects.requireNonNull(message);
		originalMessage = message;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public String getOriginalMessage()
	{
		return originalMessage;
	}
	
	public boolean isModified()
	{
		return !originalMessage.equals(message);
	}
}
