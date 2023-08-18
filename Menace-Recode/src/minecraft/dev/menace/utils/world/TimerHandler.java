package dev.menace.utils.world;

import net.minecraft.client.Minecraft;

public class TimerHandler {

    private static int currentPriority = 0;

    public static void setTimer(float speed, int priority) {
        if (priority >= currentPriority) {
            Minecraft.getMinecraft().timer.timerSpeed = speed;
            currentPriority = priority;
        }
    }

    public static void resetTimer() {
        Minecraft.getMinecraft().timer.timerSpeed = 1;
        currentPriority = 0;
    }

}
