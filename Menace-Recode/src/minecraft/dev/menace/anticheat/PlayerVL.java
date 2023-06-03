package dev.menace.anticheat;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventUpdate;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerVL {

    private final EntityPlayer player;
    private final MSTimer vlTimer = new MSTimer();
    private int vl;
    //private boolean reported;

    //Check Helpers
    public long blockingTicks = 0;
    public float realFallDistance = 0;

    public PlayerVL(EntityPlayer player) {
        this.player = player;
        vlTimer.reset();
        vl = 0;
        //reported = false;
        Menace.instance.eventManager.register(this);
    }

    public void addVL(int vl, String check) {
        this.vl += vl;
        if (Menace.instance.moduleManager.hackerDetectModule.showVl.getValue()) {
            ChatUtils.anticheat("Flagged " + player.getName() + " for " + check + " [VL=" + this.vl + "]");
        }
    }

    @EventTarget
    public void handleVL(EventUpdate event) {
        if (vl >= 10) {
            ChatUtils.anticheat("Detected " + player.getName() + " cheating [VL=" + vl + "]");
            vl = 0;
            vlTimer.reset();
            //ChatUtils.anticheat("");
            //reported = true;
            //flag();
        } else if (vlTimer.hasTimePassed(10000)) {
            vl = 0;
            vlTimer.reset();
        }
    }

    public EntityPlayer getPlayer() {
        return player;
    }

}
