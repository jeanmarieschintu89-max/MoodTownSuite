package fr.moodcraft.tgrade.gui;

import fr.moodcraft.tgrade.gui.holder.ProjectReviewHolder;
import fr.moodcraft.tgrade.model.SubmissionStatus;
import fr.moodcraft.tgrade.model.TownSubmission;
import fr.moodcraft.tgrade.util.MoodStyle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProjectReviewGUI {

    public static final String TITLE = MoodStyle.PROJECT_REVIEW_TITLE;

    private static final int[] BORDER_SLOTS = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 17,
            18, 26,
            27, 35,
            36, 37, 38, 39, 41, 42, 43, 44
    };

    public static void open(Player p, TownSubmission submission) {

        Inventory inv = Bukkit.createInventory(new ProjectReviewHolder(submission), 45, TITLE);

        fillBorders(inv);

        String statusText = statusText(submission.getStatus());
        Material statusMaterial = statusMaterial(submission.getStatus());

        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(submission.getTimestamp()));

        inv.setItem(
                4,
                MoodStyle.item(
                        Material.NETHER_STAR,
                        MoodStyle.button("Commission Urbaine"),
                        List.of(
                                MoodStyle.detail("Inspection d'un projet déposé par une ville"),
                                "",
                                MoodStyle.detail("Téléportation"),
                                MoodStyle.detail("Validation"),
                                MoodStyle.detail("Notation staff"),
                                MoodStyle.detail("Clôture des votes"),
                                "",
                                MoodStyle.detail("État : " + statusText)
                        )
                )
        );

        inv.setItem(
                13,
                MoodStyle.item(
                        statusMaterial,
                        "§6✦ §f" + submission.getBuildName() + " §6✦",
                        List.of(
                                MoodStyle.detail("Ville : §b" + submission.getTown()),
                                MoodStyle.detail("Projet : §e" + submission.getBuildName()),
                                MoodStyle.detail("État : " + statusText),
                                "",
                                MoodStyle.detail("Monde : §e" + submission.getWorld()),
                                MoodStyle.detail("Position : §eX " + submission.getX()
                                        + " Y " + submission.getY()
                                        + " Z " + submission.getZ()),
                                "",
                                MoodStyle.detail("Date : §e" + date),
                                MoodStyle.detail("ID : §8" + submission.getId()),
                                "",
                                MoodStyle.detail("Dossier national")
                        )
                )
        );

        inv.setItem(
                20,
                MoodStyle.item(
                        Material.ENDER_PEARL,
                        MoodStyle.button("Voir le projet"),
                        List.of(
                                MoodStyle.detail("Téléporte le staff à la zone déclarée"),
                                MoodStyle.detail("Ville : §b" + submission.getTown()),
                                MoodStyle.detail("Projet : §e" + submission.getBuildName()),
                                "",
                                MoodStyle.info("Inspecter sur place")
                        )
                )
        );

        inv.setItem(
                22,
                MoodStyle.item(
                        Material.LIME_CONCRETE,
                        MoodStyle.button("Valider le dossier"),
                        List.of(
                                MoodStyle.detail("Ouvre les votes"),
                                MoodStyle.detail("Autorise la notation"),
                                MoodStyle.detail("Classement hebdo"),
                                "",
                                MoodStyle.success("Ouvrir la phase de vote")
                        )
                )
        );

        inv.setItem(
                24,
                MoodStyle.item(
                        Material.ENCHANTED_BOOK,
                        MoodStyle.button("Notation staff"),
                        List.of(
                                MoodStyle.detail("Ouvre le barème réservé au staff"),
                                MoodStyle.detail("Ville : §b" + submission.getTown()),
                                MoodStyle.detail("Projet : §e" + submission.getBuildName()),
                                MoodStyle.detail("Score hebdo"),
                                "",
                                MoodStyle.info("Noter ce dossier")
                        )
                )
        );

        inv.setItem(
                26,
                MoodStyle.item(
                        Material.BEACON,
                        MoodStyle.button("Clôturer les votes"),
                        List.of(
                                MoodStyle.detail("Ferme la phase de vote"),
                                MoodStyle.detail("Note finale"),
                                MoodStyle.detail("Subvention prête"),
                                MoodStyle.detail("Notes bloquées"),
                                "",
                                MoodStyle.info("Valider la clôture")
                        )
                )
        );

        inv.setItem(
                28,
                MoodStyle.item(
                        Material.RED_CONCRETE,
                        MoodStyle.button("Refuser le dossier"),
                        List.of(
                                MoodStyle.detail("Refuse la demande de projet urbain"),
                                MoodStyle.detail("Pas de notation"),
                                MoodStyle.detail("Pas de classement"),
                                "",
                                MoodStyle.error("Refuser la demande")
                        )
                )
        );

        inv.setItem(40, MoodStyle.backItem("Demandes urbaines"));

        p.openInventory(inv);
    }

    private static void fillBorders(Inventory inv) {

        ItemStack glass = MoodStyle.glass();

        for (int slot : BORDER_SLOTS) {
            inv.setItem(slot, glass);
        }
    }

    private static String statusText(SubmissionStatus status) {

        if (status == SubmissionStatus.APPROVED) {
            return "§aValidé";
        }

        if (status == SubmissionStatus.REJECTED) {
            return "§cRefusé";
        }

        if (status == SubmissionStatus.FINISHED) {
            return "§6Clôturé";
        }

        return "§eEn examen";
    }

    private static Material statusMaterial(SubmissionStatus status) {

        if (status == SubmissionStatus.APPROVED) {
            return Material.EMERALD;
        }

        if (status == SubmissionStatus.REJECTED) {
            return Material.REDSTONE_BLOCK;
        }

        if (status == SubmissionStatus.FINISHED) {
            return Material.BEACON;
        }

        return Material.GOLD_BLOCK;
    }
}
