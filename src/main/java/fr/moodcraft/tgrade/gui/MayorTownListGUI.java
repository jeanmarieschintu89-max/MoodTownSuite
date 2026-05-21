package fr.moodcraft.tgrade.gui;

import fr.moodcraft.flag.api.MoodTownFlagAPI;
import fr.moodcraft.tgrade.manager.NationalScoreCalculator;
import fr.moodcraft.tgrade.manager.RankingManager;
import fr.moodcraft.tgrade.model.SubmissionStatus;
import fr.moodcraft.tgrade.model.TownSubmission;
import fr.moodcraft.tgrade.storage.SubmissionStorage;
import fr.moodcraft.tgrade.util.MoodStyle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MayorTownListGUI {

    public static final String TITLE = MoodStyle.MAYOR_LIST_TITLE;

    private static final int[] BORDER = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 17, 18, 26, 27, 35, 36, 44,
            45, 46, 47, 48, 50, 51, 52, 53
    };

    private static final int[] TOWN_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    public static void open(Player p) {

        Inventory inv = Bukkit.createInventory(null, 54, TITLE);
        MoodStyle.fill(inv, BORDER);

        inv.setItem(
                4,
                MoodStyle.item(
                        Material.GOLD_BLOCK,
                        MoodStyle.button("Conseil des Maires"),
                        List.of(
                                "§7Vote municipal sur",
                                "§7les projets validés.",
                                "",
                                "§8• §7Avis des maires",
                                "§8• §7Classement hebdo",
                                "§8• §7Prestige urbain"
                        )
                )
        );

        List<String> towns = approvedTowns();

        if (towns.isEmpty()) {
            inv.setItem(
                    22,
                    MoodStyle.item(
                            Material.BARRIER,
                            MoodStyle.button("Aucun vote ouvert"),
                            List.of(
                                    "§7Aucune ville n'a",
                                    "§7de projet validé.",
                                    "",
                                    "§8• §7Revenez plus tard"
                            )
                    )
            );
        }

        int index = 0;

        for (String town : towns) {
            if (index >= TOWN_SLOTS.length) {
                break;
            }

            TownSubmission project = getActiveProject(town);
            String projectName = project == null ? "Projet en cours" : project.getBuildName();

            ItemStack icon = null;

            try {
                icon = MoodTownFlagAPI.getTownShieldItem(town);
            } catch (Throwable ignored) {
                icon = null;
            }

            if (icon == null) {
                icon = new ItemStack(Material.SHIELD);
            }

            ItemMeta meta = icon.getItemMeta();

            if (meta != null) {
                meta.setDisplayName(MoodStyle.button(town));
                meta.setLore(List.of(
                        "§7Ville: §b" + town,
                        "§7Projet: §e" + projectName,
                        "§7Score: §e" + String.format("%.1f", NationalScoreCalculator.getFinalScore(town)) + "/50",
                        "§7Rang: §e#" + RankingManager.getPosition(town),
                        "§7Votes maires: §e" + NationalScoreCalculator.getMayorCount(town),
                        "",
                        "§8• §7Consulter et voter"
                ));
                icon.setItemMeta(meta);
            }

            MoodStyle.tag(icon, MoodStyle.TAG_TOWN, town);
            inv.setItem(TOWN_SLOTS[index], icon);
            index++;
        }

        inv.setItem(49, MoodStyle.backItem("Commission Urbaine"));
        p.openInventory(inv);
    }

    private static List<String> approvedTowns() {

        Set<String> towns = new HashSet<>();

        for (TownSubmission submission : SubmissionStorage.getAll()) {
            if (submission.getStatus() == SubmissionStatus.APPROVED) {
                towns.add(submission.getTown());
            }
        }

        List<String> sorted = new ArrayList<>(towns);
        sorted.sort(String.CASE_INSENSITIVE_ORDER);
        return sorted;
    }

    private static TownSubmission getActiveProject(String town) {

        for (TownSubmission submission : SubmissionStorage.getAll()) {
            if (submission.getTown().equalsIgnoreCase(town)
                    && submission.getStatus() == SubmissionStatus.APPROVED) {
                return submission;
            }
        }

        return null;
    }
}
