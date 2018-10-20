package fi.matiaspaavilainen.masuiteteleports;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuiteteleports.managers.TeleportListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MaSuiteTeleports extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        super.onEnable();
        getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new TeleportListener(this));
    }

    @EventHandler
    public void onDeath(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("MaSuiteTeleports");
        out.writeUTF("SpawnPlayer");
        out.writeUTF(p.getName());
        p.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }
}
