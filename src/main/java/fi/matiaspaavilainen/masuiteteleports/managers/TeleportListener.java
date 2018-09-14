package fi.matiaspaavilainen.masuiteteleports.managers;

import fi.matiaspaavilainen.masuiteteleports.MaSuiteTeleports;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class TeleportListener implements PluginMessageListener {
    private MaSuiteTeleports plugin;
    public TeleportListener(MaSuiteTeleports p){
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
            method = in.readUTF();
            if (subchannel.equals("Teleport")) {
                if(method.equals("SenderToPlayer")){
                    teleportPlayer(in.readUTF(), in.readUTF());
                }
                if(method.equals("PlayerToPlayer")){
                    teleportPlayer(in.readUTF(), in.readUTF());

                }
                if(method.equals("SenderToCoords")){
                    Player sender = Bukkit.getPlayer(in.readUTF());
                    Location loc = new Location(sender.getWorld(), in.readDouble(), in.readDouble(), in.readDouble());
                    sender.teleport(loc);
                }
                if(method.equals("PlayerToCoords")){
                    Player target = Bukkit.getPlayer(in.readUTF());
                    Location loc = new Location(target.getWorld(), in.readDouble(), in.readDouble(), in.readDouble());
                    target.teleport(loc);
                }
                if(method.equals("SpawnPlayer")){
                    Player p = Bukkit.getPlayer(UUID.fromString(in.readUTF()));
                    Location loc = new Location(Bukkit.getWorld(in.readUTF()), in.readDouble(), in.readDouble(), in.readDouble(), in.readFloat(), in.readFloat());
                    p.teleport(loc);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void teleportPlayer(final String s, final String t) {
        Bukkit.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
            public void run() {
                Player sender = Bukkit.getPlayer(s);
                Player target = Bukkit.getPlayer(t);
                if(sender != null && target != null){
                    sender.teleport(target);
                }
            }
        }, 5);
    }
}
