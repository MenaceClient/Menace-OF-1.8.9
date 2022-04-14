package dev.menace.module.modules.movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.Event2D;
import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventJump;
import dev.menace.event.events.EventKey;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventPostMotionUpdate;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.modules.movement.flys.FlightBase;
import dev.menace.module.modules.movement.flys.FlightMode;
import dev.menace.module.settings.BoolSetting;
import dev.menace.module.settings.DoubleSetting;
import dev.menace.module.settings.StringSetting;
import dev.menace.utils.entity.self.PlayerUtils;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.util.AxisAlignedBB;

public class Flight extends Module{
	 public static FlightBase mode;
	 public static FlightMode flightMode;
	 
	 //Settings
    public StringSetting setMode;
    public StringSetting dmgMode;
    public DoubleSetting speed;
    public BoolSetting fakeDmg;
    BoolSetting viewbobbing;
    DoubleSetting viewbobbingyaw;
    public BoolSetting vanillaKickBypass;
    
	public Flight() {
		super("Flight", Keyboard.KEY_F, Category.MOVEMENT);
	}
	
	@Override
	public void setup() {
		ArrayList<String> options = new ArrayList<>();
	    ArrayList<String> op = new ArrayList<>();
        for (FlightMode m : FlightMode.values()) {
        	options.add(m.getName());
        }
        
        op.add("Basic");
        op.add("Bypass");
        op.add("Bow");
        op.add("Wait");
        
        setMode = new StringSetting("Mode", this, "Vanilla", options);
        dmgMode = new StringSetting("Damage Mode", this, "Basic", op);
        speed = new DoubleSetting("Speed", this, 2, 1, 10);
        fakeDmg = new BoolSetting("FakeDamage", this, false);
        vanillaKickBypass = new BoolSetting("VanillaKickBypass", this, false);
        viewbobbing = new BoolSetting("Viewbobbing", this, false);
        viewbobbingyaw = new DoubleSetting("Viewbob yaw", this, 0.1, 0, 0.5);
        
        this.rSetting(setMode);
        this.rSetting(dmgMode);
        this.rSetting(speed);
        this.rSetting(fakeDmg);
        this.rSetting(viewbobbing);
        this.rSetting(viewbobbingyaw);
        this.rSetting(vanillaKickBypass);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		if (MC == null || MC.theWorld == null) {
			this.toggle();
			return;
		}
		
        for (FlightMode m : FlightMode.values()) {
			if (setMode.getString().equalsIgnoreCase(m.getName())) {
				flightMode = m;
				mode = m.getFlight();
				this.setDisplayName("Flight §7[" + m.getName() + "]");
			}
        }
        
        if (mode == null) {
        	this.onDisable();
        }
		
        mode.launchX = MC.thePlayer.posX;
		mode.launchY = MC.thePlayer.posY;
		mode.launchZ = MC.thePlayer.posZ;
		
		mode.onEnable();
		
        if (MC.thePlayer.onGround && fakeDmg.isChecked()) {
        	EventReceivePacket event = new EventReceivePacket(new S19PacketEntityStatus(MC.thePlayer, (byte) 2));
            event.call();
            if (!event.isCancelled()) {
                MC.thePlayer.handleStatusUpdate((byte) 2);
            }
        }
        
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {mode.onUpdate(); if (MC.thePlayer.onGround) mode.launchY = MC.thePlayer.posY;}
	
	@EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {mode.onPreMotion(event);}
	
	@EventTarget
    public void onMotion(EventPostMotionUpdate event) {
		
		if (viewbobbing.isChecked()) {
			MC.thePlayer.cameraYaw = (float) viewbobbingyaw.getValue();
			MC.thePlayer.prevCameraYaw = (float) viewbobbingyaw.getValue();
		}
		
		mode.onMotion(event);
	}
	
	@EventTarget
    public void onCollision(EventCollide event) {mode.onCollision(event);}
	
	@EventTarget
    public void onSendPacket(EventSendPacket event) {mode.onSendPacket(event);}
	
	@EventTarget
    public void onRPacket(EventReceivePacket event) {mode.onRecievePacket(event);}
	
	@EventTarget
	public void onRender2D(Event2D event) {mode.onRender2D();}
	
	@EventTarget
	public void onMove(EventMove event) {mode.onMove(event);}
	
	@EventTarget
	public void onJump(EventJump event) {mode.onJump(event);}
	
	@EventTarget
	public void onKey(EventKey event) {mode.onKey(event);}

	
	@Override
	public void onDisable(){
		super.onDisable();
		
		if (MC == null || MC.theWorld == null) {
			return;
		}
		
		mode.onDisable();
	}
}
