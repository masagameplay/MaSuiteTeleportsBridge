package fi.matiaspaavilainen.masuiteteleports.commands.spawns;

import fi.matiaspaavilainen.masuiteteleports.MaSuiteTeleports;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static fi.matiaspaavilainen.masuiteteleports.MaSuiteTeleports.colorize;

public class Delete implements CommandExecutor {


    private MaSuiteTeleports plugin;

    public Delete(MaSuiteTeleports p){
        plugin = p;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(args.length != 0){
            sender.sendMessage(colorize(plugin.config.getSyntaxes().getString("spawn.delete")));
            return false;
        }

        Player p = (Player) sender;
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("MaSuiteTeleports");
            out.writeUTF("DelSpawn");
            out.writeUTF(p.getName());
            p.sendPluginMessage(plugin,"BungeeCord", b.toByteArray());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}