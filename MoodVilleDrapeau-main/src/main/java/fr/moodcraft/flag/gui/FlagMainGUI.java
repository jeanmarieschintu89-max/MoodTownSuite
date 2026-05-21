package fr.moodcraft.flag.gui;

import com.palmergames.bukkit.towny.TownyAPI;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

import fr.moodcraft.flag.manager.FlagManager;
import fr.moodcraft.flag.model.TownFlag;
import fr.moodcraft.flag.util.FlagMessages;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import org.bukkit.entity.Player;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.UUID;

public class FlagMainGUI {

    public static final String TITLE =
            "§6✦ §8§lRegistre des Drapeaux §6✦";

    private static final Map<UUID, String> viewedNames =
            new HashMap<>();

    private static final Map<UUID, String> viewedTypes =
            new HashMap<>();

    public static void open(Player p) {

        Resident resident = TownyAPI.getInstance().getResident(p.getUniqueId());

        if (resident == null || !resident.hasTown()) {

            FlagMessages.error(
                    p,
                    "Registre des Drapeaux",
                    "Vous devez appartenir à une ville pour ouvrir ce menu."
            );

            return;
        }

        Town town = resident.getTownOrNull();

        if (town == null) {

            FlagMessages.error(
                    p,
                    "Registre des Drapeaux",
                    "Impossible de charger votre ville."
            );

            return;
        }

        openTown(p, town);
    }

    public static void openTown(Player p, Town town) {

        if (town == null) {
            return;
        }

        openGeneric(p, town.getName(), "town", "Ville", getMayorName(town));
    }

    public static void openNation(Player p, Nation nation) {

        if (nation == null) {
            return;
        }

        openGeneric(p, nation.getName(), "nation", "Nation", getKingName(nation));
    }

    public static void open(Player p, Town town) {
        openTown(p, town);
    }

    private static void openGeneric(
            Player p,
            String ownerName,
            String ownerType,
            String displayType,
            String leaderName
    ) {

        viewedNames.put(p.getUniqueId(), ownerName);
        viewedTypes.put(p.getUniqueId(), ownerType);

        Resident resident = TownyAPI.getInstance().getResident(p.getUniqueId());

        Inventory inv = Bukkit.createInventory(null, 27, TITLE);

        fill(inv);

        TownFlag flag = ownerType.equalsIgnoreCase("nation")
                ? FlagManager.getNation(ownerName)
                : FlagManager.getTown(ownerName);

        ItemStack banner = flag != null && flag.getBanner() != null
                ? flag.getBanner().clone()
                : new ItemStack(Material.WHITE_BANNER);

        ItemMeta meta = banner.getItemMeta();

        if (meta != null) {

            meta.setDisplayName(
                    ownerType.equalsIgnoreCase("nation")
                            ? "§6✦ §fDrapeau national §6✦"
                            : "§6✦ §fDrapeau municipal §6✦"
            );

            List<String> lore = new ArrayList<>();
            lore.add("§8• §7" + displayType + " : §e" + shortText(ownerName, 18));
            lore.add(ownerType.equalsIgnoreCase("nation")
                    ? "§8• §7Dirigeant : §f" + shortText(leaderName, 18)
                    : "§8• §7Maire : §f" + shortText(leaderName, 18));
            lore.add("");
            lore.add(flag == null
                    ? "§c✖ §fAucun drapeau enregistré."
                    : "§a✔ §fDrapeau officiel validé");
            lore.add("§8• §7Symbole affiché dans les menus");

            meta.setLore(normalizeLore(lore));
            hide(meta);
            banner.setItemMeta(meta);
        }

        inv.setItem(13, banner);

        boolean canManage = p.hasPermission("moodtownflag.admin");

        if (resident != null) {

            if (ownerType.equalsIgnoreCase("nation")) {

                canManage = canManage
                        || resident.isKing()
                        || resident.hasNationRank("assistant")
                        || resident.hasNationRank("co-king");

            } else {

                canManage = canManage
                        || resident.isMayor()
                        || resident.hasTownRank("assistant")
                        || resident.hasTownRank("co-mayor");
            }
        }

        if (canManage) {

            ItemStack importItem = new ItemStack(Material.LOOM);
            ItemMeta importMeta = importItem.getItemMeta();

            if (importMeta != null) {

                importMeta.setDisplayName("§6✦ §fImporter une bannière §6✦");
                importMeta.setLore(normalizeLore(List.of(
                        "§8• §7Tenez une bannière dans votre main",
                        "§8• §7Le motif sera copié",
                        "§8• §7comme drapeau officiel",
                        "",
                        "§e➜ §fEnregistrer"
                )));
                hide(importMeta);
                importItem.setItemMeta(importMeta);
            }

            inv.setItem(20, importItem);

            ItemStack removeItem = new ItemStack(Material.BARRIER);
            ItemMeta removeMeta = removeItem.getItemMeta();

            if (removeMeta != null) {

                removeMeta.setDisplayName("§c✦ §fSupprimer le drapeau §c✦");
                removeMeta.setLore(normalizeLore(List.of(
                        "§8• §7Retire le drapeau officiel",
                        "§8• §7L'identité visuelle sera retirée",
                        "§8• §7des menus",
                        "",
                        "§c✖ §fAction sensible"
                )));
                hide(removeMeta);
                removeItem.setItemMeta(removeMeta);
            }

            inv.setItem(24, removeItem);
        }

        if (p.hasPermission("moodtownflag.admin")) {

            ItemStack shield = new ItemStack(Material.SHIELD);
            ItemMeta shieldMeta = shield.getItemMeta();

            if (shieldMeta != null) {

                shieldMeta.setDisplayName("§6✦ §fBouclier officiel §6✦");
                shieldMeta.setLore(normalizeLore(List.of(
                        "§8• §7" + displayType + " : §e" + shortText(ownerName, 18),
                        "§8• §7Distribuer un bouclier avec ce blason",
                        "§8• §7Motif appliqué",
                        "§8• §7Équipement officiel",
                        "",
                        "§e➜ §fRecevoir"
                )));
                hide(shieldMeta);
                shield.setItemMeta(shieldMeta);
            }

            inv.setItem(22, shield);
        }

        p.openInventory(inv);
    }

