package com.sainttx.holograms.api.line;

import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.HologramPlugin;
import com.sainttx.holograms.api.entity.Nameable;

import net.minestom.server.MinecraftServer;
import net.minestom.server.chat.ColoredText;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.Position;

public class TextLine extends AbstractLine implements TextualHologramLine {

    private ColoredText text;
    private Nameable nameable;

    public TextLine(Hologram parent, String text) {
        this(parent, text, text);
    }

    TextLine(Hologram parent, String raw, String text) {
        super(parent, raw);
        this.text = ColoredText.ofLegacy(text, '&');
    }

    @Override
    public void setLocation(Position location, Instance instance) {
        super.setLocation(location, instance);
        if (!isHidden()) {
            nameable.getBukkitEntity().teleport(getLocation());
        }
    }

    @Override
    public void hide() {
        if (!isHidden()) {
            nameable.remove();
            nameable = null;
        }
    }

    @Override
    public boolean show() {
        if (isHidden()) {
            HologramPlugin plugin = (HologramPlugin) MinecraftServer.getExtensionManager().getExtension("HologramsMinestom");
            nameable = plugin.getEntityController().spawnNameable(this, getLocation(), getInstance());
            nameable.setName(text);
        }
        return true;
    }

    @Override
    public boolean isHidden() {
        return nameable == null;
    }

    @Override
    public ColoredText getText() {
        return this.text;
    }

    @Override
    public void setText(ColoredText text) {
        this.text = text;
        setRaw(text.getMessage());
        nameable.setName(text);
    }

    @Override
    public double getHeight() {
        return 0.23;
    }
}
