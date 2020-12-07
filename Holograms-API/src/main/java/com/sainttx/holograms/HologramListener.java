package com.sainttx.holograms;

import java.util.Collection;

import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.HologramPlugin;
import com.sainttx.holograms.api.entity.HologramEntity;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.event.EventCallback;
import net.minestom.server.event.instance.AddEntityToInstanceEvent;
import net.minestom.server.event.instance.InstanceChunkLoadEvent;
import net.minestom.server.event.instance.InstanceChunkUnloadEvent;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.Position;
import net.minestom.server.utils.time.TimeUnit;


public class HologramListener {
	
    public class UnLoadEvent implements EventCallback<InstanceChunkUnloadEvent> {

		@Override
		public void run(InstanceChunkUnloadEvent event) {
	        Chunk chunk = event.getInstance().getChunk(event.getChunkX(), event.getChunkZ());
	        for (Entity entity : event.getInstance().getChunkEntities(chunk)) {
	            HologramEntity hologramEntity = extension.getEntityController().getHologramEntity(entity);
	            if (hologramEntity != null) {
	                hologramEntity.remove();
	            }
	        }
	        Collection<Hologram> holograms = extension.getHologramManager().getActiveHolograms().values();
	        for (Hologram holo : holograms) {
	            Position loc = holo.getLocation();
	            int chunkX = (int) Math.floor(loc.toBlockPosition().getX() / 16.0D);
	            int chunkZ = (int) Math.floor(loc.toBlockPosition().getZ() / 16.0D);
	            if (chunkX == chunk.getChunkX() && chunkZ == chunk.getChunkZ()) {
	                holo.despawn();
	            }
	        }

		}

	}

	public class LoadEvent implements EventCallback<InstanceChunkLoadEvent> {

		@Override
		public void run(InstanceChunkLoadEvent event) {
	        Chunk chunk = event.getInstance().getChunk(event.getChunkX(), event.getChunkZ());
	        if (chunk == null || !chunk.isLoaded()) {
	            return;
	        }

	        Collection<Hologram> holograms = extension.getHologramManager().getHolograms().values();
	        for (Hologram holo : holograms) {
	            int chunkX = (int) Math.floor(holo.getLocation().toBlockPosition().getX() / 16.0D);
	            int chunkZ = (int) Math.floor(holo.getLocation().toBlockPosition().getZ() / 16.0D);
	            if (chunkX == chunk.getChunkX() && chunkZ == chunk.getChunkZ()) {
	            	MinecraftServer.getSchedulerManager().buildTask(() -> {
	            		if (!holo.isHidden())
	            		holo.spawn();
	            	}).delay(10, TimeUnit.TICK).schedule();
	            }
	        }

		}

	}

	public class EntityEvent implements EventCallback<AddEntityToInstanceEvent> {

		@Override
		public void run(AddEntityToInstanceEvent event) {
			if (event.getEntity() instanceof EntityCreature) {
				if (event.isCancelled() && extension.getEntityController().getHologramEntity(event.getEntity()) != null) {
					event.setCancelled(false);
				}
			} else if (event.getEntity() instanceof ItemEntity) {
				if (event.isCancelled() && extension.getEntityController().getHologramEntity(event.getEntity()) != null) {
					event.setCancelled(false);
				}
			}

		}

	}

	private HologramPlugin extension;

	/**
     * Creates a HologramListener
     *
     * @param instance The running instance of the Holograms controller
     */
    public HologramListener(Instance instance, HologramPlugin extension) {
    	this.extension = extension;
        instance.addEventCallback(AddEntityToInstanceEvent.class, new EntityEvent());
        instance.addEventCallback(InstanceChunkLoadEvent.class, new LoadEvent());
        instance.addEventCallback(InstanceChunkUnloadEvent.class, new UnLoadEvent());
    }
}
