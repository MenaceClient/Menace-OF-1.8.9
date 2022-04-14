package dev.menace.module.modules.misc.disablers;

import dev.menace.module.modules.misc.disablers.other.*;
import dev.menace.module.modules.misc.disablers.verus.*;

public enum DisablerMode {
	
	C00("C00", new C00()),
	KAURI("Kauri", new Kauri()),
	VERUSFULL("VerusFull", new VerusFull()),
	VERUSNEW("BlocksMC", new VerusNew()),
	VERUSBOAT("VerusBoat", new VerusBoat()),
	VERUSSCAFFOLD("VerusScaffold", new VerusScaffold()),
	VERUSCOMBAT("VerusCombat", new VerusCombat()),
	VERUS("VerusOld", new OldVerusDisabler());
	
	String name;
	DisablerBase disabler;
	DisablerMode(String name, DisablerBase disabler) {
		this.name = name;
		this.disabler = disabler;
	}
	
	public String getName() {
		return this.name;
	}
	
	public DisablerBase getDisabler() {
		return this.disabler;
	}
	
}
