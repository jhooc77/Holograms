package com.sainttx.holograms.commands;

import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.HologramManager;
import com.sainttx.holograms.api.HologramPlugin;
import com.sainttx.holograms.util.TextUtil;

import net.minestom.server.chat.ChatColor;
import net.minestom.server.command.CommandProcessor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;


public class CommandList implements CommandProcessor {

    private HologramPlugin plugin;

    public CommandList(HologramPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean process(CommandSender sender, String label, String[] args) {
        HologramManager manager = plugin.getHologramManager();

        sender.sendMessage(ChatColor.BRIGHT_GREEN + "All holograms:");
        for (Hologram hologram : manager.getActiveHolograms().values()) {
            int lines = hologram.getLines().size();
            sender.sendMessage(" - \"" + hologram.getId() + "\" at " + TextUtil.locationAsString(hologram.getLocation(), hologram.getInstance()) + " (" + lines + " lines)");
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