package dev.menace.utils.notifications;

import java.util.ArrayList;

public class NotificationManager {

    private final ArrayList<Notification> notifications;

    public NotificationManager() {
        notifications = new ArrayList<>();
    }

    public void registerNotification(Notification notification) {
        notifications.add(notification);
    }

    public void unregisterNotification(Notification notification) {
        notifications.remove(notification);
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

}
