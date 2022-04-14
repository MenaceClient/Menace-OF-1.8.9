package dev.menace.module.modules.movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventStep;
import dev.menace.event.events.EventStep.StepState;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.DoubleSetting;
import dev.menace.module.settings.StringSetting;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

public class Step extends Module {

	boolean stepping;
	
	//Settings
	StringSetting mode;
	DoubleSetting height;
	
	public Step() {
		super("Step", 0, Category.MOVEMENT);
	}
	
	@Override
	public void setup() {
		ArrayList<String> options = new ArrayList<String>();
		options.add("Vanilla");
		options.add("Verus");
		mode = new StringSetting("Mode", this, "Vanilla", options);
		height = new DoubleSetting("Step Height", this, 1, 1, 20);
		this.rSetting(mode);
		this.rSetting(height);
	}
	
	@Override
	public void onDisable() {
		stepping = false;
		super.onDisable();
	}
	
	@EventTarget
	public void onStep(EventStep event) {
		
		if (event.getStepState() == StepState.POST && stepping) stepping = false;
		
		if (event.getStepState() == StepState.POST) return;
		if (mode.getString().equalsIgnoreCase("Vanilla")) {
			event.setStepHeight((float) height.getValue());
		} else if (mode.getString().equalsIgnoreCase("Verus")) {
			stepping = true;
			MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 1.5, MC.thePlayer.posZ), 1, new ItemStack(Blocks.stone.getItem(MC.theWorld, new BlockPos(-1, -1, -1))), 0f, 0.94f, 0f));
			event.setStepHeight((float) height.getValue());
		}
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		if (event.getPacket() instanceof C02PacketUseEntity && ((C02PacketUseEntity)event.getPacket()).getAction() == C02PacketUseEntity.Action.ATTACK && stepping) {
			event.setCancelled(true);
		}
	}

}
