package fr.moodcraft.townmenu.util;

import org.bukkit.Material;

import org.bukkit.enchantments.Enchantment;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SafeGUI {

    private static final DecimalFormat MONEY =
            new DecimalFormat("#,###");

    public static ItemStack item(
            Material material,
            String name,
            String... lore
    ) {

        Material finalMaterial = normalizeMaterial(material, name);

        ItemStack item = new ItemStack(finalMaterial);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {

            meta.setDisplayName(name);

            if (lore != null && lore.length > 0) {
                meta.setLore(normalizeLore(lore));
            }

            hide(meta);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack glow(ItemStack item) {

        if (item == null)
            return null;

        ItemMeta meta = item.getItemMeta();

        if (meta != null) {

            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static void safeSet(Inventory inv, int slot, ItemStack item) {

        if (inv == null)
            return;

        if (slot < 0 || slot >= inv.getSize())
            return;

        inv.setItem(slot, item);
    }

    public static void fill(Inventory inv, Material material, String name) {

        if (inv == null)
            return;

        ItemStack filler = item(material, name);

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, filler);
        }
    }

    public static String money(double value) {
        return MONEY.format(value);
    }

    public static void hide(ItemMeta meta) {

        if (meta == null)
            return;

        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP
        );

        try {
            ItemFlag flag = ItemFlag.valueOf("HIDE_ITEM_SPECIFICS");
            meta.addItemFlags(flag);
        } catch (IllegalArgumentException ignored) {
        }
    }

    private static List<String> normalizeLore(String... lore) {

        List<String> lines = new ArrayList<>();

        for (String line : lore) {
            lines.add(normalizeLine(line));
        }

        return lines;
    }

    private static String normalizeLine(String line) {

        if (line == null || line.isBlank()) {
            return "";
        }

        String trimmed = line.trim().replace("§c✘", "§c✖");
        String clean = cleanPrefix(trimmed).toLowerCase();

        if (trimmed.startsWith("§8•")
                || trimmed.startsWith("§e➜")
                || trimmed.startsWith("§a✔")
                || trimmed.startsWith("§c✖")) {
            return trimmed;
        }

        if (isActionLine(clean) || trimmed.startsWith("§e")) {
            return "§e➜ §f" + cleanPrefix(trimmed);
        }

        if (isDangerLine(clean) || trimmed.startsWith("§c")) {
            return "§c✖ §f" + cleanPrefix(trimmed);
        }

        if (trimmed.startsWith("§a")) {
            return "§a✔ §f" + cleanPrefix(trimmed);
        }

        if (trimmed.startsWith("§7") || trimmed.startsWith("§8")) {
            return "§8• §7" + cleanPrefix(trimmed);
        }

        return "§8• §7" + cleanPrefix(trimmed);
    }

    private static Material normalizeMaterial(Material material, String name) {

        if (isReturnButton(name)) {
            return Material.BARRIER;
        }

        return material == null ? Material.BARRIER : material;
    }

    private static boolean isReturnButton(String name) {

        if (name == null) {
            return false;
        }

        String clean = name
                .replaceAll("§.", "")
                .replace("✦", "")
                .trim()
                .toLowerCase();

        return clean.equals("retour")
                || clean.equals("fermer")
                || clean.equals("revenir")
                || clean.equals("annuler")
                || clean.contains("retour au menu")
                || clean.contains("fermer le menu");
    }

    private static boolean isActionLine(String clean) {
        return clean.startsWith("clique")
                || clean.startsWith("ouvrir")
                || clean.startsWith("voir")
                || clean.startsWith("consulter")
                || clean.startsWith("commencer")
                || clean.startsWith("rejoindre")
                || clean.startsWith("gérer")
                || clean.startsWith("gerer")
                || clean.startsWith("modifier");
    }

    private static boolean isDangerLine(String clean) {
        return clean.startsWith("accès")
                || clean.startsWith("acces")
                || clean.startsWith("réservé")
                || clean.startsWith("reserve")
                || clean.startsWith("non autorisé")
                || clean.startsWith("indisponible")
                || clean.startsWith("action sensible");
    }

    private static String cleanPrefix(String text) {
        if (text == null) {
            return "";
        }

        return text
                .replaceFirst("^§[0-9a-fk-or]", "")
                .replaceFirst("^➜\\s*", "")
                .replaceFirst("^✔\\s*", "")
                .replaceFirst("^✘\\s*", "")
                .replaceFirst("^✖\\s*", "")
                .replaceFirst("^•\\s*", "")
                .trim();
    }
}
