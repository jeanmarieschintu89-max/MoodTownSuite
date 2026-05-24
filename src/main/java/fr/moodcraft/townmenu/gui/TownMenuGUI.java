package fr.moodcraft.townmenu.gui;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;

import fr.moodcraft.flag.api.MoodTownFlagAPI;
import fr.moodcraft.townmenu.util.MoodStyle;
import fr.moodcraft.townmenu.util.SafeGUI;
import fr.moodcraft.townmenu.util.TownyUtil;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TownMenuGUI {

    public static final String TITLE =
            MoodStyle.guiTitle("Menu Ville");

    public static void open(Player player) {

        Inventory inv = Bukkit.createInventory(null, 54, TITLE);
        SafeGUI.fill(inv, Material.BLACK_STAINED_GLASS_PANE, " ");

        Town town = TownyUtil.getTown(player);
        Nation nation = TownyUtil.getNation(player);

        SafeGUI.safeSet(inv, 4, SafeGUI.glow(createIdentityItem(player, town, nation)));

        if (town == null) {
            openNoTown(inv);
        } else {
            openTownMember(player, inv, town);
        }

        SafeGUI.safeSet(inv, 49,
                SafeGUI.item(
                        Material.ARROW,
                        MoodStyle.button("Retour"),
                        "§8• §7Retour au menu principal",
                        "",
                        "§e➜ §fOuvrir /menu"
                )
        );

        player.openInventory(inv);
    }

    private static void openNoTown(Inventory inv) {

        SafeGUI.safeSet(inv, 20,
                SafeGUI.item(
                        Material.OAK_SIGN,
                        MoodStyle.button("Rejoindre Utopia"),
                        "§8• §7Ville ouverte aux nouveaux",
                        "§8• §7Entrée rapide",
                        "§8• §7Départ accompagné",
                        "",
                        "§e➜ §fRejoindre la ville"
                )
        );

        SafeGUI.safeSet(inv, 22,
                SafeGUI.item(
                        Material.COMPASS,
                        MoodStyle.button("Voir les villes"),
                        "§8• §7Liste des villes Towny",
                        "§8• §7Trouver une communauté",
                        "§8• §7Choisir avant de rejoindre",
                        "",
                        "§e➜ §fOuvrir la liste"
                )
        );

        SafeGUI.safeSet(inv, 24,
                SafeGUI.item(
                        Material.GRASS_BLOCK,
                        MoodStyle.button("Créer une ville"),
                        "§8• §7Créer son territoire",
                        "§8• §7Nécessite de l'argent",
                        "§8• §7Nom à choisir",
                        "",
                        "§e➜ §fVoir les étapes"
                )
        );

        SafeGUI.safeSet(inv, 31,
                SafeGUI.item(
                        Material.EMERALD,
                        MoodStyle.button("Villes sponsorisées"),
                        "§8• §7Découvrir les villes mises en avant",
                        "§8• §7par les campagnes publicitaires",
                        "§8• §7Idéal pour visiter sans commande",
                        "",
                        "§e➜ §fOuvrir les publicités"
                )
        );
    }

    private static void openTownMember(Player player, Inventory inv, Town town) {

        SafeGUI.safeSet(inv, 10,
                SafeGUI.item(
                        Material.PLAYER_HEAD,
                        MoodStyle.button("Habitants"),
                        "§8• §7Liste des habitants",
                        "§8• §7Accès au spawn ville",
                        "",
                        "§e➜ §fOuvrir les habitants"
                )
        );

        SafeGUI.safeSet(inv, 12,
                SafeGUI.item(
                        Material.OAK_DOOR,
                        MoodStyle.button("Ma parcelle"),
                        "§8• §7Gérer votre terrain",
                        "§8• §7Accès joueurs et vente",
                        "",
                        "§e➜ §fOuvrir la parcelle"
                )
        );

        SafeGUI.safeSet(inv, 14,
                SafeGUI.item(
                        Material.GRASS_BLOCK,
                        MoodStyle.button("Territoire"),
                        "§8• §7Claims de la ville",
                        "§8• §7Carte et extensions",
                        "",
                        "§e➜ §fOuvrir le territoire"
                )
        );

        SafeGUI.safeSet(inv, 16,
                SafeGUI.item(
                        Material.BEACON,
                        MoodStyle.button("Nation"),
                        "§8• §7Informations nationales",
                        "§8• §7Spawn et drapeau",
                        "",
                        "§e➜ §fOuvrir la nation"
                )
        );

        SafeGUI.safeSet(inv, 21,
                SafeGUI.item(
                        Material.GOLD_INGOT,
                        MoodStyle.button("Banque municipale"),
                        "§8• §7Argent de la ville",
                        "§8• §7Dépôt et retrait",
                        "",
                        "§e➜ §fOuvrir la banque"
                )
        );

        SafeGUI.safeSet(inv, 23,
                SafeGUI.item(
                        Material.WRITABLE_BOOK,
                        MoodStyle.button("Urbanisme"),
                        "§8• §7Projets et votes",
                        "§8• §7Prestige municipal",
                        "",
                        "§e➜ §fOuvrir l'urbanisme"
                )
        );

        SafeGUI.safeSet(inv, 25, createTownFlagItem(town));

        SafeGUI.safeSet(inv, 34,
                SafeGUI.item(
                        Material.EMERALD,
                        MoodStyle.button("Publicité ville"),
                        "§8• §7Voir les villes sponsorisées",
                        "§8• §7ou mettre votre ville en avant",
                        "§8• §7depuis la banque municipale",
                        "",
                        "§e➜ §fOuvrir PubVille"
                )
        );

        if (TownyUtil.canManageTown(player)) {

            SafeGUI.safeSet(inv, 30,
                    SafeGUI.item(
                            Material.GOLDEN_HELMET,
                            MoodStyle.button("Gestion maire"),
                            "§8• §7Citoyens, ville, économie",
                            "§8• §7Outils municipaux avancés",
                            "",
                            "§e➜ §fOuvrir la mairie"
                    )
            );

            SafeGUI.safeSet(inv, 32,
                    SafeGUI.item(
                            Material.COMPARATOR,
                            MoodStyle.button("Paramètres ville"),
                            "§8• §7Règles municipales",
                            "§8• §7Permissions et taxes",
                            "",
                            "§e➜ §fOuvrir les réglages"
                    )
            );
        }
    }

    private static ItemStack createIdentityItem(Player player, Town town, Nation nation) {

        if (town == null) {
            return SafeGUI.item(
                    Material.CAMPFIRE,
                    MoodStyle.button("Aucune ville"),
                    "§8• §7Vous n'avez pas encore de ville",
                    "§8• §7Rejoindre une ville existante",
                    "§8• §7ou créer votre territoire",
                    "",
                    "§e➜ §fChoisissez une option"
            );
        }

        return SafeGUI.item(
                Material.NETHER_STAR,
                MoodStyle.button("Ville " + town.getName()),
                "§8• §7Centre municipal de §aMood§6Craft",
                "",
                MoodStyle.value("Ville", town.getName()),
                MoodStyle.value("Nation", nation == null ? "Aucune" : nation.getName())
        );
    }

    private static ItemStack createTownFlagItem(Town town) {

        if (town == null) {
            return SafeGUI.item(
                    Material.WHITE_BANNER,
                    MoodStyle.button("Drapeau municipal"),
                    "§8• §7Aucune ville liée",
                    "",
                    "§8• §7Indisponible"
            );
        }

        try {
            ItemStack shield = MoodTownFlagAPI.getTownShieldItem(town.getName());

            if (shield != null && shield.getType() != Material.AIR) {
                return shield;
            }

            ItemStack banner = MoodTownFlagAPI.getTownFlagItem(town.getName());

            if (banner != null && banner.getType() != Material.AIR) {
                return banner;
            }
        } catch (Throwable ignored) {
        }

        return SafeGUI.item(
                Material.WHITE_BANNER,
                MoodStyle.button("Drapeau municipal"),
                "§8• §7Identité visuelle de la ville",
                "§8• §7Blason et écu municipal",
                "",
                "§e➜ §fModifier le drapeau"
        );
    }
}
