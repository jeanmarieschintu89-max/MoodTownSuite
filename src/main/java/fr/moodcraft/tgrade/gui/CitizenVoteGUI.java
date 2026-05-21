package fr.moodcraft.tgrade.gui;

import fr.moodcraft.flag.api.MoodTownFlagAPI;
import fr.moodcraft.tgrade.gui.holder.TownVoteHolder;
import fr.moodcraft.tgrade.manager.CitizenVoteManager;
import fr.moodcraft.tgrade.manager.NationalScoreCalculator;
import fr.moodcraft.tgrade.model.CitizenVote;
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

import java.util.List;

public class CitizenVoteGUI {

    public static final String TITLE = MoodStyle.CITIZEN_VOTE_TITLE;

    public static final int BEAUTE = 20;
    public static final int AMBIANCE = 21;
    public static final int ACTIVITE = 22;
    public static final int ORIGINALITE = 23;
    public static final int POPULARITE = 24;
    public static final int BACK = 36;
    public static final int SAVE = 40;
    public static final int TP_PROJECT = 44;

    public static final int MAX_NOTE = 3;
    public static final int MAX_TOTAL = 15;

    private static final int[] BORDER = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 17, 18, 26, 27, 35,
            37, 38, 39, 41, 42, 43
    };

    public static void open(Player p, String town) {

        Inventory inv = Bukkit.createInventory(new TownVoteHolder(town), 45, TITLE);
        MoodStyle.fill(inv, BORDER);

        CitizenVote vote = CitizenVoteManager.getVote(p.getUniqueId(), town);

        if (vote == null) {
            vote = new CitizenVote(p.getUniqueId(), town);
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
            meta.setDisplayName(MoodStyle.button("Vote Citoyen"));
            meta.setLore(List.of(
                    "§7Ville: §b" + town,
                    "§7Projet: §e" + projectName,
                    "§7Score actuel: §e" + String.format("%.1f", NationalScoreCalculator.getFinalScore(town)) + "/50",
                    "",
                    "§8• §7Visitez le projet",
                    "§8• §7Ajustez les notes",
                    "§8• §7Validez le vote"
            ));
            icon.setItemMeta(meta);
        }

        inv.setItem(13, icon);

        setVote(inv, BEAUTE, Material.POPPY, "Visuel", "Beauté de la ville", vote.getBeaute());
        setVote(inv, AMBIANCE, Material.CAMPFIRE, "Ambiance", "Vie et atmosphère", vote.getAmbiance());
        setVote(inv, ACTIVITE, Material.EMERALD, "Activité", "Ville vivante", vote.getActivite());
        setVote(inv, ORIGINALITE, Material.AMETHYST_SHARD, "Originalité", "Idées et identité", vote.getOriginalite());
        setVote(inv, POPULARITE, Material.PAPER, "Avis global", "Impression générale", vote.getPopularite());

        inv.setItem(BACK, MoodStyle.backItem("Votes Citoyens"));
        inv.setItem(
                SAVE,
                MoodStyle.item(
                        Material.LIME_CONCRETE,
                        MoodStyle.button("Valider le vote"),
                        List.of(
                                "§7Enregistre votre avis",
                                "§7dans le classement.",
                                "",
                                "§8• §7Vote citoyen",
                                "§8• §7Barème /15",
                                "§8• §7Score hebdo",
                                "",
                                "§aSauvegarder"
                        )
                )
        );
        inv.setItem(
                TP_PROJECT,
                MoodStyle.item(
                        Material.ENDER_PEARL,
                        MoodStyle.button("Visiter le projet"),
                        List.of(
                                "§7Téléporte vers",
                                "§7la zone du projet.",
                                "",
                                "§8• §7Ville: §b" + town,
                                "§8• §7Projet: §e" + projectName
                        )
                )
        );

        p.openInventory(inv);
    }

    private static void setVote(
            Inventory inv,
            int slot,
            Material material,
            String name,
            String description,
            int value
    ) {

        inv.setItem(
                slot,
                MoodStyle.item(
                        material,
                        MoodStyle.button(name),
                        List.of(
                                "§7" + description + ".",
                                "",
                                "§7Note: §e" + value + "/" + MAX_NOTE,
                                "",
                                "§8• §7Cliquer pour ajuster"
                        )
                )
        );
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
