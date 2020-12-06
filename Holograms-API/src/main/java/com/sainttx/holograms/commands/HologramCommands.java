package com.sainttx.holograms.commands;

import java.util.HashMap;
import java.util.Map;

import com.sainttx.holograms.api.HologramPlugin;

import net.minestom.server.chat.ChatColor;
import net.minestom.server.command.CommandProcessor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;

public class HologramCommands implements CommandProcessor {

    /*
     * The hologram plugin instance
     */
    private HologramPlugin plugin;

    /*
     * A map containing all commands for the Hologram plugin
     */
    private Map<String, CommandProcessor> commands = new HashMap<String, CommandProcessor>();

    /**
     * Instantiates the Hologram command controller
     *
     * @param plugin The Hologram plugin instance
     */
    public HologramCommands(HologramPlugin plugin) {
        this.plugin = plugin;
        commands.put("addline", new CommandAddLine(plugin));
        commands.put("create", new CommandCreate(plugin));
        commands.put("delete", new CommandDelete(plugin));
        commands.put("info", new CommandInfo(plugin));
        commands.put("insertline", new CommandInsertLine(plugin));
        commands.put("list", new CommandList(plugin));
        commands.put("move", new CommandMove(plugin));
        commands.put("movehere", new CommandMoveHere(plugin));
        commands.put("near", new CommandNear(plugin));
        commands.put("removeline", new CommandRemoveLine(plugin));
        commands.put("reload", new CommandReload(plugin));
        commands.put("setline", new CommandSetLine(plugin));
        CommandTeleport teleportCommand = new CommandTeleport(plugin);
        commands.put("teleport", teleportCommand);
        commands.put("tp", teleportCommand);
    }

    @Override
    public boolean process(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            sendMenu(sender);
        } else {
            String subCommand = args[0].toLowerCase();
            CommandProcessor subCommandExec = commands.get(subCommand);

            if (subCommandExec == null) {
                sendMenu(sender);
            } else {
                return subCommandExec.process(sender, label, args);
            }
        }

        return false;
    }

    /*
     * Sends the command menu to a command sender
     */
    private void sendMenu(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY + "------[ " + ChatColor.WHITE + "Holograms v" + plugin.getDescription().getVersion() + " - BabayMandarinDuck " + ChatColor.GRAY + "]------");
        sender.sendMessage(ChatColor.YELLOW + "/holograms addline " + ChatColor.WHITE + "<name> <text>");
        sender.sendMessage(ChatColor.YELLOW + "/holograms create " + ChatColor.WHITE + "<name> <text>");
        sender.sendMessage(ChatColor.YELLOW + "/holograms delete " + ChatColor.WHITE + "<name>");
        sender.sendMessage(ChatColor.YELLOW + "/holograms info " + ChatColor.WHITE + "<name> <text>");
        sender.sendMessage(ChatColor.YELLOW + "/holograms insertline " + ChatColor.WHITE + "<name> <index> <text>");
        sender.sendMessage(ChatColor.YELLOW + "/holograms list");
        sender.sendMessage(ChatColor.YELLOW + "/holograms move " + ChatColor.WHITE + "<name> <instance> <x> <y> <z>");
        sender.sendMessage(ChatColor.YELLOW + "/holograms move " + ChatColor.WHITE + "<name> <x> <y> <z>");
        sender.sendMessage(ChatColor.YELLOW + "/holograms movehere " + ChatColor.WHITE + "<name>");
        sender.sendMessage(ChatColor.YELLOW + "/holograms near " + ChatColor.WHITE + "<radius>");
        sender.sendMessage(ChatColor.YELLOW + "/holograms removeline " + ChatColor.WHITE + "<name> <index>");
        sender.sendMessage(ChatColor.YELLOW + "/holograms reload");
        sender.sendMessage(ChatColor.YELLOW + "/holograms setline " + ChatColor.WHITE + "<name> <index> <text>");
        sender.sendMessage(ChatColor.YELLOW + "/holograms teleport " + ChatColor.WHITE + "<name>");
    }

	@Override
	public String getCommandName() {
		// TODO Auto-generated method stub
		return "hologram";
	}

	@Override
	public String[] getAliases() {
		// TODO Auto-generated method stub
		return new String[]{"hd"};
	}

	@Override
	public boolean hasAccess(Player player) {
		// TODO Auto-generated method stub
		return true;
	}
}
