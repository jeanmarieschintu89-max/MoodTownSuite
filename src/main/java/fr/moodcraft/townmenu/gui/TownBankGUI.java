package fr.moodcraft.townmenu.gui;

import fr.moodcraft.townmenu.util.MoodStyle;
import fr.moodcraft.townmenu.util.SafeGUI;
import fr.moodcraft.townmenu.util.TownyUtil;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TownBankGUI {

    public static final String TITLE =
            MoodStyle.guiTitle("Banque Municipale");

    public static void open(Player player) {

        Inventory inv = Bukkit.createInventory(
                null,
                36,
                TITLE
        );

        SafeGUI.fill(inv, Material.BLACK_STAINED_GLASS_PANE, " ");

        boolean canManage = TownyUtil.canManageTown(player);

        SafeGUI.safeSet(
                inv,
                4,
                SafeGUI.glow(SafeGUI.item(
                        Material.GOLD_INGOT,
                        MoodStyle.button("Banque municipale"),
                        "§8• §7Ville : §b" + TownyUtil.townName(player),
                        "§8• §7Dépôt ouvert aux habitants",
                        canManage
                                ? "§8• §7Retrait disponible"
                                : "§8• §7Retrait réservé à la mairie",
                        "",
                        "§e➜ §fCompte Towny de la ville"
                ))
        );

        SafeGUI.safeSet(
                inv,
                11,
                SafeGUI.item(
                        Material.EMERALD,
                        MoodStyle.button("Déposer"),
                        "§8• §7Ajoute de l'argent",
                        "§8• §7dans la ville",
                        "§8• §7Montant dans le chat",
                        "",
                        "§e➜ §fFaire un dépôt"
                )
        );

        if (canManage) {

            SafeGUI.safeSet(
                    inv,
                    13,
                    SafeGUI.item(
                            Material.REDSTONE,
                            MoodStyle.button("Retirer"),
                            "§8• §7Retire de l'argent",
                            "§8• §7de la ville",
                            "§8• §7Maire / assistant",
                            "",
                            "§e➜ §fFaire un retrait"
                    )
            );
        }

        SafeGUI.safeSet(
                inv,
                canManage ? 15 : 13,
                SafeGUI.item(
                        Material.BOOK,
                        MoodStyle.button("Compte Towny"),
                        "§8• §7Informations économiques",
                        "§8• §7du compte municipal",
                        "",
                        "§e➜ §fOuvrir /town"
                )
        );

        SafeGUI.safeSet(
                inv,
                31,
                SafeGUI.item(
                        Material.BARRIER,
                        MoodStyle.dangerButton("Retour"),
                        "§8• §7Retour au menu ville"
                )
        );

        player.openInventory(inv);
    }
}
