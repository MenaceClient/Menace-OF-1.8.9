package dev.menace.module.modules.movement;

import java.util.ArrayList;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventJump;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventPostMotionUpdate;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.modules.movement.speeds.SpeedBase;
import dev.menace.module.modules.movement.speeds.SpeedMode;
import dev.menace.module.settings.StringSetting;
import dev.menace.utils.entity.self.PlayerUtils;

public class Speed extends Module {

	private SpeedBase mode;
	
	//Settings
    StringSetting setMode;
	
	public Speed() {
		super("Speed", 0, Category.MOVEMENT);
	}
	
	@Override
	public void setup() {
	    ArrayList<String> options = new ArrayList<>();
        for (SpeedMode m : SpeedMode.values()) {
        	options.add(m.getName());
        }
        
        setMode = new StringSetting("Mode", this, "VerusLowHop", options);
        this.rSetting(setMode);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {mode.onUpdate();}
	
	@EventTarget
    public void onMove(EventMove event) {mode.onMove(event);}

	@Override
	public void onEnable() {
		super.onEnable();
		
		for (SpeedMode m : SpeedMode.values()) {
			if (setMode.getString().equalsIgnoreCase(m.getName())) {
				mode = m.getSpeed();
				this.setDisplayName("Speed §7[" + m.getName() + "]");
			}
		}
		
		mode.onEnable();
	}
	
	@EventTarget
	public void onJump(EventJump event) {mode.onJump(event);}
	
	@EventTarget
    public void onCollision(EventCollide event) {mode.onCollision(event);}
	
	@EventTarget
	public void onRecievePacket(EventReceivePacket event) {mode.onRecievePacket(event);}
	
	@EventTarget
	public void onPreMotion(EventPreMotionUpdate event) {mode.onPreMotion(event);}
	
	@Override
	public void onDisable() {
		super.onDisable();
		
		mode.onDisable();
	}

}
