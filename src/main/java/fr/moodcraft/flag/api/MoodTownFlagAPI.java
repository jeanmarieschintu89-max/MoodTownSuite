package fr.moodcraft.flag.api;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;

import fr.moodcraft.flag.gui.FlagMainGUI;
import fr.moodcraft.flag.manager.FlagManager;
import fr.moodcraft.flag.model.TownFlag;
import fr.moodcraft.flag.util.BannerUtil;

import org.bukkit.Material;

import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;

import org.bukkit.entity.Player;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MoodTownFlagAPI {

    public static void openMenu(
            Player player
    ) {

        FlagMainGUI.open(player);
    }

    public static void openTownMenu(
            Player player,
            Town town
    ) {

        FlagMainGUI.openTown(
                player,
                town
        );
    }

    public static void openNationMenu(
            Player player,
            Nation nation
    ) {

        FlagMainGUI.openNation(
                player,
                nation
        );
    }

    public static boolean hasTownFlag(
            String town
    ) {

        return FlagManager.getTown(town) != null;
    }

    public static boolean hasNationFlag(
            String nation
    ) {

        return FlagManager.getNation(nation) != null;
    }

    public static ItemStack getTownFlagItem(
            String town
    ) {

        TownFlag flag =
                FlagManager.getTown(town);

        if (flag == null) {
            return null;
        }

        ItemStack item =
                cleanItem(
                        flag.getBanner()
                                .clone()
                );

        applyDisplay(
                item,
                "§6✦ §fDrapeau municipal §6✦",
                List.of(
                        "§7Ville: §b" + safeName(town),
                        "",
                        "§a✔ Drapeau enregistré",
                        "",
                        "§8• §7Identité visuelle",
                        "§8• §7affichée dans les menus"
                )
        );

        return item;
    }

    public static ItemStack getNationFlagItem(
            String nation
    ) {

        TownFlag flag =
                FlagManager.getNation(nation);

        if (flag == null) {
            return null;
        }

        ItemStack item =
                cleanItem(
                        flag.getBanner()
                                .clone()
                );

        applyDisplay(
                item,
                "§6✦ §fDrapeau national §6✦",
                List.of(
                        "§7Nation: §e" + safeName(nation),
                        "",
                        "§a✔ Drapeau enregistré",
                        "",
                        "§8• §7Identité visuelle",
                        "§8• §7affichée dans les menus"
                )
        );

        return item;
    }

    public static ItemStack getTownShieldItem(
            String town
    ) {

        TownFlag flag =
                FlagManager.getTown(town);

        if (flag == null) {
            return null;
        }

        ItemStack shield =
                createShield(
                        flag.getBanner()
                );

        applyDisplay(
                shield,
                "§6✦ §fBlason municipal §6✦",
                List.of(
                        "§7Ville: §b" + safeName(town),
                        "",
                        "§a✔ Blason appliqué",
                        "",
                        "§8• §7Bouclier officiel",
                        "§8• §7lié au drapeau"
                )
        );

        return shield;
    }

    public static ItemStack getNationShieldItem(
            String nation
    ) {

        TownFlag flag =
                FlagManager.getNation(nation);

        if (flag == null) {
            return null;
        }

        ItemStack shield =
                createShield(
                        flag.getBanner()
                );

        applyDisplay(
                shield,
                "§6✦ §fBlason national §6✦",
                List.of(
                        "§7Nation: §e" + safeName(nation),
                        "",
                        "§a✔ Blason appliqué",
                        "",
                        "§8• §7Bouclier officiel",
                        "§8• §7lié au drapeau"
                )
        );

        return shield;
    }

    private static ItemStack createShield(
            ItemStack bannerItem
    ) {

        if (bannerItem == null) {
            return null;
        }

        if (!(bannerItem.getItemMeta()
                instanceof BannerMeta bannerMeta)) {
            return null;
        }

        ItemStack shield =
                new ItemStack(
                        Material.SHIELD
                );

        if (!(shield.getItemMeta()
                instanceof BlockStateMeta shieldMeta)) {
            return cleanItem(shield);
        }

        if (!(shieldMeta.getBlockState()
                instanceof Banner banner)) {
            return cleanItem(shield);
        }

        banner.setBaseColor(
                BannerUtil.getBannerColor(
                        bannerItem
                )
        );

        for (Pattern pattern :
                bannerMeta.getPatterns()) {

            banner.addPattern(pattern);
        }

        shieldMeta.setBlockState(
                banner
        );

        shieldMeta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP
        );

        addItemSpecificFlag(
                shieldMeta
        );

        shield.setItemMeta(
                shieldMeta
        );

        return shield;
    }

    private static ItemStack cleanItem(
            ItemStack item
    ) {

        if (item == null) {
            return null;
        }

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return item;
        }

        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP
        );

        addItemSpecificFlag(
                meta
        );

        item.setItemMeta(
                meta
        );

        return item;
    }

    private static void applyDisplay(
            ItemStack item,
            String name,
            List<String> lore
    ) {

        if (item == null) {
            return;
        }

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null) {
            return;
        }

        meta.setDisplayName(name);
        meta.setLore(lore);

        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP
        );

        addItemSpecificFlag(meta);

        item.setItemMeta(meta);
    }

    private static String safeName(
            String name
    ) {

        if (name == null || name.isBlank()) {
            return "Inconnu";
        }

        if (name.length() <= 18) {
            return name;
        }

        return name.substring(0, 15) + "...";
    }

    private static void addItemSpecificFlag(
            ItemMeta meta
    ) {

        try {

            ItemFlag flag =
                    ItemFlag.valueOf(
                            "HIDE_ITEM_SPECIFICS"
                    );

            meta.addItemFlags(flag);

        } catch (IllegalArgumentException ignored) {}
    }
}