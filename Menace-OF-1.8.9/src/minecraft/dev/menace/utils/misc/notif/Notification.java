package dev.menace.utils.misc.notif;

import java.awt.Color;

import dev.menace.utils.misc.MSTimer;

public class Notification {

	public String message;
	public Long time;
	public Color color;
	public MSTimer t = new MSTimer();
	
	public Notification(String message, Long time) {
		this.time = time;
		this.message = message;
		this.color = Color.BLUE;
		t.reset();
	}
	
	public Notification(String message, Color color, Long time) {
		this.time = time;
		this.message = message;
		this.color = color;
		t.reset();
	}
	
}
