package fi.matiaspaavilainen.masuiteteleports;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuiteteleports.commands.force.All;
import fi.matiaspaavilainen.masuiteteleports.commands.force.Here;
import fi.matiaspaavilainen.masuiteteleports.commands.force.Teleport;
import fi.matiaspaavilainen.masuiteteleports.commands.requests.Accept;
import fi.matiaspaavilainen.masuiteteleports.commands.requests.Deny;
import fi.matiaspaavilainen.masuiteteleports.commands.requests.To;
import fi.matiaspaavilainen.masuiteteleports.commands.spawns.Delete;
import fi.matiaspaavilainen.masuiteteleports.commands.spawns.Set;
import fi.matiaspaavilainen.masuiteteleports.commands.spawns.Spawn;
import fi.matiaspaavilainen.masuiteteleports.managers.TeleportListener;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MaSuiteTeleports extends JavaPlugin implements Listener {

    public Config config = new Config(this);

    @Override
    public void onEnable() {
        super.onEnable();
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new TeleportListener(this));

        // Create configs
        config.createConfigs();

        // Load
        loadCommands();

    }

    private void loadCommands(){
        // Force
        getCommand("tpall").setExecutor(new All(this));
        getCommand("tphere").setExecutor(new Here(this));
        getCommand("tp").setExecutor(new Teleport(this));

        // Requests
        getCommand("tpaccept").setExecutor(new Accept(this));
        getCommand("tpdeny").setExecutor(new Deny(this));
        getCommand("tpahere").setExecutor(new fi.matiaspaavilainen.masuiteteleports.commands.requests.Here(this));
        getCommand("tpa").setExecutor(new To(this));

        // Spawn
        getCommand("delspawn").setExecutor(new Delete(this));
        getCommand("setspawn").setExecutor(new Set(this));
        getCommand("spawn").setExecutor(new Spawn(this));
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

    public static String colorize(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
