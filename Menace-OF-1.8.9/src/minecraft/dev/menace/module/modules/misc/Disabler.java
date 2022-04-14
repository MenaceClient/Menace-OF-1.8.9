package dev.menace.module.modules.misc;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.modules.misc.disablers.DisablerBase;
import dev.menace.module.modules.misc.disablers.DisablerMode;
import dev.menace.module.modules.movement.flys.FlightMode;
import dev.menace.module.settings.StringSetting;
import dev.menace.utils.misc.MSTimer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S19PacketEntityStatus;

public class Disabler extends Module {
	
	private DisablerBase mode;
	
	//Settings
	StringSetting setMode;
	
	 public Disabler() {
		super("Disabler", 0, Category.MISC);
	}
	 
	@Override
	public void setup() {
		ArrayList<String> options = new ArrayList<>();
	       for (DisablerMode m : DisablerMode.values()) {
	    	   options.add(m.getName());
	       }
	       setMode = new StringSetting("Mode", this, "VerusCombat", options);
	       this.rSetting(setMode);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
        for (DisablerMode m : DisablerMode.values()) {
			if (setMode.getString().equalsIgnoreCase(m.getName())) {
				mode = m.getDisabler();
				this.setDisplayName("Disabler §7[" + m.getName() + "]");
				break;
			}
        }
        
        if (mode == null) {
        	this.toggle();
        }
        
		if (MC.theWorld == null) {
			this.toggle();
			return;
		}

		mode.onEnable();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {mode.onUpdate();}
	@EventTarget
    public void onSendPacket(EventSendPacket event) {mode.onSendPacket(event);}
	@EventTarget
    public void onRecievePacket(EventReceivePacket event) {mode.onReceivePacket(event);}
	
	@Override
	public void onDisable(){
		super.onDisable();
		
		mode.onDisable();
	}
}
