package fi.matiaspaavilainen.masuiteteleports.commands.requests;

import fi.matiaspaavilainen.masuiteteleports.MaSuiteTeleports;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class To implements CommandExecutor {

    private MaSuiteTeleports plugin;

    public To(MaSuiteTeleports p) {
        plugin = p;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (args.length != 1) {
            return false;
        }

        Player p = (Player) sender;
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("MaSuiteTeleports");
            out.writeUTF("TeleportRequestTo");
            out.writeUTF(sender.getName());
            out.writeUTF(args[0]);
            p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
