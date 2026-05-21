package fr.moodcraft.tgrade.gui;

import fr.moodcraft.tgrade.manager.RankingManager;
import fr.moodcraft.tgrade.model.SubmissionStatus;
import fr.moodcraft.tgrade.model.TownGrade;
import fr.moodcraft.tgrade.storage.SubmissionStorage;
import fr.moodcraft.tgrade.towny.TownyHook;
import fr.moodcraft.tgrade.util.MoodStyle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class UrbanismeMainGUI {

    public static final String TITLE = MoodStyle.MAIN_TITLE;

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

        TownGrade best = RankingManager.getBest();
        String bestTown = best == null ? "Aucune" : best.getTown();

        boolean canManage = TownyHook.canManage(p);
        boolean staff = p.hasPermission("moodtowngrade.staff");

        inv.setItem(
                4,
                MoodStyle.item(
                        Material.NETHER_STAR,
                        MoodStyle.button("Commission Urbaine"),
                        List.of(
                                MoodStyle.detail("Projets, votes et classement"),
                                MoodStyle.detail("Meilleure ville : §e" + bestTown),
                                MoodStyle.detail("Projets à voter : §e" + approved),
                                MoodStyle.detail("Demandes staff : §e" + pending),
                                "",
                                MoodStyle.info("Choisissez une action simple")
                        )
                )
        );

        inv.setItem(
                11,
                MoodStyle.item(
                        canManage ? Material.WRITABLE_BOOK : Material.GRAY_DYE,
                        MoodStyle.button("Déposer un projet"),
                        List.of(
                                canManage
                                        ? MoodStyle.detail("Envoie un projet de ta ville")
                                        : MoodStyle.detail("Réservé aux maires et assistants"),
                                MoodStyle.detail("Position prise automatiquement"),
                                MoodStyle.detail("Nom + description dans le chat"),
                                "",
                                canManage
                                        ? MoodStyle.info("Commencer le dépôt")
                                        : MoodStyle.error("Accès indisponible")
                        )
                )
        );

        inv.setItem(
                13,
                MoodStyle.item(
                        Material.EMERALD,
                        MoodStyle.button("Voter"),
                        List.of(
                                MoodStyle.detail("Vote pour les projets validés"),
                                canManage
                                        ? MoodStyle.detail("Maire : conseil des maires")
                                        : MoodStyle.detail("Joueur : vote citoyen"),
                                MoodStyle.detail("Aide le classement hebdo"),
                                "",
                                MoodStyle.info("Ouvrir les votes")
                        )
                )
        );

        inv.setItem(
                15,
                MoodStyle.item(
                        Material.GOLD_INGOT,
                        MoodStyle.button("Classement"),
                        List.of(
                                MoodStyle.detail("Scores des villes"),
                                MoodStyle.detail("Prestige et subventions"),
                                MoodStyle.detail("Classement hebdomadaire"),
                                "",
                                MoodStyle.info("Voir le classement")
                        )
                )
        );

        if (staff) {
            inv.setItem(
                    31,
                    MoodStyle.item(
                            Material.COMPASS,
                            MoodStyle.button("Administration"),
                            List.of(
                                    MoodStyle.detail("Demandes à valider"),
                                    MoodStyle.detail("Notes staff"),
                                    MoodStyle.detail("Subventions"),
                                    "",
                                    MoodStyle.info("Ouvrir le centre national")
                            )
                    )
            );
        }

        inv.setItem(40, MoodStyle.backItem("principal"));
        p.openInventory(inv);
    }
}
