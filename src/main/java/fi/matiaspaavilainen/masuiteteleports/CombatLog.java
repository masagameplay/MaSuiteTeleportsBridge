package fi.matiaspaavilainen.masuiteteleports;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashSet;
import java.util.Set;

public class CombatLog implements Listener {

    public static Set<CBL> combatLocks = new HashSet<CBL>();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
            CBL cbl = new CBL(e.getEntity().getUniqueId(), e.getDamager().getUniqueId());
            combatLocks.add(cbl);
        }
    }
}
