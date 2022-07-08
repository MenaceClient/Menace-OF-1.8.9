package dev.menace.module.modules.misc;

import dev.menace.Menace;
import dev.menace.event.Event;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.event.events.EventWorldChange;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.MathUtils;
import dev.menace.utils.player.PacketUtils;
import dev.menace.utils.timer.MSTimer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DisablerModule extends BaseModule {
	 ListSetting mode;
	 private boolean messageSent;
	 
    public DisablerModule() {
        super("Disabler", Category.MISC, 0);
    }
    
    @Override
	public void onEnable() {
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
    
    @Override
    public void setup() {
    	mode = new ListSetting("Mode", true, "HAZEL", new String[] {"HAZEL", "C19", "VULCAN"});
        this.rSetting(mode);
        super.setup();
    }

    @EventTarget
    public void onUpdate(Event e) {
    	//	PacketUtils.sendPacket(new C19PacketResourcePackStatus("", C19PacketResourcePackStatus.Action.ACCEPTED));
   
   }

    
    @EventTarget
    public void onSendPacket(@NotNull EventSendPacket event) {
    }

    @EventTarget
    public void onWorldChange(EventWorldChange event) {

    }
    
    
    
    
    
    
  

}
