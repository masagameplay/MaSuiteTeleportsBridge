package fi.matiaspaavilainen.masuiteteleports.commands.spawns;

import fi.matiaspaavilainen.masuiteteleports.MaSuiteTeleports;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Spawn implements CommandExecutor {


    private MaSuiteTeleports plugin;

    public Spawn(MaSuiteTeleports p){
        plugin = p;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(args.length != 0){
            return false;
        }

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("MaSuiteTeleports");
            out.writeUTF("SpawnPlayer");
            out.writeUTF(sender.getName());
            sender.getServer().sendPluginMessage(plugin,"BungeeCord", b.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
