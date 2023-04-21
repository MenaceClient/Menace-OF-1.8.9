package dev.menace.ui.hud.elements;

import dev.menace.Menace;
import dev.menace.ui.hud.BaseElement;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.notifications.Notification;
import dev.menace.utils.render.ColorUtils;
import dev.menace.utils.render.RenderUtils;

import java.awt.*;

public class NotificationElement extends BaseElement {
    public NotificationElement() {
        super(0.5, 0.5, true);
    }

    @Override
    public void render() {

        int y = this.getAbsoluteY();

        for (Notification notif : Menace.instance.notificationManager.getNotifications()) {
            if (notif.getTimer().hasTimePassed(notif.getTime())) {
                Menace.instance.notificationManager.unregisterNotification(notif);
                return;
            }

            RenderUtils.drawRect(this.getAbsoluteX() + 46, y, this.getAbsoluteX() + 49, y + 10, notif.getColor().getRGB());
            RenderUtils.drawRect(this.getAbsoluteX() - this.getStringWidth(notif.getContents()) + 44, y, this.getAbsoluteX() + 46, y + 10, Color.black.getRGB());
            this.drawString(notif.getContents(), this.getAbsoluteX() - this.getStringWidth(notif.getContents()) + 45, y, -1);

            y-=11;
        }
    }

    @Override
    public void renderDummy() {

    }

    @Override
    public int getWidth() {
        return 50;
    }

    @Override
    public int getHeight() {
        return 10;
    }
}
