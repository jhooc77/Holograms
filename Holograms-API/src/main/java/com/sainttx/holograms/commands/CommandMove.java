package com.sainttx.holograms.commands;

import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.HologramPlugin;

import net.minestom.server.chat.ChatColor;
import net.minestom.server.command.CommandProcessor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.Position;

public class CommandMove implements CommandProcessor {

    private HologramPlugin plugin;

    public CommandMove(HologramPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean process(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can move holograms.");
        } else if (args.length < 6) {
            sender.sendMessage(ChatColor.RED + "Usage: /hologram move <name> <x> <y> <z>");
        } else {
            String hologramName = args[1];
            Hologram hologram = plugin.getHologramManager().getHologram(hologramName);

            if (hologram == null) {
                sender.sendMessage(ChatColor.RED + "Hologram " + hologramName + " does not exist");
            } else {
                try {
                    float x = Float.parseFloat(args[2]);
                    float y = Float.parseFloat(args[3]);
                    float z = Float.parseFloat(args[4]);
                    Position location = new Position(x, y, z);
                    hologram.despawn();
                    hologram.teleport(location);
                    hologram.spawn();
                    plugin.getHologramManager().saveHologram(hologram);
                    sender.sendMessage(ChatColor.BRIGHT_GREEN + "Moved " + hologramName + " to "
                            + x + "/" + y + "/" + z);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "One of your x/y/z coordinates was invalid");
                }
            }
        }

        return true;
    }

	@Override
	public String getCommandName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getAliases() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAccess(Player player) {
		// TODO Auto-generated method stub
		return false;
	}
}