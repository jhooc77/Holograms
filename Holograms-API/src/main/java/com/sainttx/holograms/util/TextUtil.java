package com.sainttx.holograms.util;

import java.text.NumberFormat;
import java.util.ArrayList;

import net.minestom.server.chat.ColoredText;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.ItemMeta;
import net.minestom.server.utils.Position;

public class TextUtil {

    /**
     * Returns a color-formatted String of the input
     *
     * @param text The input string
     * @return A formatted String
     */
    public static ColoredText color(String text) {
        return ColoredText.ofLegacy(text, '&');
    }

    /**
     * Converts a Location into a String
     *
     * @param location A location to be converted
     * @return A string representation of the Location
     */
    public static String locationAsString(Position location, Instance instance) {
        return instance.getStorageLocation().getLocation() + " (x/y/z: " + formatDouble(location.getX()) + "/" + formatDouble(location.getY())
                + "/" + formatDouble(location.getZ()) + ")";
    }

    /**
     * Returns a formatted String representation of a double, rounded to 2
     * decimal places
     *
     * @param value The double to be formatted
     * @return A formatted string of the double
     */
    public static String formatDouble(double value) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(value);
    }

    /**
     * Parses an ItemStack from a string of text.
     *
     * @param text the text
     * @return the item
     * @throws NumberFormatException when an invalid amount or durability are provided
     * @throws IllegalArgumentException when an invalid material or enchantment is provided
     */
    public static ItemStack parseItem(String text) {
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

        // Set the meta and return created item line
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Implode a String array and combine into a string separated by a space
     *
     * @param toImplode A string array to implode
     * @return An imploded string joined by spaces
     */
    public static ColoredText implode(String[] toImplode) {
        return implode(0, toImplode);
    }

    /**
     * Implodes a String array and combines into a single string seprated by a space
     *
     * @param startIndex The start index of the String array to implode
     * @param toImplode The String array to implode
     * @return An imploded string joined by spaces
     */
    public static ColoredText implode(int startIndex, String[] toImplode) {
        return implode(startIndex, toImplode, " ");
    }

    /**
     * Implode a string array from a starting index and combine into a string separated by a space
     *
     * @param startIndex The start index of the String array to implode
     * @param toImplode A string array to implode
     * @param spacer The string to seperate words
     * @return An imploded string joined by the spacer
     */
    public static ColoredText implode(int startIndex, String[] toImplode, String spacer) {
        StringBuilder ret = new StringBuilder();

        for (int i = startIndex ; i < toImplode.length ; i++) {
            if (toImplode[i] != null) {
                ret.append(toImplode[i]);
            }
            if (i < toImplode.length - 1) {
                ret.append(spacer);
            }
        }

        return color(ret.toString().trim());
    }
}