    private static void fill(Inventory inv) {

        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = glass.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(" ");
            glass.setItemMeta(meta);
        }

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, glass);
        }
    }

    private static List<String> normalizeLore(List<String> lore) {

        List<String> lines = new ArrayList<>();

        if (lore == null) {
            return lines;
        }

        for (String line : lore) {
            lines.add(normalizeLine(line));
        }

        return lines;
    }

    private static String normalizeLine(String line) {

        if (line == null || line.isBlank()) {
            return "";
        }

        String trimmed = line.trim().replace("✘", "✖");

        if (trimmed.startsWith("§8•")
                || trimmed.startsWith("§e➜")
                || trimmed.startsWith("§a✔")
                || trimmed.startsWith("§c✖")) {
            return trimmed;
        }

        if (trimmed.startsWith("§a")) {
            return "§a✔ §f" + cleanPrefix(trimmed);
        }

        if (trimmed.startsWith("§c")) {
            return "§c✖ §f" + cleanPrefix(trimmed);
        }

        if (trimmed.startsWith("§7") || trimmed.startsWith("§8")) {
            return "§8• §7" + cleanPrefix(trimmed);
        }

        return "§8• §7" + cleanPrefix(trimmed);
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

    private static void hide(ItemMeta meta) {

        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP
        );

        addItemSpecificFlag(meta);
    }

    private static void addItemSpecificFlag(ItemMeta meta) {

        try {
            ItemFlag flag = ItemFlag.valueOf("HIDE_ITEM_SPECIFICS");
            meta.addItemFlags(flag);
        } catch (IllegalArgumentException ignored) {
        }
    }

    private static String shortText(String text, int max) {

        if (text == null || text.isBlank()) {
            return "Inconnu";
        }

        String clean = text.replaceAll("§.", "").trim();

        if (clean.length() <= max) {
            return clean;
        }

        return clean.substring(0, Math.max(1, max - 3)) + "...";
    }

    private static String getMayorName(Town town) {

        if (town.getMayor() == null) {
            return "Inconnu";
        }

        return town.getMayor().getName();
    }

    private static String getKingName(Nation nation) {

        if (nation.getKing() == null) {
            return "Inconnu";
        }

        return nation.getKing().getName();
    }

    public static String getViewedName(Player p) {
        return viewedNames.get(p.getUniqueId());
    }

    public static String getViewedType(Player p) {
        return viewedTypes.get(p.getUniqueId());
    }
}
