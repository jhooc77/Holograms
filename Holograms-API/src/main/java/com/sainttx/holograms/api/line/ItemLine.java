package com.sainttx.holograms.api.line;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;

import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.HologramPlugin;
import com.sainttx.holograms.api.entity.ItemHolder;

import net.minestom.server.MinecraftServer;
import net.minestom.server.chat.ColoredText;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.ItemMeta;
import net.minestom.server.utils.Position;

public class ItemLine extends AbstractLine implements ItemCarryingHologramLine {

    private static final Pattern linePattern = Pattern.compile("((item|icon|itemstack):)(.+)");
    private ItemStack item;
    private ItemHolder entity;

    public ItemLine(Hologram parent, String raw) {
        this(parent, raw, parseItem(raw));
    }

    public ItemLine(Hologram parent, ItemStack item) {
        this(parent, "item:" + itemstackToRaw(item), item);
    }

    ItemLine(Hologram parent, String raw, ItemStack itemStack) {
        super(parent, raw);
        Validate.notNull(itemStack, "Item cannot be null");
        this.item = itemStack;
    }

    // Parses an itemstack from text
    private static ItemStack parseItem(String text) {
        Matcher matcher = linePattern.matcher(text);
        if (matcher.find()) {
            text = matcher.group(3);
        }
        String[] split = text.split(" ");
        short durability = -1;
        String data = split[0];

        if (data.contains(":")) {
            String[] datasplit = data.split(":");
            data = datasplit[0];
            durability = Short.parseShort(datasplit[1]);
        }

        Material material = Material.valueOf(data.toUpperCase());

        // Throw exception if the material provided was wrong
        if (material == null) {
            throw new IllegalArgumentException("Invalid material " + data);
        }

        int amount;
        try {
            amount = split.length == 1 ? 1 : Integer.parseInt(split[1]);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid amount \"" + split[1] + "\"", ex);
        }
        ItemStack item = new ItemStack(material, (byte) amount, (short) Math.max(0, durability));
        ItemMeta meta = item.getItemMeta();

        // No meta data was provided, we can return here
        if (split.length < 3) {
            return item;
        }

        // Go through all the item meta specified
        for (int i = 2 ; i < split.length ; i++) {
            String[] information = split[i].split(":");

            // Data, name, or lore has been specified
            switch (information[0].toLowerCase()) {
                case "name":
                    // Replace '_' with spaces
                    String name = information[1].replace(' ', ' ');
                    item.setDisplayName(ColoredText.ofLegacy(name, '&'));
                    break;
                case "lore":
                    // If lore was specified 2x for some reason, don't overwrite
                    ArrayList<ColoredText> lore = item.hasLore() ? item.getLore() : new ArrayList<>();
                    String[] loreLines = information[1].split("\\|");

                    // Format all the lines and add them as lore
                    for (String line : loreLines) {
                        line = line.replace('_', ' '); // Replace '_' with space
                        lore.add(ColoredText.ofLegacy(line, '&'));
                    }

                    item.setLore(lore);
                    break;
                case "data":
                    short dataValue = Short.parseShort(information[1]);
                    item.setDamage(dataValue);
                default:
                    // Try parsing enchantment if it was nothing else
                    Enchantment ench = Enchantment.valueOf(information[0].toUpperCase());
                    int level = Integer.parseInt(information[1]);

                    if (ench != null) {
                        item.setEnchantment(ench, (short) level);
                    } else {
                        throw new IllegalArgumentException("Invalid enchantment " + information[0]);
                    }
                    break;
            }
        }

        item.setItemMeta(meta);
        return item;
    }

    // Converts an ItemStack to raw representation
    static String itemstackToRaw(ItemStack itemstack) {
        StringBuilder sb = new StringBuilder();
        sb.append(itemstack.getMaterial().toString()); // append type
        sb.append(':').append(itemstack.getDamage()); // append durability
        sb.append(' ').append(itemstack.getAmount()); // append amount

        // append name
        if (itemstack.hasDisplayName()) {
            sb.append(' ').append("name:").append(itemstack.getDisplayName().getMessage().replace(' ', '_'));
        }

        // append lore
        if (itemstack.hasLore()) {
            sb.append(' ').append("lore:");
            Iterator<String> iterator = itemstack.getLore().stream().map(text -> text.getMessage()).collect(Collectors.toList()).iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next().replace(' ', '_'));
                if (iterator.hasNext()) {
                    sb.append('|');
                }
            }
        }

            // append enchantments
            itemstack.getEnchantmentMap().forEach((ench, level) -> {
                sb.append(' ').append(ench.toString()).append(':').append(level);
            });

        return sb.toString();
    }

    @Override
    public void setLocation(Position location, Instance instance) {
        super.setLocation(location, instance);
        if (entity != null) {
            entity.setPosition(location.getX(), location.getY(), location.getZ());
        }
    }

    @Override
    public void hide() {
        if (!isHidden()) {
            entity.setMount(null);
            entity.remove();
            entity = null;
        }
    }

    @Override
    public boolean show() {
        if (isHidden()) {
            HologramPlugin plugin = (HologramPlugin) MinecraftServer.getExtensionManager().getExtension("HologramsMinestom");
            entity = plugin.getEntityController().spawnItemHolder(this, getLocation(), item, getInstance());
        }
        return true;
    }

    @Override
    public boolean isHidden() {
        return entity == null;
    }

    @Override
    public ItemStack getItem() {
        return item.copy();
    }

    @Override
    public void setItem(ItemStack item) {
        this.item = item.copy();
        entity.setItem(this.item);
    }

    @Override
    public double getHeight() {
        return 0.5;
    }
}
