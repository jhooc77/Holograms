package com.sainttx.holograms.controller;

import com.sainttx.holograms.api.entity.Nameable;
import com.sainttx.holograms.api.line.HologramLine;

import net.minestom.server.chat.ColoredText;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.hologram.Hologram;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.Position;

public class EntityNameable extends Hologram implements Nameable {

    private HologramLine parentPiece;

    public EntityNameable(Position location, Instance instance, HologramLine parentPiece) {
        super(instance, location, null, true);
        this.parentPiece = parentPiece;
    }

    @Override
    public void setName(ColoredText text) {
    	if (text!=null)
        super.setText(text);
    }

    @Override
    public ColoredText getName() {
        return super.getText();
    }

    @Override
    public void setPosition(float x, float y, float z) {
        super.setPosition(new Position(x, y, z));
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
	public Entity getBukkitEntity() {
		return this.getEntity();
	}
}
