package fr.moodcraft.townmenu.gui;

import com.palmergames.bukkit.towny.object.Nation;

import fr.moodcraft.flag.api.MoodTownFlagAPI;
import fr.moodcraft.townmenu.util.MoodStyle;
import fr.moodcraft.townmenu.util.SafeGUI;
import fr.moodcraft.townmenu.util.TownyUtil;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TownNationGUI {

    public static final String TITLE =
            MoodStyle.guiTitle("Nation");

    public static void open(Player player) {

        Inventory inv = Bukkit.createInventory(
                null,
                36,
                TITLE
        );

        SafeGUI.fill(inv, Material.BLACK_STAINED_GLASS_PANE, " ");

        Nation nation = TownyUtil.getNation(player);

        SafeGUI.safeSet(
                inv,
                4,
                SafeGUI.glow(createNationHeader(nation))
        );

        SafeGUI.safeSet(
                inv,
                11,
                SafeGUI.item(
                        Material.BEACON,
                        MoodStyle.button("Infos nation"),
                        "§7Ouvre les informations",
                        "§7de nation.",
                        "",
                        MoodStyle.bullet("Action directe")
                )
        );

        SafeGUI.safeSet(
                inv,
                13,
                SafeGUI.item(
                        Material.ENDER_PEARL,
                        MoodStyle.button("Spawn nation"),
                        "§7Téléporte au spawn",
                        "§7national.",
                        "",
                        MoodStyle.bullet("Action directe")
                )
        );

        SafeGUI.safeSet(
                inv,
                15,
                createNationFlagItem(nation)
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

    private static ItemStack createNationHeader(Nation nation) {

        if (nation == null) {
            return SafeGUI.item(
                    Material.BEACON,
                    MoodStyle.button("Aucune nation"),
                    "§7Votre ville n'est pas",
                    "§7dans une nation.",
                    "",
                    MoodStyle.bullet("Indisponible")
            );
        }

        return SafeGUI.item(
                Material.BEACON,
                MoodStyle.button("Nation " + nation.getName()),
                "§7Centre national",
                "§7de §aMood§6Craft§7.",
                "",
                MoodStyle.value("Nation", nation.getName())
        );
    }

    private static ItemStack createNationFlagItem(Nation nation) {

        if (nation == null) {
            return SafeGUI.item(
                    Material.WHITE_BANNER,
                    MoodStyle.button("Drapeau national"),
                    "§7Aucune nation liée.",
                    "",
                    MoodStyle.bullet("Indisponible")
            );
        }

        try {
            ItemStack shield = MoodTownFlagAPI.getNationShieldItem(nation.getName());

            if (shield != null && shield.getType() != Material.AIR) {
                return shield;
            }

            ItemStack banner = MoodTownFlagAPI.getNationFlagItem(nation.getName());

            if (banner != null && banner.getType() != Material.AIR) {
                return banner;
            }
        } catch (Throwable ignored) {
        }

        return SafeGUI.item(
                Material.WHITE_BANNER,
                MoodStyle.button("Drapeau national"),
                "§7Identité visuelle",
                "§7de la nation.",
                "",
                MoodStyle.bullet("Clique pour ouvrir")
        );
    }
}
