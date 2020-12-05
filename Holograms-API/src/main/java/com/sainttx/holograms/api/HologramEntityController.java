package com.sainttx.holograms.api;

import com.sainttx.holograms.api.entity.HologramEntity;
import com.sainttx.holograms.api.entity.ItemHolder;
import com.sainttx.holograms.api.entity.Nameable;
import com.sainttx.holograms.api.line.HologramLine;

import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.Position;

public interface HologramEntityController {

    /**
     * Spawns a new entity at a specific location for a HologramLine
     * to modify displayed text.
     *
     * @param line the parenting hologram line for the entity
     * @param location the location
     * @return the resulting entity that was spawned
     */
    Nameable spawnNameable(HologramLine line, Position location, Instance instance);

    /**
     * Spawns a new entity at a specific location for a HologramLine
     * to modify displayed item.
     *
     * @param line the parenting hologram line for the entity
     * @param location the location
     * @return the resulting entity that was spawned
     * @deprecated superseded by {@link #spawnItemHolder(HologramLine, Location, ItemStack)}
     */
    @Deprecated
    ItemHolder spawnItemHolder(HologramLine line, Position location, Instance instance);

    /**
     * Spawns a new entity at a specific location for a HologramLine
     * to modify displayed item.
     *
     * @param line the parenting hologram line for the entity
     * @param location the location
     * @param itemstack initial item
     * @return the resulting entity that was spawned
     */
    ItemHolder spawnItemHolder(HologramLine line, Position location, ItemStack itemstack, Instance instance);

    /**
     * Returns the {@link HologramEntity} of a hologram entity. If the
     * entity is not a Hologram <code>null</code> is returned.
     *
     * @param bukkitEntity the Bukkit entity
     * @return the base entity
     */
    HologramEntity getHologramEntity(Entity bukkitEntity);
}
