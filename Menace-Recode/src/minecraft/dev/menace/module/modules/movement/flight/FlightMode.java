package dev.menace.module.modules.movement.flight;

import dev.menace.module.modules.movement.flight.vanilla.CreativeFly;

public enum FlightMode {

	CREATIVE("Creative", new CreativeFly());
	
	FlightMode(String name, FlightBase flight) {
		
	}
	
}
