package fr.moodcraft.flag.util;

import org.bukkit.DyeColor;
import org.bukkit.Material;

import org.bukkit.inventory.ItemStack;

public class BannerUtil {

    public static boolean isBanner(
            ItemStack item
    ) {

        if (item == null)
            return false;

        Material material =
                item.getType();

        return material.name()
                .endsWith("_BANNER");
    }

    public static DyeColor getBannerColor(
            ItemStack item
    ) {

        if (!isBanner(item))
            return DyeColor.WHITE;

        String colorName =
                item.getType()
                        .name()
                        .replace("_BANNER", "");

        try {

            return DyeColor.valueOf(
                    colorName
            );

        } catch (IllegalArgumentException e) {

            return DyeColor.WHITE;
        }
    }
}