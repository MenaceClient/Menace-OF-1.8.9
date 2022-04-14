package dev.menace.module.modules.movement.flys.verus;

import dev.menace.Menace;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.ModuleManager;
import dev.menace.module.modules.movement.flys.FlightBase;
import dev.menace.utils.entity.self.PlayerUtils;
import dev.menace.utils.misc.ChatUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;

public class Verus6 extends FlightBase {
	
	@Override
	public void onEnable() {
		if (Menace.instance.moduleManager.killAuraModule.isToggled() && PlayerUtils.isInVoid()) {
			Menace.instance.moduleManager.killAuraModule.toggle();
			ChatUtils.warning("You cannot toggle this fly while killaura is on, toggled killaura as you are over the void.");
			return;
		} else if (Menace.instance.moduleManager.killAuraModule.isToggled() && !PlayerUtils.isInVoid()) {
			Menace.instance.moduleManager.flightModule.toggle();
			ChatUtils.warning("You cannot toggle this fly while killaura is on, toggled flight as you are not over the void.");
			return;
		}
	}
	
	@Override
	public void onSendPacket(EventSendPacket event) {
		if (event.getPacket() instanceof C02PacketUseEntity && ((C02PacketUseEntity)event.getPacket()).getAction() == C02PacketUseEntity.Action.ATTACK) {
			event.setCancelled(true);
		}
	}
	
	@Override
	public void onUpdate() {
		
		if (Menace.instance.moduleManager.killAuraModule.isToggled() && PlayerUtils.isInVoid()) {
			Menace.instance.moduleManager.killAuraModule.toggle();
			ChatUtils.warning("You cannot use this fly while killaura is on, toggled killaura as you are over the void.");
			return;
		} else if (Menace.instance.moduleManager.killAuraModule.isToggled() && !PlayerUtils.isInVoid()) {
			Menace.instance.moduleManager.flightModule.toggle();
			ChatUtils.warning("You cannot use this fly while killaura is on, toggled flight as you are not over the void.");
			return;
		}

		MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 1.5, MC.thePlayer.posZ), 1, new ItemStack(Blocks.stone.getItem(MC.theWorld, new BlockPos(-1, -1, -1))), 0f, 0.94f, 0f));
		MC.thePlayer.motionY = 0;
		
		if (MC.gameSettings.keyBindJump.isKeyDown()) {
			MC.thePlayer.motionY = 0.1; 
		}
		
		if (MC.gameSettings.keyBindSneak.isKeyDown()) {
			MC.thePlayer.motionY = -0.1; 
		}
	}
}
