package fr.moodcraft.townmenu.gui;

import fr.moodcraft.townmenu.util.MoodStyle;
import fr.moodcraft.townmenu.util.SafeGUI;
import fr.moodcraft.townmenu.util.TownyUtil;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TownResidentsGUI {

    public static final String TITLE =
            MoodStyle.guiTitle("Habitants");

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
                        Material.PLAYER_HEAD,
                        MoodStyle.button("Habitants"),
                        "§7Ville: §b" + TownyUtil.townName(player),
                        "",
                        MoodStyle.bullet("Liste"),
                        MoodStyle.bullet("Spawn"),
                        MoodStyle.bullet("Retour")
                ))
        );

        SafeGUI.safeSet(
                inv,
                11,
                SafeGUI.item(
                        Material.PAPER,
                        MoodStyle.button("Liste des habitants"),
                        "§7Affiche la liste",
                        "§7dans le chat.",
                        "",
                        MoodStyle.bullet("Lisible Bedrock")
                )
        );

        SafeGUI.safeSet(
                inv,
                15,
                SafeGUI.item(
                        Material.ENDER_PEARL,
                        MoodStyle.button("Spawn ville"),
                        "§7Téléporte au spawn",
                        "§7de la ville.",
                        "",
                        MoodStyle.bullet("Déplacement rapide")
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
