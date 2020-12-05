package com.sainttx.holograms.api.entity;

import net.minestom.server.item.ItemStack;

public interface ItemHolder extends HologramEntity, MountedEntity {

    /**
     * Sets the item for this entity to display.
     *
     * @param item the new item
     */
    void setItem(ItemStack item);

    /**
     * Returns the current displayed item by this entity.
     *
     * @return the current item
     */
    ItemStack getItem();
}
