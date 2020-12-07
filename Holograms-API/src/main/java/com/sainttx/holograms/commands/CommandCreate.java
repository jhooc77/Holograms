package com.sainttx.holograms.commands;

import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.HologramPlugin;
import com.sainttx.holograms.api.line.HologramLine;
import com.sainttx.holograms.util.TextUtil;

import net.minestom.server.MinecraftServer;
import net.minestom.server.chat.ChatColor;
import net.minestom.server.chat.ColoredText;
import net.minestom.server.command.CommandProcessor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.time.TimeUnit;

public class CommandCreate implements CommandProcessor {

    private HologramPlugin plugin;

    public CommandCreate(HologramPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean process(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can create holograms.");
        } else if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /hologram create <name> <text>");
        } else {
            String hologramName = args[1];
            Hologram hologram = plugin.getHologramManager().getHologram(hologramName);

            if (hologram != null) {
                sender.sendMessage(ChatColor.RED + "A hologram with that name already exists.");
            } else {
                Player player = (Player) sender;
                ColoredText text = TextUtil.implode(2, args);
                Hologram holo = new Hologram(hologramName, player.getPosition().copy().add(0, 1, 0), true, player.getInstance());
                try {
                    HologramLine line = plugin.parseLine(holo, text.getMessage());
                    holo.addLine(line);
                } catch (Exception ex) {
                    sender.sendMessage(ChatColor.RED + "Error: " + ex.getMessage());
                    return true;
                }
                holo.spawn();
                plugin.getHologramManager().addHologram(holo);
                plugin.getHologramManager().saveHologram(holo);
                sender.sendMessage(ChatColor.BRIGHT_GREEN + "Created hologram " + holo.getId() + " with line \"" + text.getMessage() + ChatColor.BRIGHT_GREEN + "\"");
                
                MinecraftServer.getSchedulerManager().buildTask(() -> {
                	holo.despawn();
                	plugin.getLogger().info("despawned");
                }).delay(5L, TimeUnit.SECOND).schedule();

                MinecraftServer.getSchedulerManager().buildTask(() -> {
                	holo.spawn();
                	plugin.getLogger().info("respawned");
                }).delay(10L, TimeUnit.SECOND).schedule();
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