package com.sainttx.holograms.controller;

import com.sainttx.holograms.api.entity.HologramEntity;
import com.sainttx.holograms.api.entity.ItemHolder;
import com.sainttx.holograms.api.line.HologramLine;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.hologram.Hologram;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.Position;

public class EntityItemHolder extends ItemEntity implements ItemHolder {

    private HologramLine line;
    private ItemStack itemstack;

    public EntityItemHolder(Position location, Instance instance, HologramLine line, ItemStack itemstack) {
        super(itemstack, location, instance);
        this.line = line;
        this.itemstack = itemstack;
        this.setNoGravity(true);
        this.setPickable(false);
    }

    @Override
    public HologramLine getHologramLine() {
        return line;
    }

    @Override
    public void setPosition(float x, float y, float z) {
        super.getPosition().setX(x);
        super.getPosition().setY(y);
        super.getPosition().setZ(z);
    }

    @Override
    public void remove() {
        super.remove();
        if (getVehicle() != null) {
            getVehicle().remove();
        }
    }

    @Override
    public void setItem(ItemStack item) {
        super.setItemStack(item);
    }

    @Override
    public ItemStack getItem() {
        return itemstack;
    }

    @Override
    public HologramEntity getMount() {
        return (HologramEntity) getVehicle();
    }

    @Override
    public void setMount(HologramEntity entity) {
        if (entity instanceof Hologram) {
        	Hologram holo = (Hologram) entity;
        	((Entity)holo.getEntity()).addPassenger(this);
        }
    }

    // Overriden NMS methods

    @Override
    public Entity getBukkitEntity() {
        return this;
    }
}