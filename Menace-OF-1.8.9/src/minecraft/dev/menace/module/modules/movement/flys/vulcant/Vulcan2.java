package dev.menace.module.modules.movement.flys.vulcant;

import dev.menace.Menace;
import dev.menace.event.events.EventJump;
import dev.menace.event.events.EventKey;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.module.modules.movement.flys.FlightBase;
import dev.menace.utils.entity.self.PlayerUtils;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Vulcan2 extends FlightBase{

	private int offGroundTicks, onGroundTicks, ticks, ticksSinceFlag, boostTicks;
    private boolean bool;
	
	@Override
	public void onPreMotion(EventPreMotionUpdate event) {
		
        if (MC.thePlayer.onGround) {
            offGroundTicks = 0;
            ++onGroundTicks;
        } else {
            onGroundTicks = 0;
            ++offGroundTicks;
        }
		
		ticksSinceFlag++;
		if (!(PlayerUtils.getBlockRelativeToPlayer(0, -0.2, 0) instanceof BlockAir) && MC.thePlayer.getDistanceSq(this.launchX, this.launchY, this.launchZ) > 4 * 4) {
			MC.thePlayer.jump();
			ticksSinceFlag = 0;
		}

		if (!(ticksSinceFlag <= 20 && ticksSinceFlag >= 0)) {
			MC.thePlayer.motionY = 0;
			switch (offGroundTicks) {
			case 1:
				MC.thePlayer.motionY = MC.gameSettings.keyBindJump.isKeyDown() ? 1 : 0.5;
				break;
			case 2:
				MC.thePlayer.motionY = MC.gameSettings.keyBindSneak.isKeyDown() ? -1 : -0.5;
				break;
			case 3:
				MC.thePlayer.motionY = 0;
				offGroundTicks = 0;
				break;
			}
		} else if (ticksSinceFlag >= 4) {
			MC.thePlayer.motionY = 0;
			MC.thePlayer.setPosition(MC.thePlayer.posX, Math.round(event.y / 0.5) * 0.5, MC.thePlayer.posZ);
		}

		if ((ticksSinceFlag <= 20 && ticksSinceFlag >= 0 && ticksSinceFlag >= 4) || MC.thePlayer.posY % 0.5 == 0) {
			final double mathGround2 = Math.round(event.y / 0.015625) * 0.015625;
			PlayerUtils.strafe((float) (0.2974 - 0.128));

			MC.thePlayer.setPosition(MC.thePlayer.posX, mathGround2, MC.thePlayer.posZ);

			event.y = mathGround2;
			event.setGround(true);
			MC.thePlayer.onGround = true;

		}

		if (bool || ticks > 15) {
			MC.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition((MC.thePlayer.posX + MC.thePlayer.lastTickPosX) / 2, (MC.thePlayer.posY + MC.thePlayer.lastTickPosY) / 2, (MC.thePlayer.posZ + MC.thePlayer.lastTickPosZ) / 2, true));
			PlayerUtils.strafe((float) (PlayerUtils.moveSpeed() * 1.5 * 2));
			MC.timer.timerSpeed = 1.2f + MC.thePlayer.hurtTime / 3f;
		} else {
			PlayerUtils.strafe((float) PlayerUtils.moveSpeed());
		}
	}
	
	@Override
	public void onJump(EventJump event) {
		event.setCancelled(true);
	}
	
	@Override
	public void onKey(EventKey event) {
		 if (ticksSinceFlag <= 20 && ticksSinceFlag > 0) {
             if (event.getKey() == MC.gameSettings.keyBindBack.getKeyCode() 
            		 || event.getKey() == MC.gameSettings.keyBindForward.getKeyCode()
            		 || event.getKey() == MC.gameSettings.keyBindLeft.getKeyCode()
            		 || event.getKey() == MC.gameSettings.keyBindRight.getKeyCode()
            		 || event.getKey() == MC.gameSettings.keyBindSneak.getKeyCode()) {
            	 event.setCancelled(true);
             }
             PlayerUtils.stop();
         }
	}
	
	@Override
	public void onRecievePacket(EventReceivePacket event) {
		if (!(event.getPacket() instanceof S08PacketPlayerPosLook)) return;
		
		final S08PacketPlayerPosLook s08 = (S08PacketPlayerPosLook) event.getPacket();
		if (MC.thePlayer.ticksExisted > 20) {
            if (Math.abs(s08.getX() - this.launchX) + Math.abs(s08.getY() - this.launchY) + Math.abs(s08.getZ() - this.launchZ) < 4) {
            	event.setCancelled(true);
                if (!bool) {
                    MC.thePlayer.hurtTime = 9;
                    bool = true;
                }
            } else {
                Menace.instance.moduleManager.flightModule.toggle();
            }
        }
	}

}
