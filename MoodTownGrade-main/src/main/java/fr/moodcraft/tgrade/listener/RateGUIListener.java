package fr.moodcraft.tgrade.listener;

import fr.moodcraft.tgrade.gui.RateGUI;
import fr.moodcraft.tgrade.gui.holder.RateVoteHolder;
import fr.moodcraft.tgrade.manager.GradeManager;
import fr.moodcraft.tgrade.manager.NationalScoreCalculator;
import fr.moodcraft.tgrade.manager.RateSessionManager;
import fr.moodcraft.tgrade.model.RateSession;
import fr.moodcraft.tgrade.model.StaffVote;
import fr.moodcraft.tgrade.model.SubmissionStatus;
import fr.moodcraft.tgrade.model.TownGrade;
import fr.moodcraft.tgrade.model.TownSubmission;
import fr.moodcraft.tgrade.storage.SubmissionStorage;
import fr.moodcraft.tgrade.storage.VoteStorage;
import fr.moodcraft.tgrade.util.MoodStyle;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class RateGUIListener
        implements Listener {

    @EventHandler
    public void click(
            InventoryClickEvent e
    ) {

        if (!MoodStyle.titleEquals(e.getView().getTitle(), RateGUI.TITLE)) {
            return;
        }

        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player p)) {
            return;
        }

        if (e.getRawSlot() < 0
                || e.getRawSlot() >= e.getView().getTopInventory().getSize()) {

            return;
        }

        String town =
                getTownFromMenu(e, p);

        if (town == null
                || town.isBlank()) {

            deny(
                    p,
                    "§cVille introuvable.",
                    "§7Le menu ne contient aucune ville valide."
            );

            return;
        }

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

        TownGrade grade =
                GradeManager.get(town);

        if (grade != null
                && grade.isLocked()) {

            p.closeInventory();

            p.playSound(
                    p.getLocation(),
                    Sound.ENTITY_VILLAGER_NO,
                    1f,
                    1f
            );

            p.sendMessage("");
            p.sendMessage("§8----- §6✦ Commission Urbaine ✦ §8-----");
            p.sendMessage("");
            p.sendMessage("§cVotes clôturés.");
            p.sendMessage("");
            p.sendMessage("§7Ville: §b" + town);
            p.sendMessage("§7Projet: §e" + projectName);
            p.sendMessage("");
            p.sendMessage("§8• §7Ce dossier ne reçoit plus de notes");
            p.sendMessage("§8-----------------------------");

            RateSessionManager.remove(
                    p.getUniqueId()
            );

            return;
        }

        int slot =
                e.getRawSlot();

        switch (slot) {

            case RateGUI.ARCHI -> session.setArchitecture(
                    next(
                            session.getArchitecture(),
                            10
                    )
            );

            case RateGUI.COHERENCE -> session.setCoherence(
                    next(
                            session.getCoherence(),
                            6
                    )
            );

            case RateGUI.ACTIVITE -> session.setActivite(
                    next(
                            session.getActivite(),
                            8
                    )
            );

            case RateGUI.BANQUE -> session.setBanque(
                    next(
                            session.getBanque(),
                            4
                    )
            );

            case RateGUI.BUILD -> session.setBuild(
                    next(
                            session.getBuild(),
                            8
                    )
            );

            case RateGUI.RP -> session.setRoleplay(
                    next(
                            session.getRoleplay(),
                            6
                    )
            );

            case RateGUI.TAILLE -> session.setTaille(
                    next(
                            session.getTaille(),
                            3
                    )
            );

            case RateGUI.VOTES -> session.setVotes(
                    next(
                            session.getVotes(),
                            5
                    )
            );

            case RateGUI.SAVE -> {
                saveStaffVote(p, town, projectName, session);
                return;
            }

            default -> {
                return;
            }
        }

        p.playSound(
                p.getLocation(),
                Sound.UI_BUTTON_CLICK,
                1f,
                1f
        );

        RateGUI.open(
                p,
                town
        );
    }

    private static String getTownFromMenu(
            InventoryClickEvent e,
            Player p
    ) {

        if (e.getView()
                .getTopInventory()
                .getHolder() instanceof RateVoteHolder holder) {

            return holder.getTown();
        }

        RateSession session =
                RateSessionManager.get(
                        p.getUniqueId()
                );

        if (session == null) {
            return null;
        }

        return session.getTown();
    }

    private static void saveStaffVote(
            Player p,
            String town,
            String projectName,
            RateSession session
    ) {

        StaffVote vote =
                new StaffVote(
                        p.getUniqueId(),
                        town
                );

        vote.setArchitecture(
                session.getArchitecture()
        );

        vote.setStyle(
                session.getCoherence()
        );

        vote.setActivite(
                session.getActivite()
        );

        vote.setBanque(
                session.getBanque()
        );

        vote.setRemarquable(
                session.getBuild()
        );

        vote.setRp(
                session.getRoleplay()
        );

        vote.setTaille(
                session.getTaille()
        );

        vote.setVotes(
                session.getVotes()
        );

        VoteStorage.saveStaffVote(vote);

        double staffScore =
                NationalScoreCalculator.getStaffScore(town);

        double mayorScore =
                NationalScoreCalculator.getMayorScore(town);

        double citizenScore =
                NationalScoreCalculator.getCitizenScore(town);

        double finalScore =
                NationalScoreCalculator.getFinalScore(town);

        RateSessionManager.remove(
                p.getUniqueId()
        );

        p.closeInventory();

        p.playSound(
                p.getLocation(),
                Sound.UI_TOAST_CHALLENGE_COMPLETE,
                1f,
                1f
        );

        p.sendMessage("");
        p.sendMessage("§8----- §6✦ Commission Urbaine ✦ §8-----");
        p.sendMessage("");
        p.sendMessage("§a✔ §fNotation staff enregistrée.");
        p.sendMessage("");
        p.sendMessage("§7Ville: §b" + town);
        p.sendMessage("§7Projet: §e" + projectName);
        p.sendMessage("§7Note staff: §e" + oneDecimal(staffScore) + "/50");
        p.sendMessage("§7Score national: §e" + oneDecimal(finalScore) + "/50");
        p.sendMessage("");
        p.sendMessage("§8• §7Maires: §e" + oneDecimal(mayorScore));
        p.sendMessage("§8• §7Citoyens: §e" + oneDecimal(citizenScore));
        p.sendMessage("§8• §7Classement hebdo actualisé");
        p.sendMessage("§8-----------------------------");
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

    private static int next(
            int value,
            int max
    ) {

        value++;

        if (value > max) {
            value = 0;
        }

        return value;
    }

    private static void deny(
            Player p,
            String message,
            String detail
    ) {

        p.playSound(
                p.getLocation(),
                Sound.ENTITY_VILLAGER_NO,
                1f,
                1f
        );

        p.sendMessage("");
        p.sendMessage("§8----- §6✦ Commission Urbaine ✦ §8-----");
        p.sendMessage("");
        p.sendMessage(message);
        p.sendMessage(detail);
        p.sendMessage("");
        p.sendMessage("§8• §7Service officiel de §aMood§6Craft");
        p.sendMessage("§8-----------------------------");
    }

    private static String oneDecimal(
            double value
    ) {

        return String.format(
                "%.1f",
                value
        );
    }
}
