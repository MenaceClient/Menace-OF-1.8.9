package dev.menace.utils.player;

import net.minecraft.client.Minecraft;

public class MovementUtils {

	private static Minecraft MC = Minecraft.getMinecraft();
	
	public static boolean isMoving() {
		return MC.thePlayer.motionX != 0 && MC.thePlayer.motionZ != 0;
	}
	
}
