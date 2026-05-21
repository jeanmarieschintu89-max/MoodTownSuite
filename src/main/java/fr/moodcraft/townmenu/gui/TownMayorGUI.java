package fr.moodcraft.townmenu.gui;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

import fr.moodcraft.flag.api.MoodTownFlagAPI;
import fr.moodcraft.townmenu.util.MessageUtil;
import fr.moodcraft.townmenu.util.MoodStyle;
import fr.moodcraft.townmenu.util.SafeGUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import org.bukkit.entity.Player;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TownMayorGUI {

    public static final String TITLE =
            MoodStyle.guiTitle("Gestion Maire");

    public static void open(
            Player player
    ) {

        Inventory inv =
                Bukkit.createInventory(
                        null,
                        54,
                        TITLE
                );

        SafeGUI.fill(
                inv,
                Material.BLACK_STAINED_GLASS_PANE,
                " "
        );

        Resident resident;
        Town town;

        try {

            resident =
                    TownyAPI.getInstance()
                            .getResident(player);

            if (resident == null
                    || !resident.hasTown()) {

                MessageUtil.error(
                        player,
                        "Vous n'appartenez à aucune ville."
                );

                return;
            }

            town =
                    resident.getTown();

        } catch (Throwable ignored) {

            MessageUtil.error(
                    player,
                    "Impossible de charger votre ville."
            );

            return;
        }

        try {

            if (town.getMayor() != null
                    && !town.getMayor().equals(resident)
                    && !player.hasPermission("moodtownmenu.admin")) {

                MessageUtil.error(
                        player,
                        "Ce menu est réservé au maire de la ville."
                );

                return;
            }

        } catch (Throwable ignored) {

            MessageUtil.error(
                    player,
                    "Impossible de vérifier vos droits de mairie."
            );

            return;
        }

        SafeGUI.safeSet(
                inv,
                4,
                SafeGUI.glow(
                        flagItem(town)
                )
        );

        SafeGUI.safeSet(
                inv,
                20,
                SafeGUI.item(
                        Material.PLAYER_HEAD,
                        MoodStyle.button("Citoyens"),
                        "§8• §7Invitations et expulsions",
                        "§8• §7Assistants de mairie",
                        "",
                        "§e➜ §fOuvrir le panneau"
                )
        );

        SafeGUI.safeSet(
                inv,
                22,
                SafeGUI.item(
                        Material.COMPASS,
                        MoodStyle.button("Ville"),
                        "§8• §7Spawn municipal",
                        "§8• §7Nom de la ville",
                        "§8• §7Identité visuelle",
                        "",
                        "§e➜ §fOuvrir le panneau"
                )
        );

        SafeGUI.safeSet(
                inv,
                24,
                SafeGUI.item(
                        Material.EMERALD_BLOCK,
                        MoodStyle.button("Économie"),
                        "§8• §7Banque municipale",
                        "§8• §7Bureau des Entreprises",
                        "§8• §7Fiscalité",
                        "",
                        "§e➜ §fOuvrir le panneau"
                )
        );

        SafeGUI.safeSet(
                inv,
                30,
                SafeGUI.item(
                        Material.COMPARATOR,
                        MoodStyle.button("Paramètres"),
                        "§8• §7Règles municipales",
                        "§8• §7Permissions et taxes",
                        "",
                        "§e➜ §fOuvrir les réglages"
                )
        );

        SafeGUI.safeSet(
                inv,
                32,
                SafeGUI.item(
                        Material.WHITE_BANNER,
                        MoodStyle.button("Drapeau"),
                        "§8• §7Blason de ville",
                        "§8• §7Écu municipal",
                        "",
                        "§e➜ §fModifier l'identité"
                )
        );

        SafeGUI.safeSet(
                inv,
                34,
                SafeGUI.item(
                        Material.TNT,
                        MoodStyle.dangerButton("Zone sensible"),
                        "§8• §7Actions dangereuses",
                        "§8• §7Unclaim ou fermeture",
                        "",
                        "§c✖ §fÀ utiliser avec prudence"
                )
        );

        SafeGUI.safeSet(
                inv,
                49,
                SafeGUI.item(
                        Material.ARROW,
                        MoodStyle.button("Retour"),
                        "§8• §7Retour au menu ville"
                )
        );

        player.openInventory(inv);
    }

    private static ItemStack flagItem(
            Town town
    ) {

        if (town == null) {

            return SafeGUI.item(
                    Material.NETHER_STAR,
                    MoodStyle.button("Gestion maire"),
                    "§8• §7Aucune ville trouvée",
                    "",
                    "§8• §7Indisponible"
            );
        }

        try {

            ItemStack flag =
                    MoodTownFlagAPI.getTownFlagItem(
                            town.getName()
                    );

            if (flag != null
                    && flag.getType() != Material.AIR) {

                return flag;
            }

        } catch (Throwable ignored) {
        }

        return SafeGUI.item(
                Material.NETHER_STAR,
                MoodStyle.button("Gestion maire"),
                "§8• §7Ville : §b" + town.getName(),
                "",
                "§8• §7Outils municipaux",
                "§8• §7Administration"
        );
    }
}
