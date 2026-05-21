package fr.moodcraft.townmenu.gui;

import fr.moodcraft.townmenu.util.MoodStyle;
import fr.moodcraft.townmenu.util.SafeGUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import org.bukkit.entity.Player;

import org.bukkit.inventory.Inventory;

public class TownMayorEconomyGUI {

    public static final String TITLE =
            MoodStyle.guiTitle("Mairie Économie");

    public static void open(
            Player player
    ) {

        Inventory inv =
                Bukkit.createInventory(
                        null,
                        45,
                        TITLE
                );

        SafeGUI.fill(
                inv,
                Material.BLACK_STAINED_GLASS_PANE,
                " "
        );

        SafeGUI.safeSet(
                inv,
                4,
                SafeGUI.glow(
                        SafeGUI.item(
                                Material.EMERALD_BLOCK,
                                MoodStyle.button("Économie municipale"),
                                "§8• §7Banque de ville",
                                "§8• §7Bureau des Entreprises",
                                "§8• §7Taxes municipales"
                        )
                )
        );

        SafeGUI.safeSet(
                inv,
                11,
                SafeGUI.item(
                        Material.GOLD_INGOT,
                        MoodStyle.button("Banque municipale"),
                        "§8• §7Dépôts et retraits",
                        "§8• §7Argent de la ville",
                        "",
                        "§e➜ §fOuvrir la banque"
                )
        );

        SafeGUI.safeSet(
                inv,
                13,
                SafeGUI.item(
                        Material.LECTERN,
                        MoodStyle.button("Bureau des Entreprises"),
                        "§8• §7Entreprises",
                        "§8• §7Contrats municipaux",
                        "§8• §7Litiges",
                        "",
                        "§e➜ §fOuvrir MoodBusiness"
                )
        );

        SafeGUI.safeSet(
                inv,
                15,
                SafeGUI.item(
                        Material.PAPER,
                        MoodStyle.button("Fiscalité"),
                        "§8• §7Taxes de ville",
                        "§8• §7Terrains et commerces",
                        "",
                        "§e➜ §fOuvrir les taxes"
                )
        );

        SafeGUI.safeSet(
                inv,
                40,
                SafeGUI.item(
                        Material.ARROW,
                        MoodStyle.button("Retour"),
                        "§8• §7Retour à la gestion maire"
                )
        );

        player.openInventory(inv);
    }
}
