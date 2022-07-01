package dev.menace.module.modules.misc;

import dev.menace.utils.misc.ChatUtils;
import org.lwjgl.input.Keyboard;

import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.player.PacketUtils;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public class DevModule extends BaseModule {

	public DevModule() {
		super("DevModule", Category.MISC, Keyboard.KEY_P);
	}
	
	@Override
	public void onEnable() {
		ChatUtils.message(String.valueOf(MC.thePlayer.rotationYaw));
		ChatUtils.message(String.valueOf(MC.thePlayer.rotationPitch));
		ChatUtils.message(String.valueOf(MC.thePlayer.posX));
		ChatUtils.message(String.valueOf(MC.thePlayer.posY));
		ChatUtils.message(String.valueOf(MC.thePlayer.posZ));
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}

}
