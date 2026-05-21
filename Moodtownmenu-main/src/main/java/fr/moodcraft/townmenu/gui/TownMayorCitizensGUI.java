package fr.moodcraft.townmenu.gui;

import fr.moodcraft.townmenu.util.MoodStyle;
import fr.moodcraft.townmenu.util.SafeGUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import org.bukkit.entity.Player;

import org.bukkit.inventory.Inventory;

public class TownMayorCitizensGUI {

    public static final String TITLE =
            MoodStyle.guiTitle("Mairie Citoyens");

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
                                Material.PLAYER_HEAD,
                                MoodStyle.button("Gestion des citoyens"),
                                "§8• §7Invitations",
                                "§8• §7Expulsions",
                                "§8• §7Assistants de mairie"
                        )
                )
        );

        SafeGUI.safeSet(
                inv,
                11,
                SafeGUI.item(
                        Material.LIME_DYE,
                        MoodStyle.button("Inviter"),
                        "§8• §7Écrire le pseudo dans le chat",
                        "§8• §7Compatible Java et Bedrock",
                        "",
                        "§e➜ §fLancer la saisie"
                )
        );

        SafeGUI.safeSet(
                inv,
                13,
                SafeGUI.item(
                        Material.RED_DYE,
                        MoodStyle.button("Expulser"),
                        "§8• §7Écrire le pseudo dans le chat",
                        "§8• §7Retire un habitant de la ville",
                        "",
                        "§e➜ §fLancer la saisie"
                )
        );

        SafeGUI.safeSet(
                inv,
                15,
                SafeGUI.item(
                        Material.GOLDEN_HELMET,
                        MoodStyle.button("Ajouter assistant"),
                        "§8• §7Écrire le pseudo dans le chat",
                        "§8• §7Donne un rôle municipal",
                        "",
                        "§e➜ §fLancer la saisie"
                )
        );

        SafeGUI.safeSet(
                inv,
                22,
                SafeGUI.item(
                        Material.CHAINMAIL_HELMET,
                        MoodStyle.button("Retirer assistant"),
                        "§8• §7Écrire le pseudo dans le chat",
                        "§8• §7Retire le rôle municipal",
                        "",
                        "§e➜ §fLancer la saisie"
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
