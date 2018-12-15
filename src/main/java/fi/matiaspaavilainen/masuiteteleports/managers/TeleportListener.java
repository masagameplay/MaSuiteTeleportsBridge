package fi.matiaspaavilainen.masuiteteleports.managers;

import fi.matiaspaavilainen.masuiteteleports.MaSuiteTeleports;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;

public class TeleportListener implements PluginMessageListener {
    private MaSuiteTeleports plugin;

    public TeleportListener(MaSuiteTeleports p) {
        plugin = p;
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        final DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        String subchannel = null;
        String method = null;
        try {
            subchannel = in.readUTF();
            if (subchannel.equals("MaSuiteTeleports")) {
                method = in.readUTF();
                if (method.equals("PlayerToPlayer")) {
                    teleportPlayer(in.readUTF(), in.readUTF());
                }
                if (method.equals("PlayerToXYZ")) {
                    Player p = Bukkit.getPlayer(in.readUTF());
                    if (p == null) {
                        return;
                    }
                    plugin.tpQue.add(p.getUniqueId());
                    p.teleport(new Location(p.getWorld(), in.readDouble(), in.readDouble(), in.readDouble()));
                    Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> plugin.tpQue.remove(p.getUniqueId()), 100);
                }
                if (method.equals("PlayerToLocation")) {
                    Player p = Bukkit.getPlayer(in.readUTF());
                    if (p == null) {
                        return;
                    }
                    plugin.tpQue.add(p.getUniqueId());
                    p.teleport(new Location(Bukkit.getWorld(in.readUTF()), in.readDouble(), in.readDouble(), in.readDouble()));
                    Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> plugin.tpQue.remove(p.getUniqueId()), 100);
                }

                if (method.equals("SpawnPlayer")) {
                    Player p = Bukkit.getPlayer(in.readUTF());
                    if (p == null) {
                        return;
                    }
                    String[] locInfo = in.readUTF().split(":");
                    Location loc = new Location(Bukkit.getWorld(locInfo[0]), Double.parseDouble(locInfo[1]), Double.parseDouble(locInfo[2]), Double.parseDouble(locInfo[3]), Float.parseFloat(locInfo[4]), Float.parseFloat(locInfo[5]));
                    p.teleport(loc);
                }
                if (method.equals("GetLocation")) {
                    Player p = Bukkit.getPlayer(in.readUTF());
                    if (p == null) {
                        return;
                    }
                    String server = in.readUTF();
                    try (ByteArrayOutputStream b = new ByteArrayOutputStream();
                         DataOutputStream out = new DataOutputStream(b)) {
                        out.writeUTF("MaSuiteTeleports");
                        out.writeUTF("GetLocation");
                        out.writeUTF(p.getName());
                        Location loc = p.getLocation();
                        out.writeUTF(loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getYaw() + ":" + loc.getPitch());
                        out.writeUTF(server);
                        p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void teleportPlayer(final String s, final String t) {
        Player sender = Bukkit.getPlayer(s);
        Player target = Bukkit.getPlayer(t);
        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (sender != null && target != null) {
                plugin.tpQue.add(sender.getUniqueId());
                sender.teleport(target);
            }
        }, 5);

        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> plugin.tpQue.remove(sender.getUniqueId()), 100);
    }
}
