package dev.menace.module.modules.movement.flight;

import dev.menace.module.modules.movement.flight.other.*;
import dev.menace.module.modules.movement.flight.vanilla.*;
import dev.menace.module.modules.movement.flight.verus.*;

public enum FlightMode {

	CREATIVE("Creative", new CreativeFly(), FlightType.VANILLA),
	MOTION("Motion", new MotionFly(), FlightType.VANILLA),
	FAKEGROUND("FakeGround", new FakeGroundFly(), FlightType.VANILLA),
	DAMAGE("Damage", new DamageFly(), FlightType.VANILLA),
	VERUSJUMP("Jump", new VerusJumpFly(), FlightType.VERUS),
	VERUSBOB("Bob", new VerusBobFly(), FlightType.VERUS),
	BLOCKSMC("NCPSlime", new BlocksMCFly(), FlightType.OTHER),
	MATRIXDAMAGEFLY("MatrixDamage", new MatrixDamageFly(), FlightType.OTHER);
	
	
	final String name;
	final FlightBase flight;
	final FlightType flightType;
	private FlightMode(String name, FlightBase flight, FlightType flightType) {
		this.name = name;
		this.flight = flight;
		this.flightType = flightType;
	}
	public String getName() {
		return name;
	}
	public FlightBase getFlight() {
		return flight;
	}
	
	public FlightType getType() {
		return flightType;
	}
	
	public enum FlightType {
		VANILLA("Vanilla"),
		VERUS("Verus"),
		OTHER("Other");
		
		String name;
		private FlightType(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
}
