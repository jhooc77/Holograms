package com.sainttx.holograms.api.exception;

/**
 * Thrown whenever a Hologram entity fails to spawn
 */
public class HologramEntitySpawnException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1895850132308845796L;

	public HologramEntitySpawnException() {
    }

    public HologramEntitySpawnException(String message) {
        super(message);
    }

    public HologramEntitySpawnException(String message, Throwable cause) {
        super(message, cause);
    }

    public HologramEntitySpawnException(Throwable cause) {
        super(cause);
    }
}
