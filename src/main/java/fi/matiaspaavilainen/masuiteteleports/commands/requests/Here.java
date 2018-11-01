package fi.matiaspaavilainen.masuiteteleports.commands.requests;

import fi.matiaspaavilainen.masuiteteleports.MaSuiteTeleports;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static fi.matiaspaavilainen.masuiteteleports.MaSuiteTeleports.colorize;

public class Here implements CommandExecutor {
	private MaSuiteTeleports plugin;

	public Here(MaSuiteTeleports p) {
		plugin = p;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}

		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

			if (args.length != 1) {
				sender.sendMessage(colorize(plugin.config.getSyntaxes().getString("tpahere")));
				return;
			}

			if (plugin.in_command.contains(sender)) { // this function is not really necessary, but safety first
				sender.sendMessage(colorize(plugin.config.getMessages().getString("on_active_command")));
				return;
			}

			plugin.in_command.add(sender);

			Player p = (Player) sender;
			
			try (ByteArrayOutputStream b = new ByteArrayOutputStream();
					DataOutputStream out = new DataOutputStream(b);) {

				out.writeUTF("MaSuiteTeleports");
				out.writeUTF("TeleportRequestHere");
				out.writeUTF(sender.getName());
				out.writeUTF(args[0]);
				p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());

			} catch (IOException e) {
				e.printStackTrace();
			}

			plugin.in_command.remove(sender);

		});
		return true;
	}
}