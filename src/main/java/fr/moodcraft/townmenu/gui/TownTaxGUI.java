package fr.moodcraft.townmenu.gui;

import fr.moodcraft.townmenu.util.MoodStyle;
import fr.moodcraft.townmenu.util.SafeGUI;
import fr.moodcraft.townmenu.util.TownyUtil;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TownTaxGUI {

    public static final String TITLE =
            MoodStyle.guiTitle("Taxes Ville");

    public static void open(Player player) {

        Inventory inv = Bukkit.createInventory(
                null,
                36,
                TITLE
        );

        SafeGUI.fill(inv, Material.BLACK_STAINED_GLASS_PANE, " ");

        SafeGUI.safeSet(
                inv,
                4,
                SafeGUI.glow(SafeGUI.item(
                        Material.GOLD_INGOT,
                        MoodStyle.button("Taxes ville"),
                        "§7Ville: §b" + TownyUtil.townName(player),
                        "",
                        MoodStyle.bullet("Ville"),
                        MoodStyle.bullet("Parcelle"),
                        MoodStyle.bullet("Commerce")
                ))
        );

        SafeGUI.safeSet(
                inv,
                10,
                SafeGUI.item(
                        Material.EMERALD,
                        MoodStyle.button("Taxe de ville"),
                        "§7Taxe générale",
                        "§7des habitants.",
                        "",
                        MoodStyle.bullet("Saisie chat")
                )
        );

        SafeGUI.safeSet(
                inv,
                12,
                SafeGUI.item(
                        Material.GRASS_BLOCK,
                        MoodStyle.button("Taxe de parcelle"),
                        "§7Taxe appliquée",
                        "§7aux terrains.",
                        "",
                        MoodStyle.bullet("Saisie chat")
                )
        );

        SafeGUI.safeSet(
                inv,
                14,
                SafeGUI.item(
                        Material.CHEST,
                        MoodStyle.button("Taxe commerce"),
                        "§7Taxe des commerces",
                        "§7Towny.",
                        "",
                        MoodStyle.bullet("Saisie chat")
                )
        );

        SafeGUI.safeSet(
                inv,
                16,
                SafeGUI.item(
                        Material.MAP,
                        MoodStyle.button("Taxe ambassade"),
                        "§7Taxe des parcelles",
                        "§7ambassade.",
                        "",
                        MoodStyle.bullet("Saisie chat")
                )
        );

        SafeGUI.safeSet(
                inv,
                22,
                SafeGUI.item(
                        Material.BOOK,
                        MoodStyle.button("Informations"),
                        "§7Les taxes sont gérées",
                        "§7par Towny.",
                        "",
                        MoodStyle.bullet("Montants simples"),
                        MoodStyle.bullet("Prévenir les habitants")
                )
        );

        SafeGUI.safeSet(
                inv,
                31,
                SafeGUI.item(
                        Material.ARROW,
                        MoodStyle.button("Retour"),
                        "§7Retour aux paramètres.",
                        "",
                        MoodStyle.bullet("Menu ville")
                )
        );

        player.openInventory(inv);
    }
}
