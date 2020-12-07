package com.sainttx.holograms.commands;

import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.HologramPlugin;

import net.minestom.server.chat.ChatColor;
import net.minestom.server.command.CommandProcessor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;

public class CommandHide implements CommandProcessor {

	private HologramPlugin plugin;

	public CommandHide(HologramPlugin plugin) {
		this.plugin = plugin;
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
	public boolean process(CommandSender sender, String command, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /hologram hide <name>");
        } else {
            String hologramName = args[1];
            Hologram hologram = plugin.getHologramManager().getHologram(hologramName);

            if (hologram == null) {
                sender.sendMessage(ChatColor.RED + "Hologram " + hologramName + " does not exist");
            } else {
            	if (!hologram.isHidden()) {
            		sender.sendMessage(ChatColor.BRIGHT_GREEN + "Hide hologram " + hologram.getId());
            		hologram.hide();
            	} else {
            		sender.sendMessage(ChatColor.BRIGHT_GREEN + "Reveal hologram " + hologram.getId());
            		hologram.reveal();
            	}
            }
        }

        return true;
	}

	@Override
	public boolean hasAccess(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

}
