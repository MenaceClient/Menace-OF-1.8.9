package dev.menace.utils.player;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.Priority;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventWorldChange;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

public class PacketBalanceUtils {

    public static PacketBalanceUtils instance = new PacketBalanceUtils();

    private int balance;
    private final MSTimer lastPacket = new MSTimer();


    public void start() {
        Menace.instance.eventManager.register(this);
    }

    public void stop() {
        Menace.instance.eventManager.unregister(this);
    }

    @EventTarget
    public void onWorldChange(EventWorldChange event) {
        reset();
    }

    public void handleNoEvent() {
        //Not needed but used for debugging
        this.balance += 50 - lastPacket.timePassed();
        lastPacket.reset();
    }

    @EventTarget(Priority.FIFTH)
    public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof C03PacketPlayer) {
            if (event.isCancelled()) {
                this.balance -= lastPacket.timePassed();
            } else {
                this.balance += 50 - lastPacket.timePassed();
            }
            lastPacket.reset();
        }
    }

    private void reset() {
        lastPacket.reset();
        this.balance = 0;
    }

    public int getBalance() {
        return this.balance;
    }

}
