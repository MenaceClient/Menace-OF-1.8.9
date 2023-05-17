package dev.menace.module.modules.misc;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventTeleport;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.PacketUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DevModule extends BaseModule {

	Queue<C03PacketPlayer> packetQueue = new ConcurrentLinkedDeque<>();

	public DevModule() {
		super("DevModule", Category.MISC, Keyboard.KEY_P);
	}

	@Override
	public void setup() {
		super.setup();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onDisable() {
		packetQueue.clear();
		super.onDisable();
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
	}

	@EventTarget
	public void onSendPacket(EventSendPacket event) {

	}

	@EventTarget
	public void onTeleport(EventTeleport event) {
		event.cancel();
		ChatUtils.message("Edited teleport to " + event.getPosX() + ", " + event.getPosY() + ", " + event.getPosZ());
		//this.toggle();
	}


}