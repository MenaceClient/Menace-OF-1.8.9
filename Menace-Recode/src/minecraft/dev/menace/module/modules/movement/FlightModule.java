package dev.menace.module.modules.movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventJump;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventPostMotion;
import dev.menace.event.events.EventPreMotion;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.module.DontSaveState;
import dev.menace.module.modules.movement.flight.FlightBase;
import dev.menace.module.modules.movement.flight.FlightMode;
import dev.menace.module.modules.movement.flight.FlightMode.FlightType;
import dev.menace.module.settings.ListSetting;
import dev.menace.module.settings.SliderSetting;
import dev.menace.module.settings.ToggleSetting;

@DontSaveState
public class FlightModule extends BaseModule {
	
	FlightBase flightMode;
	
	public ListSetting mode;
	ListSetting vanillaMode;
	ListSetting verusMode;
	public ListSetting dmgMode;
	public SliderSetting speed;
	SliderSetting viewbobbingYaw;
	
	public FlightModule() {
		super("Flight", "Fly like a bird!", Category.MOVEMENT, Keyboard.KEY_F);
	}
	
	@Override
	public void setup() {
		ArrayList<String> values = new ArrayList<>();
		for (FlightType fm : FlightType.values()) {
			values.add(fm.getName());
		}
		mode = new ListSetting("Mode", true, "Vanilla", values.toArray(new String[] {}));
		
		ArrayList<String> vanillaValues = new ArrayList<>();
		ArrayList<String> verusValues = new ArrayList<>();
		for (FlightMode fm : FlightMode.values()) {
			switch (fm.getType())
			{
			case VANILLA:
				vanillaValues.add(fm.getName());
				break;
			case VERUS:
				verusValues.add(fm.getName());
				break;
			default:
				break;
			}
		}
		
		vanillaMode = new ListSetting("VanillaMode", true, "Creative", vanillaValues.toArray(new String[] {})) {
			@Override
			public void constantCheck() {
				if (Menace.instance.moduleManager.flightModule.mode.getValue().equalsIgnoreCase("Vanilla")) {
					this.setVisible(true);
				} else {
					this.setVisible(false);
				}
			}
		};
		verusMode = new ListSetting("VerusMode", false, "Jump", verusValues.toArray(new String[] {})) {
			@Override
			public void constantCheck() {
				if (Menace.instance.moduleManager.flightModule.mode.getValue().equalsIgnoreCase("Verus")) {
					this.setVisible(true);
				} else {
					this.setVisible(false);
				}
			}
		};
		dmgMode = new ListSetting("DmgMode", false, "Basic", new String[] {"Basic", "Verus", "Jump", "Bow", "Wait"}) {
			@Override
			public void constantCheck() {
				if (Menace.instance.moduleManager.flightModule.mode.getValue().equalsIgnoreCase("Vanilla")
						&& Menace.instance.moduleManager.flightModule.vanillaMode.getValue().equalsIgnoreCase("Damage")) {
					this.setVisible(true);
				} else {
					this.setVisible(false);
				}
			}
		};
		speed = new SliderSetting("Speed", false, 2, 1, 10, true) {
			@Override
			public void constantCheck() {
				if (Menace.instance.moduleManager.flightModule.mode.getValue().equalsIgnoreCase("Vanilla")
						&& Menace.instance.moduleManager.flightModule.vanillaMode.getValue().equalsIgnoreCase("Damage")) {
					this.setVisible(true);
				} else {
					this.setVisible(false);
				}
			}
		};
		viewbobbingYaw = new SliderSetting("Viewbob", false, 0.1, 0, 0.5, 0.1, false);
		this.rSetting(mode);
		this.rSetting(vanillaMode);
		this.rSetting(verusMode);
		this.rSetting(dmgMode);
		this.rSetting(speed);
		this.rSetting(viewbobbingYaw);
		super.setup();
	}
	
	@Override
	public void onEnable() {
		for (FlightMode fm : FlightMode.values()) {
			if (vanillaMode.getValue().equalsIgnoreCase(fm.getName()) &&  mode.getValue().equalsIgnoreCase("vanilla")) {
				flightMode = fm.getFlight();
				break;
			} else if (verusMode.getValue().equalsIgnoreCase(fm.getName()) &&  mode.getValue().equalsIgnoreCase("verus")) {
				flightMode = fm.getFlight();
				break;
			}
		}
		
		flightMode.launchX = MC.thePlayer.posX;
		flightMode.launchY = MC.thePlayer.posY;
		flightMode.launchZ = MC.thePlayer.posZ;
		
		flightMode.onEnable();
		super.onEnable();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (MC.thePlayer.onGround) {
			flightMode.launchY = MC.thePlayer.posY;
		}
		flightMode.onUpdate();
		}
	@EventTarget
	public void onCollide(EventCollide event) {flightMode.onCollide(event);}
	@EventTarget
	public void onMove(EventMove event) {flightMode.onMove(event);}
	@EventTarget
	public void onPreMotion(EventPreMotion event) {
		flightMode.onPreMotion(event);
		MC.thePlayer.cameraYaw = viewbobbingYaw.getValueF();
		MC.thePlayer.prevCameraYaw = viewbobbingYaw.getValueF();
	}
	@EventTarget
	public void onPostMotion(EventPostMotion event) {flightMode.onPostMotion(event);}
	@EventTarget
	public void onSentPacket(EventSendPacket event) {flightMode.onSendPacket(event);}
	@EventTarget
	public void onReceivePacket(EventReceivePacket event) {flightMode.onReceivePacket(event);}
	@EventTarget
	public void onJump(EventJump event) {flightMode.onJump(event);}
	
	@Override
	public void onDisable() {
		flightMode.onDisable();
		super.onDisable();
	}

}
