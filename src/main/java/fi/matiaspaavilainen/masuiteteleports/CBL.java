package fi.matiaspaavilainen.masuiteteleports;

import java.util.UUID;

public class CBL {
    private UUID attacker;
    private UUID victim;

    public CBL(UUID victim, UUID attacker){
        this.victim = victim;
        this.attacker = attacker;
    }

    public UUID getVictim() {
        return victim;
    }

    public void setVictim(UUID victim) {
        this.victim = victim;
    }

    public UUID getAttacker() {
        return attacker;
    }

    public void setAttacker(UUID attacker) {
        this.attacker = attacker;
    }
}
