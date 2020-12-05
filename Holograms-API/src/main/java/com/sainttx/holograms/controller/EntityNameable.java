package com.sainttx.holograms.controller;

import com.sainttx.holograms.api.entity.Nameable;
import com.sainttx.holograms.api.line.HologramLine;

import net.minestom.server.chat.ColoredText;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.type.decoration.EntityArmorStand;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.Position;

public class EntityNameable extends EntityArmorStand implements Nameable {

    private HologramLine parentPiece;

    public EntityNameable(Position location, Instance instance, HologramLine parentPiece) {
        super(location);
        setInvisible(true);
        setSmall(true);
        setHasArms(true);
        setNoGravity(true);
        setNoBasePlate(false);
        setMarker(true);
        setInstance(instance);
        this.parentPiece = parentPiece;
    }

    @Override
    public void setName(ColoredText text) {
    	if (text!=null)
        super.setCustomName(text);
        super.setCustomNameVisible(text!=null);
    }

    @Override
    public ColoredText getName() {
        return super.getCustomName();
    }

    @Override
    public void setPosition(float x, float y, float z) {
        super.getPosition().setX(x);
        super.getPosition().setY(y);
        super.getPosition().setZ(z);
    }

    @Override
    public HologramLine getHologramLine() {
        return parentPiece;
    }

    @Override
    public void remove() {
        super.remove();
    }

    @Override
    public void setInvisible(boolean flag) {
        super.setInvisible(true);
    }

	@Override
	public Entity getBukkitEntity() {
		return this;
	}
}
