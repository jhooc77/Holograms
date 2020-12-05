package com.sainttx.holograms.api;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;

import com.sainttx.holograms.HologramListener;
import com.sainttx.holograms.ManagerImpl;
import com.sainttx.holograms.api.line.HologramLine;
import com.sainttx.holograms.api.line.TextLine;
import com.sainttx.holograms.commands.HologramCommands;
import com.sainttx.holograms.controller.HologramEntityControllerImpl;
import com.sainttx.holograms.parser.AnimatedItemLineParser;
import com.sainttx.holograms.parser.AnimatedTextLineParser;
import com.sainttx.holograms.parser.ItemLineParser;
import com.sainttx.holograms.tasks.HologramUpdateTask;

import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.Extension;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.utils.time.TimeUnit;

public class HologramPlugin extends Extension {
	
    // Holds all registered line parsers
    private Set<HologramLine.Parser> parsers = new HashSet<>();

    /**
     * Parses and returns a {@link HologramLine} from a textual representation.
     * This method will return a {@link TextLine} if no valid parser is found.
     *
     * @param hologram the parent hologram
     * @param text the text
     * @return the created line
     */
    public HologramLine parseLine(Hologram hologram, String text) {
        Optional<HologramLine.Parser> parser = parsers.stream()
                .filter(p -> p.canParse(text))
                .findFirst();
        return parser.isPresent() ? parser.get().parse(hologram, text) : new TextLine(hologram, text);
    }

    /**
     * Registers a new line parser for creation/parsing of hologram lines.
     *
     * @param parser the parser
     * @return true if the parser was successfully added
     */
    public boolean addLineParser(HologramLine.Parser parser) {
        return parsers.add(parser);
    }
    private HologramManager manager;
    private HologramEntityController controller;
    private Runnable updateTask = new HologramUpdateTask(this);

    @Override
    public void initialize() {
        this.manager = new ManagerImpl(this);

        // Register parsers
        addLineParser(new ItemLineParser());
        addLineParser(new AnimatedItemLineParser());
        addLineParser(new AnimatedTextLineParser());
        
        MinecraftServer.getInstanceManager().getInstances().forEach(instance -> {
        	new HologramListener(instance, this);
        });
        MinecraftServer.getCommandManager().register(new HologramCommands(this));
        SchedulerManager schedulerManager = MinecraftServer.getSchedulerManager();
        schedulerManager.buildTask(() -> ((ManagerImpl) manager).load()).delay(5, TimeUnit.TICK).schedule();
        schedulerManager.buildTask(updateTask).delay(2, TimeUnit.TICK).repeat(2, TimeUnit.TICK).schedule();
        controller = (HologramEntityController)new HologramEntityControllerImpl(this);
    }

    @Override
    public void terminate() {
        manager.clear();
        this.manager = null;
        this.controller = null;
    }

    public HologramManager getHologramManager() {
        return manager;
    }

    public HologramEntityController getEntityController() {
        return controller;
    }
    
    public Logger getLogger() {
    	return super.getLogger();
    }
}
