package fr.moodcraft.townmenu.gui;

import fr.moodcraft.townmenu.util.MoodStyle;
import fr.moodcraft.townmenu.util.SafeGUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import org.bukkit.entity.Player;

import org.bukkit.inventory.Inventory;

public class TownMayorCityGUI {

    public static final String TITLE =
            MoodStyle.guiTitle("Mairie Ville");

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
                                Material.COMPASS,
                                MoodStyle.button("Gestion de la ville"),
                                "§8• §7Spawn municipal",
                                "§8• §7Nom de ville",
                                "§8• §7Identité visuelle"
                        )
                )
        );

        SafeGUI.safeSet(
                inv,
                11,
                SafeGUI.item(
                        Material.RECOVERY_COMPASS,
                        MoodStyle.button("Définir le spawn"),
                        "§8• §7Place le spawn ville",
                        "§8• §7sur votre position actuelle",
                        "",
                        "§e➜ §fAction directe"
                )
        );

        SafeGUI.safeSet(
                inv,
                13,
                SafeGUI.item(
                        Material.NAME_TAG,
                        MoodStyle.button("Renommer la ville"),
                        "§8• §7Écrire le nouveau nom",
                        "§8• §7dans le chat",
                        "",
                        "§e➜ §fLancer la saisie"
                )
        );

        SafeGUI.safeSet(
                inv,
                15,
                SafeGUI.item(
                        Material.WHITE_BANNER,
                        MoodStyle.button("Drapeau municipal"),
                        "§8• §7Blason de ville",
                        "§8• §7Écu municipal",
                        "",
                        "§e➜ §fModifier l'identité"
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
