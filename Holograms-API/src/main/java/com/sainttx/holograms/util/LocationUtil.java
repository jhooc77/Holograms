package com.sainttx.holograms.util;

import org.apache.commons.lang3.Validate;

import com.extollit.tuple.Pair;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.Position;

public class LocationUtil {

    /**
     * Converts a Location into a String
     *
     * @param location A location to be converted
     * @return A string representation of the Location
     */
    public static String locationAsString(Position location, Instance instance) {
        return instance.getStorageLocation().getLocation() + ";" + location.getX() + ";" + location.getY()
                + ";" + location.getZ() + ";" + location.getPitch() + ";" + location.getYaw();
    }

    /**
     * Converts a String into a Location
     *
     * @param string A String to be converted
     * @return The Location represented by the String, null if an invalid String was provided or
     * the world specified is not loaded
     */
    public static Pair<Position, Instance> stringAsLocation(String string) {
        String[] parts = string.split(";");

        if (parts.length >= 4) { // At least world, x, y, z specified
            Instance world = null;
            for (Instance i : MinecraftServer.getInstanceManager().getInstances()){
            	if (i.getStorageLocation().getLocation().equals(parts[0])) {
            		world = i;
            	}
            }
            Validate.notNull(world, "Instance is not exist");
            if (world != null) {
                float x = Float.parseFloat(parts[1]);
                float y = Float.parseFloat(parts[2]);
                float z = Float.parseFloat(parts[3]);

                Position location = new Position(x, y, z);
                if (parts.length == 6) { // Has a pitch & yaw
                    float pitch = Float.parseFloat(parts[4]);
                    float yaw = Float.parseFloat(parts[5]);
                    location.setYaw(yaw);
                    location.setPitch(pitch);
                }

                return new Pair<Position, Instance>(location, world);
            }
        }

        return null;
    }
}
