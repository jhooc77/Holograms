package com.sainttx.holograms.controller;

import com.sainttx.holograms.api.HologramEntityController;
import com.sainttx.holograms.api.HologramPlugin;
import com.sainttx.holograms.api.entity.HologramEntity;
import com.sainttx.holograms.api.entity.ItemHolder;
import com.sainttx.holograms.api.line.HologramLine;

import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.Position;

public class HologramEntityControllerImpl implements HologramEntityController {

    private HologramPlugin plugin;

    public HologramEntityControllerImpl(HologramPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
	public EntityNameable spawnNameable(HologramLine line, Position location, Instance instance) {
        EntityNameable armorStand = new EntityNameable(location, instance, line);
        armorStand.setPosition(location.getX(), location.getY(), location.getZ());
        if (!addEntityToWorld(instance, armorStand.getBukkitEntity())) {
            plugin.getLogger().info("Failed to spawn hologram entity in instance " + instance.getStorageLocation().getLocation()
                    + " at x:" + location.getX() + " y:" + location.getY() + " z:" + location.getZ());
        }
        return armorStand;
    }

    @Deprecated
	@Override
    public ItemHolder spawnItemHolder(HologramLine line, Position location, Instance instance) {
        return spawnItemHolder(line, location, new ItemStack(Material.AIR, (byte) 1), instance);
    }

    @Override
    public ItemHolder spawnItemHolder(HologramLine line, Position location, ItemStack itemstack, Instance instance) {
        EntityItemHolder item = new EntityItemHolder(location, instance, line, itemstack);
        if (!addEntityToWorld(instance, item)) {
            plugin.getLogger().info("Failed to spawn item entity in instance " + instance.getStorageLocation().getLocation()
                    + " at x:" + location.getX() + " y:" + location.getY() + " z:" + location.getZ());
        }
        EntityNameable armorStand = spawnNameable(line, location, instance);
        item.setMount(armorStand);
        return item;
    }

    private static boolean addEntityToWorld(Instance instance, Entity nmsEntity) {
    	Chunk chunk = instance.getChunkAt(nmsEntity.getPosition());

        if (!chunk.isLoaded()) {
            nmsEntity.remove();
            return false;
        }
        return true;
    }

    @Override
    public HologramEntity getHologramEntity(Entity bukkitEntity) {
        return bukkitEntity instanceof HologramEntity ? (HologramEntity) bukkitEntity : null;
    }
}
