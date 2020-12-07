package com.sainttx.holograms;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.extollit.tuple.Pair;
import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.HologramManager;
import com.sainttx.holograms.api.HologramPlugin;
import com.sainttx.holograms.api.exception.HologramEntitySpawnException;
import com.sainttx.holograms.api.line.HologramLine;
import com.sainttx.holograms.api.line.UpdatingHologramLine;
import com.sainttx.holograms.util.LocationUtil;

import net.minestom.server.instance.Instance;
import net.minestom.server.storage.StorageLocation;
import net.minestom.server.utils.Position;

public class ManagerImpl implements HologramManager {

    private HologramPlugin plugin;
    private Configuration persistingHolograms;
    private StorageLocation storageLocation;
    private Map<String, Hologram> Holograms = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private Set<UpdatingHologramLine> trackedUpdatingLines = new HashSet<>();

    public ManagerImpl(HologramPlugin plugin) {
        this.plugin = plugin;
        this.reloadConfiguration();
    }

    /* Re-reads the holograms.yml file into memory */
    private void reloadConfiguration() {
        this.persistingHolograms = new Configuration(plugin, "holograms");
        this.storageLocation = persistingHolograms.getStorage();
    }

    @Override
    public void reload() {
        clear();
        reloadConfiguration();
        load();
    }

    /**
     * Loads all saved Holograms
     */
    public void load() {
        if (persistingHolograms == null || storageLocation == null) {
            this.reloadConfiguration();
        }

        // Load all the holograms
        if (storageLocation.get("holograms", String[].class) != null) {
        	int i = 0;
            loadHolograms:
            for (String hologramName : storageLocation.get("holograms", String[].class)) {
                List<String> uncoloredLines = Arrays.asList(storageLocation.get("holograms." + hologramName + ".lines", String[].class));
                Pair<Position, Instance> locationd = LocationUtil.stringAsLocation(storageLocation.get("holograms." + hologramName + ".location", String.class));
                Position location = locationd.left;
                if (location == null) {
                    plugin.getLogger().info("Hologram \"" + hologramName + "\" has an invalid location");
                    continue;
                }
                Instance instance = locationd.right;
				// Create the Hologram
                Hologram hologram = new Hologram(hologramName, location, true, instance);
                if (storageLocation.get("holograms." + hologramName + ".hide", Boolean.class)) {
                	hologram.hide();
                	i++;
                }
                // Add the lines
                for (String string : uncoloredLines) {
                    HologramLine line = plugin.parseLine(hologram, string);
                    try {
                        hologram.addLine(line);
                    } catch (HologramEntitySpawnException e) {
                        plugin.getLogger().info("Failed to spawn Hologram \"" + hologramName + "\"", e);
                        continue loadHolograms;
                    }
                }
                if (!hologram.isHidden()) {
                	hologram.spawn();
                }
                addHologram(hologram);
            }
        	plugin.getLogger().info("Loaded \"" + Holograms.size() + "\" holograms with \"" + i + "\" inactive holograms");
        } else {
            plugin.getLogger().info("holograms storage file doesn't exist, no holograms loaded");
        }
    }

    @Override
    public void saveHologram(Hologram hologram) {
        String hologramName = hologram.getId();
        Collection<HologramLine> holoLines = hologram.getLines();
        List<String> uncoloredLines = holoLines.stream()
                .map(HologramLine::getRaw)
                .collect(Collectors.toList());
        storageLocation.set("holograms." + hologramName + ".location", LocationUtil.locationAsString(hologram.getLocation(), hologram.getInstance()), String.class);
        storageLocation.set("holograms." + hologramName + ".lines", uncoloredLines.toArray(new String[uncoloredLines.size()]), String[].class);
        storageLocation.set("holograms." + hologramName + ".hide", hologram.isHidden(), Boolean.class);
        storageLocation.set("holograms", Holograms.keySet().toArray(new String[Holograms.size()]), String[].class);
    }

    @Override
    public void deleteHologram(Hologram hologram) {
        hologram.despawn();
        Holograms.remove(hologram.getId());
        storageLocation.delete("holograms." + hologram.getId() + ".location");
        storageLocation.delete("holograms." + hologram.getId() + ".lines");
        storageLocation.delete("holograms." + hologram.getId() + ".hide");
        storageLocation.set("holograms", Holograms.keySet().toArray(new String[Holograms.size()]), String[].class);
    }

    @Override
    public Hologram getHologram(String name) {
        return Holograms.get(name);
    }

    @Override
    public Map<String, Hologram> getActiveHolograms() {
    	Map<String, Hologram> activeHolograms = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    	Holograms.forEach((t, h) -> {
    		if (!h.isHidden()) activeHolograms.put(t, h);
    	});
        return activeHolograms;
    }

    @Override
    public void addHologram(Hologram hologram) {
        Holograms.put(hologram.getId(), hologram);
    }

    @Override
    public void trackLine(UpdatingHologramLine line) {
        trackedUpdatingLines.add(line);
    }

    @Override
    public boolean untrackLine(UpdatingHologramLine line) {
        return trackedUpdatingLines.remove(line);
    }

    @Override
    public Collection<? extends UpdatingHologramLine> getTrackedLines() {
        return trackedUpdatingLines;
    }

    @Override
    public void clear() {
        getHolograms().values().forEach(Hologram::despawn);
        getHolograms().clear();
    }

	@Override
	public Map<String, Hologram> getHolograms() {
		return Holograms;
	}
}
