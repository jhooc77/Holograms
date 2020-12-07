package com.sainttx.holograms.commands;

import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.HologramPlugin;
import com.sainttx.holograms.api.line.HologramLine;
import com.sainttx.holograms.util.TextUtil;

import net.minestom.server.chat.ChatColor;
import net.minestom.server.command.CommandProcessor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;

import java.util.Collection;

public class CommandInfo implements CommandProcessor {

    private HologramPlugin plugin;

    public CommandInfo(HologramPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean process(CommandSender sender,  String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /hologram info <name>");
        } else {
            String hologramName = args[1];
            Hologram hologram = plugin.getHologramManager().getHologram(hologramName);

            if (hologram == null) {
                sender.sendMessage(ChatColor.RED + "Hologram " + hologramName + " does not exist");
            } else {
                sender.sendMessage(ChatColor.BRIGHT_GREEN + "Hologram \"" + hologram.getId() + "\"" + (hologram.isHidden()?" §7§o(Hidden)":""));
                Collection<HologramLine> lines = hologram.getLines();
                sender.sendMessage(ChatColor.GRAY + "Location: " + ChatColor.WHITE + TextUtil.locationAsString(hologram.getLocation(), hologram.getInstance()));
                if (lines.isEmpty()) {
                    sender.sendMessage(ChatColor.GRAY + "Hologram has no lines.");
                } else {
                    sender.sendMessage(ChatColor.GRAY + "Lines:");
                    for (HologramLine line : lines) {
                        sender.sendMessage(" - \"" + line.getRaw() + ChatColor.WHITE + "\"");
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