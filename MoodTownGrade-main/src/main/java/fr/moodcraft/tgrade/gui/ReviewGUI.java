package fr.moodcraft.tgrade.gui;

import fr.moodcraft.tgrade.model.SubmissionStatus;
import fr.moodcraft.tgrade.model.TownSubmission;
import fr.moodcraft.tgrade.storage.SubmissionStorage;
import fr.moodcraft.tgrade.util.MoodStyle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class ReviewGUI {

    public static final String TITLE = MoodStyle.REVIEW_TITLE;

    public static void open(Player p, String town) {

        Inventory inv = Bukkit.createInventory(null, 27, TITLE);
        MoodStyle.fill(inv,
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 17,
                18, 19, 20, 21, 23, 24, 25, 26
        );

        TownSubmission project = getActiveProject(town);

        inv.setItem(
                4,
                MoodStyle.item(
                        Material.NETHER_STAR,
                        MoodStyle.button("Suivi Urbain"),
                        List.of(
                                "§7Dossier de ville.",
                                "",
                                "§7Ville: §b" + town,
                                "§7Projet: §e" + (project == null ? "Aucun" : project.getBuildName()),
                                "",
                                "§8• §7Registre national"
                        )
                )
        );

        if (project == null) {
            inv.setItem(
                    13,
                    MoodStyle.item(
                            Material.BARRIER,
                            MoodStyle.button("Aucun projet"),
                            List.of(
                                    "§7Aucun projet actif",
                                    "§7pour cette ville.",
                                    "",
                                    "§8• §7Dossier fermé"
                            )
                    )
            );
        } else {
            inv.setItem(
                    13,
                    MoodStyle.item(
                            Material.PAPER,
                            MoodStyle.button(project.getBuildName()),
                            List.of(
                                    "§7Ville: §b" + project.getTown(),
                                    "§7État: §e" + project.getStatus().getDisplay(),
                                    "",
                                    "§8• §7" + project.getWorld(),
                                    "§8• §7X " + project.getX()
                                            + "  Y " + project.getY()
                                            + "  Z " + project.getZ()
                            )
                    )
            );
        }

        inv.setItem(22, MoodStyle.backItem("Commission Urbaine"));
        p.openInventory(inv);
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
