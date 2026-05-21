package fr.moodcraft.tgrade.gui;

import fr.moodcraft.flag.api.MoodTownFlagAPI;
import fr.moodcraft.tgrade.manager.GradeManager;
import fr.moodcraft.tgrade.model.SubmissionStatus;
import fr.moodcraft.tgrade.model.TownGrade;
import fr.moodcraft.tgrade.model.TownSubmission;
import fr.moodcraft.tgrade.storage.SubmissionStorage;
import fr.moodcraft.tgrade.util.MoodStyle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EvaluationManagerGUI {

    public static final String TITLE =
            MoodStyle.EVALUATION_TITLE;

    private static final int[] BORDER_SLOTS = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 17,
            18, 26,
            27, 35,
            36, 44,
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

        fillBorders(inv);

        inv.setItem(
                4,
                MoodStyle.item(
                        Material.ENCHANTED_BOOK,
                        MoodStyle.button("Notation staff"),
                        MoodStyle.detail("Choisis une ville avec un projet validé"),
                        MoodStyle.detail("Dossier urbain"),
                        MoodStyle.detail("Note staff"),
                        MoodStyle.detail("Classement hebdo"),
                        "",
                        MoodStyle.info("Ouvrir un dossier")
                )
        );

        List<String> towns = getApprovedTowns();

        if (towns.isEmpty()) {

            inv.setItem(
                    22,
                    MoodStyle.item(
                            Material.BARRIER,
                            MoodStyle.button("Aucun dossier"),
                            MoodStyle.detail("Aucune ville n'a de projet validé"),
                            "",
                            MoodStyle.detail("Validation requise")
                    )
            );

            inv.setItem(49, backItem());
            p.openInventory(inv);
            return;
        }

        int index = 0;

        for (String town : towns) {

            if (index >= TOWN_SLOTS.length) {
                break;
            }

            TownSubmission project = getActiveProject(town);

            String projectName = project == null
                    ? "Projet en cours"
                    : project.getBuildName();

            TownGrade grade = GradeManager.get(town);

            String score = grade == null
                    ? "§70/50"
                    : grade.getFormattedScore();

            ItemStack icon = null;
            boolean hasFlag = false;

            try {
                icon = MoodTownFlagAPI.getTownShieldItem(town);
                hasFlag = icon != null;
            } catch (Throwable ignored) {
                icon = null;
            }

            if (icon == null) {
                icon = new ItemStack(Material.SHIELD);
            }

            ItemMeta meta = icon.getItemMeta();

            if (meta != null) {

                meta.setDisplayName("§6✦ §f" + town + " §6✦");

                List<String> lore = new ArrayList<>();
                lore.add(MoodStyle.detail("Ville : §b" + town));
                lore.add(MoodStyle.detail("Projet : §e" + projectName));
                lore.add(MoodStyle.detail("Score : " + score));
                lore.add("");
                lore.add(hasFlag
                        ? MoodStyle.detail("Blason enregistré")
                        : MoodStyle.detail("Blason non défini"));
                lore.add(MoodStyle.detail("Inspection staff"));
                lore.add(MoodStyle.detail("Notation hebdo"));
                lore.add("");
                lore.add(MoodStyle.info("Ouvrir le dossier"));

                meta.setLore(lore);
                meta.addItemFlags(
                        ItemFlag.HIDE_ADDITIONAL_TOOLTIP,
                        ItemFlag.HIDE_ATTRIBUTES,
                        ItemFlag.HIDE_ENCHANTS
                );

                icon.setItemMeta(meta);
            }

            MoodStyle.tag(icon, MoodStyle.TAG_TOWN, town);
            inv.setItem(TOWN_SLOTS[index], icon);
            index++;
        }

        inv.setItem(49, backItem());
        p.openInventory(inv);
    }

    private static void fillBorders(Inventory inv) {

        ItemStack glass = MoodStyle.glass();

        for (int slot : BORDER_SLOTS) {
            inv.setItem(slot, glass);
        }
    }

    private static ItemStack backItem() {
        return MoodStyle.backItem("Centre National");
    }

    private static List<String> getApprovedTowns() {

        Set<String> towns = new HashSet<>();

        for (TownSubmission submission : SubmissionStorage.getAll()) {

            if (submission.getStatus() != SubmissionStatus.APPROVED) {
                continue;
            }

            towns.add(submission.getTown());
        }

        List<String> sorted = new ArrayList<>(towns);
        sorted.sort(String.CASE_INSENSITIVE_ORDER);
        return sorted;
    }

    private static TownSubmission getActiveProject(String town) {

        TownSubmission fallback = null;

        for (TownSubmission submission : SubmissionStorage.getAll()) {

            if (!submission.getTown().equalsIgnoreCase(town)) {
                continue;
            }

            if (submission.getStatus() == SubmissionStatus.APPROVED) {
                return submission;
            }

            fallback = submission;
        }

        return fallback;
    }
}
