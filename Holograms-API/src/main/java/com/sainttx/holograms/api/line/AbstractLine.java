package com.sainttx.holograms.api.line;

import org.apache.commons.lang3.Validate;

import com.sainttx.holograms.api.Hologram;

import net.minestom.server.instance.Instance;
import net.minestom.server.utils.Position;

public abstract class AbstractLine implements HologramLine {

    private Hologram parent;
    private Position location;
    private String raw;
    private Instance instance;

    public AbstractLine(Hologram parent, String raw) {
        Validate.notNull(parent, "Parent hologram cannot be null");
        Validate.notNull(raw, "Raw representation cannot be null");
        this.parent = parent;
        this.raw = raw;
        this.location = parent.getLocation();
        this.setInstance(parent.getInstance());
    }
    
    protected void setRaw(String raw) {
        Validate.notNull(raw, "Raw representation cannot be null");
        this.raw = raw;
    }

    @Override
    public void setLocation(Position location, Instance instance) {
        this.location = location.copy();
        this.instance = instance;
    }

    @Override
    public Position getLocation() {
        return location.copy();
    }

    @Override
    public double getHeight() {
        return 0d;
    }

    @Override
    public final Hologram getHologram() {
        return parent;
    }

    @Override
    public final String getRaw() {
        return raw;
    }
    
	@Override
	public Instance getInstance() {
		return instance;
	}
    
    @Override
	public void setInstance(Instance instance) {
		this.instance = instance;
	}
}
