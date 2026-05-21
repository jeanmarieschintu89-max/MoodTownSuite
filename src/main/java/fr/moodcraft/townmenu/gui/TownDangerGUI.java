package fr.moodcraft.townmenu.gui;

import fr.moodcraft.townmenu.util.MoodStyle;
import fr.moodcraft.townmenu.util.SafeGUI;
import fr.moodcraft.townmenu.util.TownyUtil;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TownDangerGUI {

    public static final String TITLE =
            MoodStyle.guiTitle("Zone Sensible");

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
                        Material.TNT,
                        MoodStyle.dangerButton("Zone sensible"),
                        "§7Ville: §b" + TownyUtil.townName(player),
                        "",
                        MoodStyle.bullet("Actions dangereuses"),
                        MoodStyle.bullet("Confirmation Towny")
                ))
        );

        SafeGUI.safeSet(
                inv,
                11,
                SafeGUI.item(
                        Material.RED_WOOL,
                        MoodStyle.dangerButton("Quitter la ville"),
                        "§7Affiche la commande",
                        "§7de départ Towny.",
                        "",
                        MoodStyle.bullet("Confirmation manuelle")
                )
        );

        SafeGUI.safeSet(
                inv,
                13,
                SafeGUI.item(
                        Material.LAVA_BUCKET,
                        MoodStyle.dangerButton("Fermer la ville"),
                        "§7Action définitive",
                        "§7via Towny.",
                        "",
                        MoodStyle.bullet("Maire uniquement"),
                        MoodStyle.bullet("Confirmation manuelle")
                )
        );

        SafeGUI.safeSet(
                inv,
                15,
                SafeGUI.item(
                        Material.BARRIER,
                        MoodStyle.dangerButton("Unclaim ici"),
                        "§7Retire le chunk",
                        "§7actuel de la ville.",
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
                        "§7Retour à la gestion maire.",
                        "",
                        MoodStyle.bullet("Menu maire")
                )
        );

        player.openInventory(inv);
    }
}
