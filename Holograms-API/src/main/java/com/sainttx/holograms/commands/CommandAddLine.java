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

public class CommandAddLine implements CommandProcessor {

    private HologramPlugin plugin;

    public CommandAddLine(HologramPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean process(CommandSender sender, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /hologram addline <name> <text>");
        } else {
            String hologramName = args[1];
            Hologram hologram = plugin.getHologramManager().getHologram(hologramName);

            if (hologram == null) {
                sender.sendMessage(ChatColor.RED + "Hologram " + hologramName + " does not exist");
            } else {
                ColoredText text = TextUtil.implode(2, args);
                try {
                    HologramLine line = plugin.parseLine(hologram, text.getMessage());
                    hologram.addLine(line);
                    plugin.getHologramManager().saveHologram(hologram);
                } catch (Exception ex) {
                    sender.sendMessage(ChatColor.RED + "Error: " + ex.getMessage());
                    return true;
                }
                sender.sendMessage(ChatColor.BRIGHT_GREEN + "Added line \"" + text.getMessage() + ChatColor.BRIGHT_GREEN + "\" to hologram " + hologram.getId());
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
