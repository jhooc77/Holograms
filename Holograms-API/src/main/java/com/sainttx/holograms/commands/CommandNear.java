package com.sainttx.holograms.commands;

import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.HologramManager;
import com.sainttx.holograms.api.HologramPlugin;
import com.sainttx.holograms.util.TextUtil;

import net.minestom.server.chat.ChatColor;
import net.minestom.server.command.CommandProcessor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CommandNear implements CommandProcessor {

    private HologramPlugin plugin;

    public CommandNear(HologramPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean process(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can view nearby holograms, use /hologram list instead.");
        } else if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /hologram near <radius>");
        } else {
            Double radius = null;

            try {
                radius = Double.parseDouble(args[1]);
            } catch (NumberFormatException ex) { /* Handled later */ }

            if (radius == null || Double.isInfinite(radius) || Double.isNaN(radius) || radius < 0) {
                sender.sendMessage(ChatColor.RED + "Please enter a valid number as your radius.");
                return true;
            }

            Player player = (Player) sender;
            HologramManager manager = plugin.getHologramManager();
            Map<Hologram, Float> nearby = new HashMap<>();
            for (Hologram hologram : manager.getHolograms().values()) {
                if (hologram.getInstance().equals(player.getInstance())) {
                    float distance = hologram.getLocation().getDistance(player.getPosition());
                    if (distance <= radius) {
                        nearby.put(hologram, distance);
                    }
                }
            }

            if (nearby.isEmpty()) {
                sender.sendMessage(ChatColor.RED + "There are no nearby holograms within radius " + radius);
            } else {
                sender.sendMessage(ChatColor.BRIGHT_GREEN + "Holograms within " + radius + " blocks:");
                for (Map.Entry<Hologram, Float> near : nearby.entrySet()) {
                    Hologram holo = near.getKey();
                    sender.sendMessage(" - \"" + holo.getId() + "\" at " + TextUtil.locationAsString(holo.getLocation(), holo.getInstance()) + " (" + TextUtil.formatDouble(near.getValue()) + " blocks away)" + (holo.isHidden()?" §7§o(Hidden)":""));
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