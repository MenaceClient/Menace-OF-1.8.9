package dev.menace.module.modules.misc;

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
		/*
		double x = MC.thePlayer.posX;
		double y = MC.thePlayer.posY;
		double z = MC.thePlayer.posZ;
		
		int i = 0;
		while (i < 65) {
			PacketUtils.sendPacket(new C04PacketPlayerPosition(x, y + 0.049, z, false));
			PacketUtils.sendPacket(new C04PacketPlayerPosition(x, y, z, false));
			i++;
		}
		
		PacketUtils.sendPacket(new C04PacketPlayerPosition(x, y, z, true));
		*/
		MC.thePlayer.jump();
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}

}
