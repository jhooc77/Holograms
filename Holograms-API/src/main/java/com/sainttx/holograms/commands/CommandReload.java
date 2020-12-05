package com.sainttx.holograms.commands;

import com.sainttx.holograms.api.HologramPlugin;

import net.minestom.server.chat.ChatColor;
import net.minestom.server.command.CommandProcessor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;

public class CommandReload implements CommandProcessor {

    private HologramPlugin plugin;

    public CommandReload(HologramPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean process(CommandSender sender, String label, String[] args) {
        plugin.getHologramManager().reload();
        sender.sendMessage(ChatColor.BRIGHT_GREEN + "Reloaded all holograms");
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