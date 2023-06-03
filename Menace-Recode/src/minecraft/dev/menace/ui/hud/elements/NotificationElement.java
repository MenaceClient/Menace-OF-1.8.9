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
        super(910, 530, true);
    }

    @Override
    public void render() {

        int y = this.getAbsoluteY();

        for (Notification notif : Menace.instance.notificationManager.getNotifications()) {
            if (notif.getTimer().hasTimePassed(notif.getTime())) {
                Menace.instance.notificationManager.unregisterNotification(notif);
                return;
            }

            int x = (int) (this.getAbsoluteX() + notif.getAnimation().getMax() - notif.getAnimation().getValue());

            RenderUtils.drawRect(x + 46, y, x + 49, y + 10, notif.getColor().getRGB());
            RenderUtils.drawRect(x - this.getStringWidth(notif.getContents()) + 44, y, x + 46, y + 10, Color.black.getRGB());
            this.drawString(notif.getContents(), x - this.getStringWidth(notif.getContents()) + 45, y, -1);

            notif.getAnimation().update();

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
