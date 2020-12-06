package com.sainttx.holograms.commands;

import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.HologramPlugin;
import com.sainttx.holograms.api.line.HologramLine;
import com.sainttx.holograms.api.line.TextualHologramLine;

import net.minestom.server.chat.ChatColor;
import net.minestom.server.command.CommandProcessor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;

public class CommandRemoveLine implements CommandProcessor {

    private HologramPlugin plugin;

    public CommandRemoveLine(HologramPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean process(CommandSender sender, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /hologram removeline <name> <index>");
        } else {
            String hologramName = args[1];
            Hologram hologram = plugin.getHologramManager().getHologram(hologramName);

            if (hologram == null) {
                sender.sendMessage(ChatColor.RED + "Hologram " + hologramName + " does not exist");
            } else {
                int index;
                try {
                    index = Integer.parseInt(args[2]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + args[2] + " is not a valid number");
                    return true;
                }

                if (index < 0 || index >= hologram.getLines().size()) {
                    sender.sendMessage(ChatColor.RED + "Index must be between 0 and " + (hologram.getLines().size() - 1));
                } else {
                    HologramLine line = hologram.getLine(index);
                    hologram.removeLine(line);
                    if (line instanceof TextualHologramLine) {
                        sender.sendMessage(ChatColor.BRIGHT_GREEN + "Removed line \"" + ((TextualHologramLine) line).getText().getMessage()
                                + ChatColor.BRIGHT_GREEN + "\" from hologram " + hologram.getId());
                    } else {
                        sender.sendMessage(ChatColor.BRIGHT_GREEN + "Removed line at position " + index
                                + " from hologram " + hologram.getId());
                    }

                    if (hologram.getLines().size() == 0) {
                        plugin.getHologramManager().deleteHologram(hologram);
                        sender.sendMessage(ChatColor.BRIGHT_GREEN + "Hologram " + hologram.getId() + " has no more lines and was deleted");
                    } else {
                        plugin.getHologramManager().saveHologram(hologram);
                    }
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