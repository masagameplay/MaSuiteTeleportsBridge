package fi.matiaspaavilainen.masuiteteleports;

import fi.matiaspaavilainen.masuiteteleports.commands.Back;
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
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MaSuiteTeleports extends JavaPlugin implements Listener {

    public Config config = new Config(this);

    public final List<CommandSender> in_command = new ArrayList<>();

    @Override
    public void onEnable() {
        super.onEnable();
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new TeleportListener(this));

        // Create configs
        config.createConfigs();
        saveDefaultConfig();

        // Load
        loadCommands();

    }

    private void loadCommands() {
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

        // Back
        getCommand("back").setExecutor(new Back(this));
    }

    @EventHandler
    public void onDeath(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        switch (getConfig().getString("respawn-type").toLowerCase()) {
            case ("none"):
                break;
            case ("bed"):
                if (p.getBedSpawnLocation() != null) {
                    p.teleport(p.getBedSpawnLocation());
                } else {
                    try {
                        out.writeUTF("MaSuiteTeleports");
                        out.writeUTF("SpawnPlayer");
                        out.writeUTF(p.getName());
                        p.sendPluginMessage(this, "BungeeCord", b.toByteArray());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                break;
            case ("home"):
                try {
                    out.writeUTF("HomeCommand");
                    out.writeUTF(p.getName());
                    out.writeUTF("home");
                    p.sendPluginMessage(this, "BungeeCord", b.toByteArray());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            case ("spawn"):
                try {
                    out.writeUTF("MaSuiteTeleports");
                    out.writeUTF("SpawnPlayer");
                    out.writeUTF(p.getName());
                    p.sendPluginMessage(this, "BungeeCord", b.toByteArray());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;

        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("MaSuiteTeleports");
            out.writeUTF("GetLocation");
            out.writeUTF(e.getEntity().getName());
            Location loc = e.getEntity().getLocation();
            out.writeUTF(loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getYaw() + ":" + loc.getPitch());
            out.writeUTF("DETECTSERVER");
            e.getEntity().sendPluginMessage(this, "BungeeCord", b.toByteArray());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String colorize(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
