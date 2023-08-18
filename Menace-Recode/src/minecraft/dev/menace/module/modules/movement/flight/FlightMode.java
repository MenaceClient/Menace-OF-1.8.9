package dev.menace.module.modules.movement.flight;

import dev.menace.module.modules.movement.flight.ncp.*;
import dev.menace.module.modules.movement.flight.other.*;
import dev.menace.module.modules.movement.flight.vanilla.*;
import dev.menace.module.modules.movement.flight.verus.*;
import dev.menace.module.modules.movement.flight.vulcan.*;

public enum FlightMode {

	CREATIVE("Creative", new CreativeFly(), FlightType.VANILLA),
	MOTION("Motion", new MotionFly(), FlightType.VANILLA),
	FAKEGROUND("FakeGround", new FakeGroundFly(), FlightType.VANILLA),
	DAMAGE("Damage", new DamageFly(), FlightType.VANILLA),
	VERUSJUMP("Jump", new VerusJumpFly(), FlightType.VERUS),
	VERUSBOB("Bob", new VerusBobFly(), FlightType.VERUS),
	NCPSLIME("NCPSlime", new NCPSlimeFly(), FlightType.NCP),
	OLDNCP("OldNCP", new OldNCPFly(), FlightType.NCP),
	VULCANCLIP("Clip", new VulcanClipFly(), FlightType.VULCAN),
	MATRIXDAMAGEFLY("MatrixDamage", new MatrixDamageFly(), FlightType.OTHER);
	
	
	final String name;
	final FlightBase flight;
	final FlightType flightType;
	FlightMode(String name, FlightBase flight, FlightType flightType) {
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
		NCP("NCP"),
		VULCAN("Vulcan"),
		OTHER("Other");
		
		final String name;
		private FlightType(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
}
