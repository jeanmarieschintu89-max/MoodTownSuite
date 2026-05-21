package fr.moodcraft.tgrade.gui;

import fr.moodcraft.flag.api.MoodTownFlagAPI;
import fr.moodcraft.tgrade.gui.holder.RateVoteHolder;
import fr.moodcraft.tgrade.manager.GradeManager;
import fr.moodcraft.tgrade.manager.RateSessionManager;
import fr.moodcraft.tgrade.model.RateSession;
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
import java.util.List;

public class RateGUI {

    public static final String TITLE =
            MoodStyle.RATE_TITLE;

    public static final int ARCHI = 10;
    public static final int COHERENCE = 12;
    public static final int ACTIVITE = 14;
    public static final int BANQUE = 16;
    public static final int BUILD = 28;
    public static final int RP = 30;
    public static final int TAILLE = 32;
    public static final int VOTES = 34;
    public static final int SAVE = 49;

    public static void open(
            Player p,
            String town
    ) {

        TownGrade grade =
                GradeManager.get(town);

        RateSession session =
                RateSessionManager.create(
                        p.getUniqueId(),
                        town
                );

        TownSubmission project =
                getActiveProject(town);

        String projectName =
                project == null
                        ? "Projet en cours"
                        : project.getBuildName();

        Inventory inv = Bukkit.createInventory(
                new RateVoteHolder(town),
                54,
                TITLE
        );

        fill(inv);

        ItemStack shield =
                null;

        boolean hasFlag = false;

        try {
            shield = MoodTownFlagAPI.getTownShieldItem(town);
            hasFlag = shield != null;
        } catch (Throwable ignored) {
            shield = null;
        }

        if (shield == null) {
            shield = new ItemStack(Material.SHIELD);
        }

        ItemMeta shieldMeta =
                shield.getItemMeta();

        if (shieldMeta != null) {

            shieldMeta.setDisplayName(
                    "§6✦ §fNotation staff §6✦"
            );

            List<String> lore =
                    new ArrayList<>();

            lore.add("§7Ville: §b" + town);
            lore.add("§7Projet: §e" + projectName);
            lore.add("");

            if (hasFlag) {
                lore.add("§8• §7Blason enregistré");
            } else {
                lore.add("§8• §7Blason non défini");
            }

            lore.add("§8• §7Note staff");
            lore.add("§8• §7Classement hebdo");
            lore.add("");
            lore.add("§7Note actuelle:");
            lore.add(grade == null ? "§70/50" : grade.getFormattedScore());
            lore.add("");
            lore.add("§eClique un critère");

            shieldMeta.setLore(lore);
            shieldMeta.addItemFlags(
                    ItemFlag.HIDE_ADDITIONAL_TOOLTIP,
                    ItemFlag.HIDE_ATTRIBUTES,
                    ItemFlag.HIDE_ENCHANTS
            );

            shield.setItemMeta(shieldMeta);
        }

        inv.setItem(
                4,
                shield
        );

        set(
                inv,
                ARCHI,
                Material.QUARTZ_BLOCK,
                "Architecture",
                "Qualité visuelle",
                "Intégration du projet",
                session.getArchitecture(),
                10
        );

        set(
                inv,
                COHERENCE,
                Material.BOOKSHELF,
                "Cohérence",
                "Style général",
                "Logique urbaine",
                session.getCoherence(),
                6
        );

        set(
                inv,
                ACTIVITE,
                Material.CLOCK,
                "Activité",
                "Vie de la ville",
                "Présence des habitants",
                session.getActivite(),
                8
        );

        set(
                inv,
                BANQUE,
                Material.EMERALD,
                "Banque",
                "Gestion économique",
                "Stabilité financière",
                session.getBanque(),
                4
        );

        set(
                inv,
                BUILD,
                Material.BRICKS,
                "Build remarquable",
                "Éléments forts",
                "Qualité du projet",
                session.getBuild(),
                8
        );

        set(
                inv,
                RP,
                Material.WRITABLE_BOOK,
                "Roleplay",
                "Identité de ville",
                "Cohérence RP",
                session.getRoleplay(),
                6
        );

        set(
                inv,
                TAILLE,
                Material.GRASS_BLOCK,
                "Développement",
                "Progression urbaine",
                "Taille du territoire",
                session.getTaille(),
                3
        );

        set(
                inv,
                VOTES,
                Material.DIAMOND,
                "Participation",
                "Votes citoyens",
                "Conseil des maires",
                session.getVotes(),
                5
        );

        inv.setItem(
                SAVE,
                item(
                        Material.LIME_CONCRETE,
                        "§6✦ §fValider la notation §6✦",
                        List.of(
                                "§7Enregistre la note",
                                "§7dans le dossier hebdo.",
                                "",
                                "§8• §7Ville: §b" + town,
                                "§8• §7Projet: §e" + projectName,
                                "§8• §7Total: §e" + session.getTotal() + "/50",
                                "",
                                "§aSauvegarder"
                        )
                )
        );

        p.openInventory(inv);
    }

    private static void fill(
            Inventory inv
    ) {

        ItemStack glass =
                item(
                        Material.BLACK_STAINED_GLASS_PANE,
                        " ",
                        null
                );

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, glass);
        }
    }

    private static void set(
            Inventory inv,
            int slot,
            Material material,
            String title,
            String line1,
            String line2,
            int value,
            int max
    ) {

        inv.setItem(
                slot,
                item(
                        material,
                        "§6✦ §f" + title + " §6✦",
                        List.of(
                                "§7" + line1,
                                "§7" + line2,
                                "",
                                "§7Note: §e" + value + "§7/§e" + max,
                                "§7Impact: §eClassement hebdo",
                                "",
                                "§8• §7Clic: +1",
                                "§8• §7Après " + max + ", retour à 0"
                        )
                )
        );
    }

    private static ItemStack item(
            Material material,
            String name,
            List<String> lore
    ) {

        ItemStack item =
                new ItemStack(material);

        ItemMeta meta =
                item.getItemMeta();

        if (meta != null) {

            meta.setDisplayName(name);

            if (lore != null) {
                meta.setLore(lore);
            }

            meta.addItemFlags(
                    ItemFlag.HIDE_ADDITIONAL_TOOLTIP,
                    ItemFlag.HIDE_ATTRIBUTES,
                    ItemFlag.HIDE_ENCHANTS
            );

            item.setItemMeta(meta);
        }

        return item;
    }

    private static TownSubmission getActiveProject(
            String town
    ) {

        TownSubmission fallback =
                null;

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
