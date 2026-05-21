package fr.moodcraft.tgrade.gui;

import fr.moodcraft.flag.api.MoodTownFlagAPI;
import fr.moodcraft.tgrade.manager.NationalScoreCalculator;
import fr.moodcraft.tgrade.manager.RankingManager;
import fr.moodcraft.tgrade.model.TownGrade;
import fr.moodcraft.tgrade.util.MoodStyle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClassementGUI {

    public static final String TITLE = MoodStyle.CLASSEMENT_TITLE;

    private static final int[] BORDER = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 17, 18, 26, 27, 35, 36, 44,
            45, 46, 47, 48, 50, 51, 52, 53
    };

    private static final int[] RANK_SLOTS = {
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
                        Material.GOLD_INGOT,
                        MoodStyle.button("Classement Hebdo"),
                        List.of(
                                "§7Classement national",
                                "§7des villes évaluées.",
                                "",
                                "§7Score moyen: §e" + String.format("%.1f", RankingManager.getAverageScore()),
                                "§7Villes classées: §e" + RankingManager.getFinishedTowns(),
                                "§7Prestige total: §e" + RankingManager.getTotalPrestige(),
                                "",
                                "§8• §7Subventions",
                                "§8• §7Prestige"
                        )
                )
        );

        List<TownGrade> grades = new ArrayList<>(RankingManager.getTop());
        grades.sort(Comparator.comparingDouble((TownGrade grade) -> NationalScoreCalculator.getFinalScore(grade.getTown())).reversed());

        if (grades.isEmpty()) {
            inv.setItem(
                    22,
                    MoodStyle.item(
                            Material.BARRIER,
                            MoodStyle.button("Aucun classement"),
                            List.of(
                                    "§7Aucune ville n'est",
                                    "§7encore classée.",
                                    "",
                                    "§8• §7Votes requis"
                            )
                    )
            );
        }

        int index = 0;

        for (TownGrade grade : grades) {
            if (index >= RANK_SLOTS.length) {
                break;
            }

            String town = grade.getTown();
            double score = NationalScoreCalculator.getFinalScore(town);
            int position = index + 1;

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
                meta.setDisplayName(MoodStyle.button("#" + position + " " + town));
                meta.setLore(List.of(
                        "§7Ville: §b" + town,
                        "§7Score: §e" + String.format("%.1f", score) + "/50",
                        "§7Rang: §6" + grade.getRank(),
                        "§7Subvention: §e" + grade.getPayout() + "€",
                        "",
                        "§8• §7Staff: §e" + String.format("%.1f", NationalScoreCalculator.getStaffScore(town)),
                        "§8• §7Maires: §e" + String.format("%.1f", NationalScoreCalculator.getMayorScore(town)),
                        "§8• §7Citoyens: §e" + String.format("%.1f", NationalScoreCalculator.getCitizenScore(town))
                ));
                icon.setItemMeta(meta);
            }

            inv.setItem(RANK_SLOTS[index], icon);
            index++;
        }

        inv.setItem(49, MoodStyle.backItem("Commission Urbaine"));
        p.openInventory(inv);
    }
}
