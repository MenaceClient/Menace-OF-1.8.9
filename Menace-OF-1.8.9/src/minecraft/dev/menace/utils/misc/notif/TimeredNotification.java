package dev.menace.utils.misc.notif;

import dev.menace.utils.misc.MSTimer;

public class TimeredNotification extends Notification {

	public MSTimer timer;
	
	public TimeredNotification(String message, MSTimer timer, Long time) {
		super(message, time);
		this.timer = timer;
	}

}
