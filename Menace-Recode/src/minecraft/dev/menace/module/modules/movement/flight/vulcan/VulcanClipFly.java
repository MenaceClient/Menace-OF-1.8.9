package dev.menace.module.modules.movement.flight.vulcan;

import dev.menace.Menace;
import dev.menace.event.events.EventPreMotion;
import dev.menace.module.modules.movement.flight.FlightBase;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.timer.MSTimer;

public class VulcanClipFly extends FlightBase {

    int step;
    MSTimer flyTimer = new MSTimer();

    @Override
    public void onEnable() {
        step = 0;
        super.onEnable();
    }

    //Thx Geuxy <3
    @Override
    public void onPreMotion(EventPreMotion event) {
        switch (step) {
            case 0:
                //event.setPosY(event.getPosY() - 1.1);
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.1, mc.thePlayer.posZ);
                step++;
                break;
            case 1:
                mc.thePlayer.setPosition(mc.thePlayer.posX, launchY, mc.thePlayer.posZ);
                flyTimer.reset();
                step++;
                break;
            case 2:
                mc.thePlayer.motionY = 0;
                MovementUtils.strafe(0.6f);

                if (flyTimer.hasTimePassed(5000)) {
                    Menace.instance.moduleManager.flightModule.setToggled(false);
                }
                break;
        }
        super.onPreMotion(event);
    }
}
