package fr.moodcraft.tgrade.gui;

import fr.moodcraft.tgrade.manager.GradeManager;
import fr.moodcraft.tgrade.model.SubmissionStatus;
import fr.moodcraft.tgrade.storage.SubmissionStorage;
import fr.moodcraft.tgrade.util.MoodStyle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class UrbanismeAdminGUI {

    public static final String TITLE = MoodStyle.ADMIN_TITLE;

    private static final int[] BORDER = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 17, 18, 26, 27, 35,
            36, 37, 38, 39, 41, 42, 43, 44
    };

    public static void open(Player p) {

        Inventory inv = Bukkit.createInventory(null, 45, TITLE);
        MoodStyle.fill(inv, BORDER);

        long pending = SubmissionStorage.getAll().stream()
                .filter(sub -> sub.getStatus() == SubmissionStatus.PENDING)
                .count();

        long approved = SubmissionStorage.getAll().stream()
                .filter(sub -> sub.getStatus() == SubmissionStatus.APPROVED)
                .count();

        inv.setItem(
                4,
                MoodStyle.item(
                        Material.NETHER_STAR,
                        MoodStyle.button("Centre National"),
                        List.of(
                                "§7Administration urbaine",
                                "§7de §aMood§6Craft§7.",
                                "",
                                "§7Demandes: §e" + pending,
                                "§7Projets validés: §e" + approved,
                                "§7Villes suivies: §e" + GradeManager.getAll().size(),
                                "",
                                "§8• §7Inspection",
                                "§8• §7Notation",
                                "§8• §7Subventions"
                        )
                )
        );

        inv.setItem(
                13,
                MoodStyle.item(
                        Material.WRITABLE_BOOK,
                        MoodStyle.button("Demandes Urbaines"),
                        List.of(
                                "§7Consulte les demandes",
                                "§7envoyées par les villes.",
                                "",
                                "§7En attente: §e" + pending,
                                "",
                                "§8• §7Voir le projet",
                                "§8• §7Valider",
                                "§8• §7Refuser",
                                "",
                                "§eOuvrir les demandes"
                        )
                )
        );

        inv.setItem(
                22,
                MoodStyle.item(
                        Material.ENCHANTED_BOOK,
                        MoodStyle.button("Notations"),
                        List.of(
                                "§7Note les villes ayant",
                                "§7un projet validé.",
                                "",
                                "§7Projets ouverts: §e" + approved,
                                "",
                                "§8• §7Note staff",
                                "§8• §7Clôture des votes",
                                "§8• §7Classement",
                                "",
                                "§eGérer les évaluations"
                        )
                )
        );

        inv.setItem(
                31,
                MoodStyle.item(
                        Material.EMERALD_BLOCK,
                        MoodStyle.button("Subventions Urbaines"),
                        List.of(
                                "§7Verse les subventions",
                                "§7aux villes éligibles.",
                                "",
                                "§8• §7Notes clôturées",
                                "§8• §7Classement hebdo",
                                "§8• §7Paiement ville",
                                "",
                                "§aVerser les subventions"
                        )
                )
        );

        inv.setItem(
                33,
                MoodStyle.item(
                        Material.GOLD_INGOT,
                        MoodStyle.button("Classement Hebdo"),
                        List.of(
                                "§7Consulte le classement",
                                "§7national des villes.",
                                "",
                                "§8• §7Scores",
                                "§8• §7Prestige",
                                "§8• §7Subventions",
                                "",
                                "§eVoir le classement"
                        )
                )
        );

        inv.setItem(40, MoodStyle.backItem("Commission Urbaine"));
        p.openInventory(inv);
    }
}
