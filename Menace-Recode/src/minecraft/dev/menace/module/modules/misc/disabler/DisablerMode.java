package dev.menace.module.modules.misc.disabler;

import dev.menace.module.modules.misc.disabler.misc.*;
import dev.menace.module.modules.misc.disabler.verus.*;

public enum DisablerMode {

    PINGSPOOF("PingSpoof", new PingSpoofDisabler()),
    VERUS("VerusTransaction", "Verus working exactly as intended", new VerusTransactionDisabler());

    final String name;
    final DisablerBase disabler;
    final String goofyName;

    DisablerMode(String name, DisablerBase disabler) {
        this.name = name;
        this.goofyName = name;
        this.disabler = disabler;
    }

    DisablerMode(String name, String goofyName, DisablerBase disabler) {
        this.name = name;
        this.goofyName = goofyName;
        this.disabler = disabler;
    }

    public String getName() {
        return name;
    }

    public String getGoofyName() {
        return goofyName;
    }

    public DisablerBase getDisabler() {
        return disabler;
    }

}
