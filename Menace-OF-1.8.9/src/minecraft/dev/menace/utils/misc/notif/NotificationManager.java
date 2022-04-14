package dev.menace.utils.misc.notif;

import java.util.ArrayList;

public class NotificationManager {

	private static ArrayList<Notification> notifs = new ArrayList<Notification>();
	
	public void addNotification(Notification notif) {
		notifs.add(notif);
	}
	
	public void removeNotification(Notification notif) {
		notifs.remove(notif);
	}
	
	public static ArrayList<Notification> getNotifications() {
		return notifs;
	}
	
}
