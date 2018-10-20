package fi.matiaspaavilainen.masuiteteleports.commands.force;

import fi.matiaspaavilainen.masuiteteleports.MaSuiteTeleports;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

// TODO: Add different types eg. to coords or to player
public class Teleport implements CommandExecutor {

    private MaSuiteTeleports plugin;

    public Teleport(MaSuiteTeleports p) {
        plugin = p;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player p = (Player) sender;
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("MaSuiteTeleports");
            switch (args.length) {
                case (1):
                    out.writeUTF("TeleportForceTo");
                    out.writeUTF(sender.getName());
                    out.writeUTF(args[0]);
                    break;
                case (2):
                    out.writeUTF("TeleportTargetToTarget");
                    out.writeUTF(sender.getName());
                    out.writeUTF(args[0]);
                    out.writeUTF(args[1]);
                    break;
                case (3):
                    if (!isDouble(args[0]) && !isDouble(args[1]) && !isDouble(args[2])) {
                        return false;
                    }
                    out.writeUTF("TeleportToXYZ");
                    out.writeUTF(sender.getName());
                    out.writeUTF(sender.getName());
                    out.writeDouble(Double.parseDouble(args[0]));
                    out.writeDouble(Double.parseDouble(args[1]));
                    out.writeDouble(Double.parseDouble(args[2]));
                    break;

                    // TODO: Make a cheeck to get command type
                case (4):
                    if (!isDouble(args[1]) && !isDouble(args[2]) && !isDouble(args[3])) {
                        return false;
                    }
                    out.writeUTF("TeleportToXYZ");
                    out.writeUTF(sender.getName());
                    out.writeUTF(args[0]);
                    out.writeDouble(Double.parseDouble(args[1]));
                    out.writeDouble(Double.parseDouble(args[2]));
                    out.writeDouble(Double.parseDouble(args[3]));
                    break;
                case (5):
                    if (!isDouble(args[1]) && !isDouble(args[2]) && !isDouble(args[3])) {
                        return false;
                    }
                    out.writeUTF("TeleportToCoordinates");
                    out.writeUTF(sender.getName());
                    out.writeUTF(args[0]);
                    out.writeDouble(Double.parseDouble(args[1]));
                    out.writeDouble(Double.parseDouble(args[2]));
                    out.writeDouble(Double.parseDouble(args[3]));
                    break;
            }

            p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if string is parsable to Double
    private boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
