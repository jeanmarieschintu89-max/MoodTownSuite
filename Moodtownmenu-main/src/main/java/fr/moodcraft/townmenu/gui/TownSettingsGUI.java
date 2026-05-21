package fr.moodcraft.townmenu.gui;

import fr.moodcraft.townmenu.util.MoodStyle;
import fr.moodcraft.townmenu.util.SafeGUI;
import fr.moodcraft.townmenu.util.TownyUtil;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TownSettingsGUI {

    public static final String TITLE =
            MoodStyle.guiTitle("Paramètres Ville");

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
                        Material.COMPARATOR,
                        MoodStyle.button("Paramètres ville"),
                        "§7Ville: §b" + TownyUtil.townName(player),
                        "",
                        MoodStyle.bullet("Ouverture"),
                        MoodStyle.bullet("Permissions"),
                        MoodStyle.bullet("Taxes")
                ))
        );

        SafeGUI.safeSet(
                inv,
                10,
                SafeGUI.item(
                        Material.OAK_DOOR,
                        MoodStyle.button("Ville ouverte"),
                        "§7Active ou désactive",
                        "§7l'entrée libre.",
                        "",
                        MoodStyle.bullet("Action directe")
                )
        );

        SafeGUI.safeSet(
                inv,
                13,
                SafeGUI.item(
                        Material.IRON_DOOR,
                        MoodStyle.button("Permissions"),
                        "§7Règles pour les",
                        "§7joueurs extérieurs.",
                        "",
                        MoodStyle.bullet("Construire"),
                        MoodStyle.bullet("Casser"),
                        MoodStyle.bullet("Interagir")
                )
        );

        SafeGUI.safeSet(
                inv,
                16,
                SafeGUI.item(
                        Material.GOLD_INGOT,
                        MoodStyle.button("Taxes"),
                        "§7Gère les taxes",
                        "§7municipales.",
                        "",
                        MoodStyle.bullet("Ville"),
                        MoodStyle.bullet("Parcelle"),
                        MoodStyle.bullet("Commerce")
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
