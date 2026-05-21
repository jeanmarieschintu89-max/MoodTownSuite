package fr.moodcraft.tgrade.gui;

import fr.moodcraft.tgrade.model.SubmissionStatus;
import fr.moodcraft.tgrade.model.TownSubmission;
import fr.moodcraft.tgrade.storage.SubmissionStorage;
import fr.moodcraft.tgrade.util.MoodStyle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.List;

public class PendingProjectsGUI {

    public static final String TITLE = MoodStyle.PENDING_TITLE;

    private static final int[] BORDER = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 17, 18, 26, 27, 35, 36, 44,
            45, 46, 47, 48, 50, 51, 52, 53
    };

    private static final int[] PROJECT_SLOTS = {
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
                        Material.NETHER_STAR,
                        MoodStyle.button("Demandes Urbaines"),
                        List.of(
                                "§7Dossiers envoyés",
                                "§7à la Commission.",
                                "",
                                "§8• §7Inspection",
                                "§8• §7Validation",
                                "§8• §7Refus"
                        )
                )
        );

        List<TownSubmission> submissions = SubmissionStorage.getAll().stream()
                .filter(sub -> sub.getStatus() == SubmissionStatus.PENDING)
                .sorted(Comparator.comparing(TownSubmission::getTown, String.CASE_INSENSITIVE_ORDER))
                .toList();

        if (submissions.isEmpty()) {
            inv.setItem(
                    22,
                    MoodStyle.item(
                            Material.BARRIER,
                            MoodStyle.button("Aucune demande"),
                            List.of(
                                    "§7Aucun dossier",
                                    "§7en attente.",
                                    "",
                                    "§8• §7Registre vide"
                            )
                    )
            );
        }

        int index = 0;

        for (TownSubmission submission : submissions) {
            if (index >= PROJECT_SLOTS.length) {
                break;
            }

            ItemStack item = MoodStyle.item(
                    Material.PAPER,
                    MoodStyle.button(submission.getBuildName()),
                    List.of(
                            "§7Ville: §b" + submission.getTown(),
                            "§7Projet: §e" + submission.getBuildName(),
                            "§7État: §eEn attente",
                            "",
                            "§8• §7" + submission.getWorld(),
                            "§8• §7X " + submission.getX()
                                    + "  Y " + submission.getY()
                                    + "  Z " + submission.getZ(),
                            "",
                            "§eInspecter la demande"
                    )
            );

            MoodStyle.tag(item, MoodStyle.TAG_SUBMISSION_ID, submission.getId());
            inv.setItem(PROJECT_SLOTS[index], item);
            index++;
        }

        inv.setItem(49, MoodStyle.backItem("Centre National"));
        p.openInventory(inv);
    }
}
