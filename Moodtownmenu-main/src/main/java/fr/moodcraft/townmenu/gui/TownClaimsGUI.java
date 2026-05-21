package fr.moodcraft.townmenu.gui;

import fr.moodcraft.townmenu.util.MoodStyle;
import fr.moodcraft.townmenu.util.SafeGUI;
import fr.moodcraft.townmenu.util.TownyUtil;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TownClaimsGUI {

    public static final String TITLE =
            MoodStyle.guiTitle("Territoire");

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
                        Material.GRASS_BLOCK,
                        MoodStyle.button("Territoire"),
                        "§7Ville: §b" + TownyUtil.townName(player),
                        "",
                        MoodStyle.bullet("Claims"),
                        MoodStyle.bullet("Carte"),
                        MoodStyle.bullet("Extension")
                ))
        );

        SafeGUI.safeSet(
                inv,
                10,
                SafeGUI.item(
                        Material.LIME_CONCRETE,
                        MoodStyle.button("Claim ici"),
                        "§7Ajoute le chunk",
                        "§7à la ville.",
                        "",
                        MoodStyle.bullet("Action directe")
                )
        );

        SafeGUI.safeSet(
                inv,
                12,
                SafeGUI.item(
                        Material.RED_CONCRETE,
                        MoodStyle.button("Unclaim ici"),
                        "§7Retire le chunk",
                        "§7de la ville.",
                        "",
                        MoodStyle.bullet("Action directe")
                )
        );

        SafeGUI.safeSet(
                inv,
                14,
                SafeGUI.item(
                        Material.FILLED_MAP,
                        MoodStyle.button("Carte Towny"),
                        "§7Affiche la carte",
                        "§7dans le chat.",
                        "",
                        MoodStyle.bullet("Vue rapide")
                )
        );

        SafeGUI.safeSet(
                inv,
                16,
                SafeGUI.item(
                        Material.COMPASS,
                        MoodStyle.button("Claim auto"),
                        "§7Active le claim",
                        "§7automatique.",
                        "",
                        MoodStyle.bullet("Action Towny")
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
