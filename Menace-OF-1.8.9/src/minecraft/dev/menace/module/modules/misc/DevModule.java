package dev.menace.module.modules.misc;

import java.util.concurrent.TimeUnit;

import com.google.gson.JsonObject;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.*;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.utils.entity.self.PlayerUtils;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.render.RenderUtils;
import dev.menace.utils.world.BlockUtils;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C69PacketMenaceOutbound;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.network.play.server.S69PacketMenaceInbound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.optifine.player.CapeImageBuffer;
import net.optifine.player.CapeUtils;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class DevModule extends Module {
	
	public DevModule() {
		super("Dev Module", 0, Category.MISC);
	}
	
	@Override
    public void onEnable() {
        super.onEnable();
        //toggle();
    }

	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		for (EntityPlayer p : MC.theWorld.playerEntities) {
			if (p.getName() == "M4rwaan" || p.getName() == "Nwwf" || p.getName() == "UmmDrRep" || p.getName() == "eyadd" || p.getName() == "1AhMeD") {
				ChatUtils.message("staff detected");
				MC.thePlayer.sendChatMessage("/hub");
			}
		}
	}
	
}
