package com.sainttx.holograms.commands;

import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.HologramPlugin;

import net.minestom.server.chat.ChatColor;
import net.minestom.server.command.CommandProcessor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;

public class CommandMoveHere implements CommandProcessor {

    private HologramPlugin plugin;

    public CommandMoveHere(HologramPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean process(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can move holograms.");
        } else if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /hologram movehere <name>");
        } else {
            String hologramName = args[1];
            Hologram hologram = plugin.getHologramManager().getHologram(hologramName);

            if (hologram == null) {
                sender.sendMessage(ChatColor.RED + "Hologram " + hologramName + " does not exist");
            } else {
                Player player = (Player) sender;
                hologram.despawn();
                hologram.teleport(player.getPosition().clone().add(0, 1, 0), player.getInstance());
                hologram.spawn();
                plugin.getHologramManager().saveHologram(hologram);
                sender.sendMessage(ChatColor.BRIGHT_GREEN + "Moved " + hologramName + " to your current location");
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