package com.sainttx.holograms.commands;

import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.HologramPlugin;
import com.sainttx.holograms.api.line.HologramLine;
import com.sainttx.holograms.util.TextUtil;

import net.minestom.server.chat.ChatColor;
import net.minestom.server.chat.ColoredText;
import net.minestom.server.command.CommandProcessor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;

public class CommandSetLine implements CommandProcessor {

    private HologramPlugin plugin;

    public CommandSetLine(HologramPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean process(CommandSender sender, String label, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(ChatColor.RED + "Usage: /hologram setline <name> <index> <text>");
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
                    sender.sendMessage(ChatColor.RED + "Index must be between 0 and  " + (hologram.getLines().size() - 1));
                } else {
                    HologramLine line = hologram.getLine(index);
                    ColoredText text = TextUtil.implode(3, args);
                    hologram.removeLine(line);
                    try {
                        HologramLine settingLine = plugin.parseLine(hologram, text.getMessage());
                        hologram.addLine(settingLine, index);
                    } catch (Exception ex) {
                        sender.sendMessage(ChatColor.RED + "Error: " + ex.getMessage());
                        return true;
                    }
                    plugin.getHologramManager().saveHologram(hologram);
                    sender.sendMessage(ChatColor.BRIGHT_GREEN + "Updated line at position " + index + " of hologram " + hologram.getId());
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