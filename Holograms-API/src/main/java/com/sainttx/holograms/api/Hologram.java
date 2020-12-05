package com.sainttx.holograms.api;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.sainttx.holograms.api.line.HologramLine;
import com.sainttx.holograms.api.line.ItemLine;
import com.sainttx.holograms.api.line.UpdatingHologramLine;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.Position;

public class Hologram {

    private final HologramPlugin plugin;
    private final String id;
    private Position location;
    private boolean persist;
    private boolean spawned;
    private List<HologramLine> lines = new ArrayList<>();
    private Instance instance;

    @ConstructorProperties({ "id", "location" })
    public Hologram(String id, Position location, Instance instance) {
        this(id, location, false, instance);
    }

    @ConstructorProperties({ "id", "location", "persist" })
    public Hologram(String id, Position location2, boolean persist, Instance instance) {
        Validate.notNull(id, "Hologram id cannot be null");
        Validate.notNull(location2, "Hologram location cannot be null");
        Validate.notNull(instance, "Instance cannot be null");
        this.plugin = (HologramPlugin) MinecraftServer.getExtensionManager().getExtension("HologramsMinestom");
        this.id = id;
        this.location = location2;
        this.persist = persist;
        this.instance = instance;
    }
    
    // Internal method to save hologram if persistent state has been set
    private void save() {
        if (isPersistent()) {
            plugin.getHologramManager().saveHologram(this);
        }
    }
    public Instance getInstance() {
    	return this.instance;
    }
    /**
     * Returns the unique ID id of this Hologram.
     *
     * @return the holograms id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Returns whether the Hologram is currently spawned (ie. visible).
     *
     * @return true if spawned, false otherwise
     */
    public boolean isSpawned() {
        return spawned;
    }

    /**
     * Returns the persistence state of this Hologram.
     *
     * @return <tt>true</tt> if the Hologram is persistent
     */
    public boolean isPersistent() {
        return persist;
    }

    /**
     * Sets the persistence state of this Hologram.
     *
     * @param persist the new state
     */
    public void setPersistent(boolean persist) {
        this.persist = persist;
    }

    /**
     * Returns the location of this Hologram.
     *
     * @return the holograms location
     */
    public Position getLocation() {
        return this.location.copy();
    }

    /**
     * Returns the lines contained by this Hologram.
     *
     * @return all lines in the hologram
     */
    public List<HologramLine> getLines() {
        return lines;
    }

    /**
     * Adds a new {@link HologramLine} to this Hologram.
     *
     * @param line the line
     */
    public void addLine(HologramLine line) {
        addLine(line, lines.size());
    }

    /**
     * Inserts a new {@link HologramLine} to this Hologram at a specific index.
     *
     * @param line the line
     * @param index the index to add the line at
     */
    public void addLine(HologramLine line, int index) {
        lines.add(index, line);
        save();
        if (this.spawned) {
            spawn();
        }
        if (line instanceof UpdatingHologramLine) { // Track updating line
            plugin.getHologramManager().trackLine(((UpdatingHologramLine) line));
        }
    }

    /**
     * Removes a {@link HologramLine} from this Hologram and de-spawns it.
     *
     * @param line the line
     * @throws IllegalArgumentException if the line is not part of this hologram
     */
    public void removeLine(HologramLine line) {
        lines.remove(line);
        save();
        line.hide();
        if (line instanceof UpdatingHologramLine) { // Remove tracked line
            plugin.getHologramManager().untrackLine(((UpdatingHologramLine) line));
        }
    }

    /**
     * Returns a {@link HologramLine} at a specific index.
     *
     * @param index the index
     */
    public HologramLine getLine(int index) {
        if (index < 0 || index >= lines.size()) {
            return null;
        }
        return lines.get(index);
    }

    /**
     * Returns whether the Hologram is in a loaded chunk.
     *
     * @return true if chunk is loaded, false otherwise
     */
    public boolean isChunkLoaded(Instance instance) {
        Position location = getLocation();
        int chunkX = (int) Math.floor(location.toBlockPosition().getX() / 16.0D);
        int chunkZ = (int) Math.floor(location.toBlockPosition().getZ() / 16.0D);
        return instance.isChunkLoaded(chunkX, chunkZ);
    }

    // Reorganizes holograms after an initial index
    private void reorganize() {
        Position location = getLocation();
        float y = location.getY();
        for (int i = 0 ; i < lines.size() ; i++) {
            HologramLine line = getLine(i);
            if (line instanceof ItemLine) {
                y -= 0.2;
            }
            Position lineLocation = new Position(location.getX(), y, location.getZ());
            y -= line.getHeight();
            y -= HologramLine.SPACE_BETWEEN_LINES;
            line.setLocation(lineLocation, instance);
        }
    }

    /**
     * De-spawns all of the lines in this Hologram.
     */
    public void despawn() {
        getLines().forEach(HologramLine::hide);
        this.spawned = false;
    }

    /**
     * Spawns all of the lines in this Hologram.
     */
    public void spawn() {
        if (this.isSpawned()) {
            despawn();
        }
        if (isChunkLoaded(instance)) {
            reorganize();
            getLines().forEach(HologramLine::show);
            this.spawned = true;
        }
    }

    /**
     * Teleports this Hologram to a new {@link Location}.
     *
     * @param location the location
     */
    public void teleport(Position location) {
        if (!this.location.equals(location)) {
            this.location = location.copy();
            save();
            if (isSpawned()) {
                spawn();
            }
        }
    }
    public void teleport(Position location, Instance instance) {
        if (!this.location.equals(location)) {
            this.location = location.copy();
            this.instance = instance;
            save();
            if (isSpawned()) {
                spawn();
            }
        }
    }
}
