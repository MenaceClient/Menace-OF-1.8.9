package dev.menace.gui.hud.element.elements;

import java.awt.Color;
import java.text.DecimalFormat;

import dev.menace.gui.hud.ScreenPosition;
import dev.menace.gui.hud.element.ElementDraggable;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.ColorUtils;
import dev.menace.utils.misc.notif.Notification;
import dev.menace.utils.misc.notif.TimeredNotification;
import dev.menace.utils.render.RenderUtils;

public class Notifications extends ElementDraggable {

	DecimalFormat decimalFormat = new DecimalFormat("0.0");
	
	@Override
	public int getWidth() {
		return 50;
	}

	@Override
	public int getHeight() {
		return 10;
	}

	@Override
	public void render(ScreenPosition pos) {
		if (Menace.instance.notificationManager.getNotifications().isEmpty()) return; 
			
		java.util.ArrayList<Notification> list = Menace.instance.notificationManager.getNotifications();
		
		int y = pos.getAbsoluteY();
		for (Notification n : list) { 
			
			if (n.t.hasTimePassed(n.time)) {
				Menace.instance.notificationManager.removeNotification(n);
				return;
			}
			
			String message = n.message;
			
			if (n instanceof TimeredNotification) {
				TimeredNotification notif = (TimeredNotification) n;
				 message = n.message.replace("$TIMER$", decimalFormat.format(notif.timer.hasTimeLeft(n.time)));
			}
			
			if (Menace.instance.moduleManager.hudModule.notifAlign.getString().equalsIgnoreCase("Left")) {
				RenderUtils.drawRect(pos.getAbsoluteX() - 3, y, pos.getAbsoluteX(), y + 10, n.color);
				RenderUtils.drawRect(pos.getAbsoluteX(), y, pos.getAbsoluteX() + font.getStringWidth(message) + 2, y + 10, ColorUtils.reAlpha(Color.BLACK, 150));
				font.drawString(message, pos.getAbsoluteX() + 1, y + 1, -1);
			} else {
				RenderUtils.drawRect(pos.getAbsoluteX() - font.getStringWidth(message) + 49 - 3, y, pos.getAbsoluteX() - font.getStringWidth(message) + 49, y + 10, n.color);
				RenderUtils.drawRect(pos.getAbsoluteX() - font.getStringWidth(message) + 49, y, pos.getAbsoluteX()+ 50, y + 10, ColorUtils.reAlpha(Color.BLACK, 150));
				font.drawString(message, pos.getAbsoluteX() + 1 - font.getStringWidth(message) + 49, y + 1, -1);
			}
			
			y -= 11;
			
		}
		
	}

}
