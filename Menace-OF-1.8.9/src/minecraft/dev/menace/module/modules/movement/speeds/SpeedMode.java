package dev.menace.module.modules.movement.speeds;

import dev.menace.module.modules.movement.speeds.vanilla.*;
import dev.menace.module.modules.movement.speeds.verus.*;

public enum SpeedMode {
	
	BHOP("Bhop", new Bhop()),
	ONGROUND("OnGround", new OnGround()),
	GROUNDSTRAFEHOP("GroundStrafeHop", new GroundStrafeHop()),
	VANILLAYPORT("VanillaYPort", new VanillaYPort()),
	VERUS("Verus", new Verus()),
	VERUSLOWHOP("VerusLowHop", new VerusLowHop()),
	VERUSLOWHOP2("VerusLowHop2", new VerusLowHop2()),
	VERUSYPORT("VerusYPort", new VerusYPort()),
	VERUSDMG("VerusDmg", new VerusDmg());

	
	private String name;
	private SpeedBase speed;
	SpeedMode(String name, SpeedBase speed) {
		this.name = name;
		this.speed = speed;
	}
	
 	public String getName() {
 		return this.name;
 	}
 	
 	public SpeedBase getSpeed() {
 		return this.speed;
 	}
	
}
