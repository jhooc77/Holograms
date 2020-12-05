package com.sainttx.holograms.api.entity;

import net.minestom.server.chat.ColoredText;

public interface Nameable extends HologramEntity {

    /**
     * Sets the display name for this entity.
     *
     * @param text the new text
     */
    void setName(ColoredText text);

    /**
     * Returns the current display name for this entity.
     *
     * @return the current text.
     */
    ColoredText getName();

}
