package fr.moodcraft.townmenu.gui;

import fr.moodcraft.townmenu.util.MoodStyle;
import fr.moodcraft.townmenu.util.SafeGUI;
import fr.moodcraft.townmenu.util.TownyUtil;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TownPlotGUI {

    public static final String TITLE =
            MoodStyle.guiTitle("Ma Parcelle");

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
                        Material.OAK_DOOR,
                        MoodStyle.button("Ma Parcelle"),
                        "§7Ville: §b" + TownyUtil.townName(player),
                        "",
                        MoodStyle.bullet("Terrain personnel"),
                        MoodStyle.bullet("Accès"),
                        MoodStyle.bullet("Vente")
                ))
        );

        SafeGUI.safeSet(
                inv,
                10,
                SafeGUI.item(
                        Material.GRASS_BLOCK,
                        MoodStyle.button("Acheter / Claim"),
                        "§7Claim le terrain",
                        "§7où vous êtes.",
                        "",
                        MoodStyle.bullet("Action directe")
                )
        );

        SafeGUI.safeSet(
                inv,
                12,
                SafeGUI.item(
                        Material.COARSE_DIRT,
                        MoodStyle.button("Retirer le claim"),
                        "§7Libère votre terrain",
                        "§7actuel.",
                        "",
                        MoodStyle.bullet("Action directe")
                )
        );

        SafeGUI.safeSet(
                inv,
                14,
                SafeGUI.item(
                        Material.LIME_DYE,
                        MoodStyle.button("Ajouter un accès"),
                        "§7Écrivez le pseudo",
                        "§7dans le chat.",
                        "",
                        MoodStyle.bullet("Saisie chat")
                )
        );

        SafeGUI.safeSet(
                inv,
                16,
                SafeGUI.item(
                        Material.RED_DYE,
                        MoodStyle.button("Retirer un accès"),
                        "§7Écrivez le pseudo",
                        "§7dans le chat.",
                        "",
                        MoodStyle.bullet("Saisie chat")
                )
        );

        SafeGUI.safeSet(
                inv,
                21,
                SafeGUI.item(
                        Material.EMERALD,
                        MoodStyle.button("Mettre en vente"),
                        "§7Écrivez le prix",
                        "§7dans le chat.",
                        "",
                        MoodStyle.bullet("Saisie chat")
                )
        );

        SafeGUI.safeSet(
                inv,
                23,
                SafeGUI.item(
                        Material.REDSTONE,
                        MoodStyle.button("Retirer de la vente"),
                        "§7Le terrain n'est plus",
                        "§7à vendre.",
                        "",
                        MoodStyle.bullet("Action directe")
                )
        );

        SafeGUI.safeSet(
                inv,
                31,
                SafeGUI.item(
                        Material.ARROW,
                        MoodStyle.button("Retour"),
                        "§7Retour au menu ville.",
                        "",
                        MoodStyle.bullet("Menu principal")
                )
        );

        player.openInventory(inv);
    }
}
