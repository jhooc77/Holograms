package com.sainttx.holograms.api.entity;

import com.sainttx.holograms.api.line.HologramLine;

import net.minestom.server.entity.Entity;

public interface HologramEntity {

    /**
     * Returns the parenting {@link HologramLine} of this base.
     *
     * @return the base line
     */
    HologramLine getHologramLine();

    /**
     * Permanently removes this entity.
     */
    void remove();

    /**
     * Gets the Bukkit entity for this hologram line.
     *
     * @return the entity
     */
    Entity getBukkitEntity();

    /**
     * Set the x/y/z coordinates of the entity.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    void setPosition(float x, float y, float z);
}
