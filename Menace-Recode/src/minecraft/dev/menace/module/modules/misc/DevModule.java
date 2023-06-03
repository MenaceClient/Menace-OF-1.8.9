package dev.menace.module.modules.misc;

import com.mojang.authlib.GameProfile;
import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.*;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.player.PlayerUtils;
import dev.menace.utils.render.RenderUtils;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DevModule extends BaseModule {

	boolean damage = false;
	double y = 0;

	public DevModule() {
		super("DevModule", Category.MISC, Keyboard.KEY_P);
	}

	@Override
	public void setup() {
		super.setup();
	}

	@Override
	public void onEnable() {
		//PacketUtils.sendPacket(new C14PacketTabComplete("/"));

		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {

	}

	@EventTarget
	public void onPre(EventPreMotion event) {

	}

	@EventTarget
	public void onMove(EventMove event) {

	}

	@EventTarget
	public void onSendPacket(EventSendPacket event) {

	}

	@EventTarget
	public void onReceivePacket(EventReceivePacket event) {
		if (event.getPacket() instanceof S3APacketTabComplete) {
			S3APacketTabComplete packet = (S3APacketTabComplete) event.getPacket();

			StringBuilder builder = new StringBuilder();
			for (String s : packet.getMatches()) {
				String fixed = s.replace("/", "");
				if (fixed.startsWith("?")) {
					continue;
				}
				builder.append(s).append(", ");
			}

			//ChatUtils.message("Plugins: " + builder);
		}
	}

	@EventTarget
	public void onRender(EventRender2D event) {
		RenderUtils.renderGaussianBlurredRect(0, 0, 100, 100, new Color(1f, 0f, 0f, 0.5f));
	}

	@EventTarget
	public void onTeleport(EventTeleport event) {

	}


}