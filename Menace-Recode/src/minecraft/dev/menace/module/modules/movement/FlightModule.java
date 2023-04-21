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
	ListSetting otherMode;
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
		ArrayList<String> otherValues = new ArrayList<>();
		for (FlightMode fm : FlightMode.values()) {
			switch (fm.getType())
			{
				case VANILLA:
					vanillaValues.add(fm.getName());
					break;

				case VERUS:
					verusValues.add(fm.getName());
					break;

				case OTHER:
					otherValues.add(fm.getName());

					break;

				default:
					break;
			}
		}
		
		vanillaMode = new ListSetting("VanillaMode", true, "Creative", vanillaValues.toArray(new String[] {})) {
			@Override
			public void constantCheck() {
				this.setVisible(Menace.instance.moduleManager.flightModule.mode.getValue().equalsIgnoreCase("Vanilla"));
			}
		};
		verusMode = new ListSetting("VerusMode", false, "Jump", verusValues.toArray(new String[] {})) {
			@Override
			public void constantCheck() {
				this.setVisible(Menace.instance.moduleManager.flightModule.mode.getValue().equalsIgnoreCase("Verus"));
			}
		};
		otherMode = new ListSetting("OtherMode", false, "BlocksMC", otherValues.toArray(new String[] {})) {
			@Override
			public void constantCheck() {
				this.setVisible(Menace.instance.moduleManager.flightModule.mode.getValue().equalsIgnoreCase("Other"));
			}
		};
		dmgMode = new ListSetting("DmgMode", false, "Basic", new String[] {"Basic", "Verus", "Jump", "Bow", "Wait"}) {
			@Override
			public void constantCheck() {
				this.setVisible(Menace.instance.moduleManager.flightModule.mode.getValue().equalsIgnoreCase("Vanilla")
						&& Menace.instance.moduleManager.flightModule.vanillaMode.getValue().equalsIgnoreCase("Damage"));
			}
		};
		speed = new SliderSetting("Speed", false, 2, 1, 10, true) {
			@Override
			public void constantCheck() {
				this.setVisible(Menace.instance.moduleManager.flightModule.mode.getValue().equalsIgnoreCase("Vanilla")
						&& (Menace.instance.moduleManager.flightModule.vanillaMode.getValue().equalsIgnoreCase("Damage")
						|| Menace.instance.moduleManager.flightModule.vanillaMode.getValue().equalsIgnoreCase("Motion")));
			}
		};
		viewbobbingYaw = new SliderSetting("Viewbob", false, 0.1, 0, 0.5, 0.1, false);
		this.rSetting(mode);
		this.rSetting(vanillaMode);
		this.rSetting(verusMode);
		this.rSetting(otherMode);
		this.rSetting(dmgMode);
		this.rSetting(speed);
		this.rSetting(viewbobbingYaw);
		super.setup();
	}
	
	@Override
	public void onEnable() {
		for (FlightMode fm : FlightMode.values()) {
			if (vanillaMode.getValue().equalsIgnoreCase(fm.getName()) &&  mode.getValue().equalsIgnoreCase("vanilla")) {
				this.setDisplayName(fm.getName());
				flightMode = fm.getFlight();
				break;
			} else if (verusMode.getValue().equalsIgnoreCase(fm.getName()) &&  mode.getValue().equalsIgnoreCase("verus")) {
				this.setDisplayName(fm.getName());
				flightMode = fm.getFlight();
				break;
			} else if (otherMode.getValue().equalsIgnoreCase(fm.getName()) && mode.getValue().equalsIgnoreCase("Other")) {
				this.setDisplayName(fm.getName());
				flightMode = fm.getFlight();
				break;
			}
		}
		
		flightMode.launchX = mc.thePlayer.posX;
		flightMode.launchY = mc.thePlayer.posY;
		flightMode.launchZ = mc.thePlayer.posZ;

		super.onEnable();
		flightMode.onEnable();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mc.thePlayer.onGround) {
			flightMode.launchY = mc.thePlayer.posY;
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
		mc.thePlayer.cameraYaw = viewbobbingYaw.getValueF();
		mc.thePlayer.prevCameraYaw = viewbobbingYaw.getValueF();
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
		super.onDisable();
		flightMode.onDisable();
	}

}
