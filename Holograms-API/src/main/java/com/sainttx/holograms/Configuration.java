package com.sainttx.holograms;

import java.io.File;

import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.Extension;
import net.minestom.server.storage.StorageLocation;

public class Configuration {

    /**
     * The name of the file that this configuration is stored in
     */
    protected String file;

    /**
     * The plugin that stores this configuration
     */
    protected Extension plugin;
    
    protected StorageLocation storageLocation;

    /**
     * Instantiates a new Configuration file
     *
     * @param plugin The plugin instantiating the file
     * @param file The name of the file being created as a .YML file
     */
    public Configuration(Extension plugin, String file) {
        this.plugin = plugin;
        this.file = file;
        this.createFile();
    }
    
    private File getDataFoler() {
		return new File(MinecraftServer.getExtensionManager().getExtensionFolder(), plugin.getDescription().getName());
    	
    }

    /**
     * Creates and loads the configuration
     */
    private void createFile() {
    	
		new File(getDataFoler() + "/" + file).mkdirs();
		storageLocation = MinecraftServer.getStorageManager().getLocation(getDataFoler() + "/" + file);
    }

    /**
     * Deletes the file from disk
     */
    public void deleteFile() {
        File file = new File(getDataFoler(), this.file);
        file.delete();
    }
    
    public StorageLocation getStorage() {
    	return storageLocation;
    }
}
