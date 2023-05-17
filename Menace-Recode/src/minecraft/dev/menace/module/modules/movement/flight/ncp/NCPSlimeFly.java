package dev.menace.module.modules.movement.flight.ncp;

import dev.menace.Menace;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.module.modules.movement.flight.FlightBase;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.world.BlockUtils;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;

public class NCPSlimeFly extends FlightBase {

    boolean canFly;
    int flagCount;

    @Override
    public void onEnable() {
        canFly = false;
        flagCount = 0;
    }

    @Override
    public void onUpdate() {
        if (flagCount > 3) {
            ChatUtils.message("Flight disabled due to flag.");
            Menace.instance.moduleManager.flightModule.setToggled(false);
            canFly = false;
            return;
        }

        if(!canFly && BlockUtils.getBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ)) == Blocks.slime_block) canFly = true;

        if (!canFly) return;
        mc.thePlayer.motionY = 0;
    }

    @Override
    public void onReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook && canFly) {
            flagCount++;
        }
    }

}
