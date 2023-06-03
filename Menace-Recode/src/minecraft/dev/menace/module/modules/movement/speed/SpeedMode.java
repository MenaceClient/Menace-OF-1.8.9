package dev.menace.module.modules.movement.speed;

import dev.menace.module.modules.movement.speed.ncp.*;
import dev.menace.module.modules.movement.speed.other.*;
import dev.menace.module.modules.movement.speed.vanilla.*;
import dev.menace.module.modules.movement.speed.verus.*;

public enum SpeedMode {

    BHOP("BHop", new BhopSpeed(), SpeedType.VANILLA),
    STRAFE("Strafe", new StrafeSpeed(), SpeedType.VANILLA),
    VERUSLOWHOP("LowHop", new VerusLowhopSpeed(), SpeedType.VERUS),
    NCPLOWHOP("LowHop", new NCPLowHopSpeed(), SpeedType.NCP),
    NCPWTFSPEED("WTF (OldNCP)", new NCPWTFSpeed(), SpeedType.NCP),
    BLOCKSMC("BlocksMC", new BlocksMCSpeed(), SpeedType.OTHER),
    EXPERIMENTAL("Experimental", new ExperimentalSpeed(), SpeedType.OTHER);

    final String name;
    final SpeedBase speed;
    final SpeedType speedType;
    SpeedMode(String name, SpeedBase speed, SpeedType speedType) {
        this.name = name;
        this.speed = speed;
        this.speedType = speedType;
    }
    public String getName() {
        return name;
    }
    public SpeedBase getSpeed() {
        return speed;
    }

    public SpeedType getType() {
        return speedType;
    }

    public enum SpeedType {

        VANILLA("Vanilla"),
        VERUS("Verus"),
        NCP("NCP"),
        OTHER("Other");

        final String name;
        SpeedType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
