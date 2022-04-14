package dev.menace.module.modules.movement.flys;

import dev.menace.module.modules.movement.flys.other.*;
import dev.menace.module.modules.movement.flys.vanilla.*;
import dev.menace.module.modules.movement.flys.verus.*;
import dev.menace.module.modules.movement.flys.vulcant.*;
import dev.menace.module.modules.movement.speeds.SpeedBase;

public enum FlightMode {

	VANILLA("Vanilla", new Vanilla()),
	BASIC("MotionY", new Basic()),
	BASIC2("GroundSpoof", new Basic2()),
	PULSE("TimerPulse", new Pulse()),
	JETPACK("JetPack", new Jetpack()),
	VERUS6("Verus MotionY", new Verus6()),
	VERUS("Verus Jump", new Verus()),
	VERUS2("Verus Bob", new Verus2()),
	VERUS4("Verus Bob 2", new Verus4()),
	VERUS5("Verus Bob 3", new Verus5()),
	VERUS3("Verus Fast", new Verus3()),
	VULCAN("Vulcan", new Vulcan()),
	VULCAN2("Vulcan2", new Vulcan2()),
	NCP("NCP", new NCP()),
	FUNCRAFT("Funcraft", new Funcraft());
	
	
	private String name;
	private FlightBase flight;
	FlightMode(String name, FlightBase flight) {
		this.name = name;
		this.flight = flight;
	}
	
 	public String getName() {
 		return this.name;
 	}
 	
 	public FlightBase getFlight() {
 		return this.flight;
 	}
}
