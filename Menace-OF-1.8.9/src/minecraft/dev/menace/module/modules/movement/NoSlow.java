package dev.menace.module.modules.movement;

import java.util.ArrayList;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventPostMotionUpdate;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.event.events.EventSlowDown;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.StringSetting;
import dev.menace.utils.entity.self.PlayerUtils;
import dev.menace.utils.misc.ChatUtils;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlow extends Module {

	//Settings
	StringSetting mode;
	
	public NoSlow() {
		super("NoSlow", 0, Category.MOVEMENT);
	}
	
	@Override
	public void setup() {
		ArrayList<String> op = new ArrayList<>();
		op.add("Simple");
		op.add("NCP");
		mode = new StringSetting("Mode", this, "Simple", op);
		this.rSetting(mode);
	}
	
	@EventTarget
	public void onPre(EventPreMotionUpdate event) {
		this.setDisplayName("NoSlow§7 [" + mode.getString() + "]");
		if (!MC.thePlayer.isBlocking() || !mode.getString().equalsIgnoreCase("NCP") || !PlayerUtils.isMoving()) return;
		MC.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
    }
	
	@EventTarget
	public void onPost(EventPostMotionUpdate event) {
		if (!MC.thePlayer.isBlocking() || !mode.getString().equalsIgnoreCase("NCP") || !PlayerUtils.isMoving()) return;
		MC.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(MC.thePlayer.inventory.getCurrentItem()));
	}
	
	@EventTarget
	public void onSlow(EventSlowDown event) {
		event.setCancelled(true);
	}
	
}
