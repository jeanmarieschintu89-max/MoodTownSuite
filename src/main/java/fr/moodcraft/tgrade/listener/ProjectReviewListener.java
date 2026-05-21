package fr.moodcraft.tgrade.listener;

import fr.moodcraft.tgrade.gui.PendingProjectsGUI;
import fr.moodcraft.tgrade.gui.ProjectReviewGUI;
import fr.moodcraft.tgrade.gui.RateGUI;
import fr.moodcraft.tgrade.gui.holder.ProjectReviewHolder;
import fr.moodcraft.tgrade.manager.GradeManager;
import fr.moodcraft.tgrade.manager.NationalScoreCalculator;
import fr.moodcraft.tgrade.model.SubmissionStatus;
import fr.moodcraft.tgrade.model.TownGrade;
import fr.moodcraft.tgrade.model.TownSubmission;
import fr.moodcraft.tgrade.storage.SubmissionStorage;
import fr.moodcraft.tgrade.util.MoodStyle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ProjectReviewListener implements Listener {

    @EventHandler
    public void click(InventoryClickEvent e) {

        if (!MoodStyle.titleEquals(e.getView().getTitle(), ProjectReviewGUI.TITLE)) {
            return;
        }

        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player p)) {
            return;
        }

        if (e.getRawSlot() < 0 || e.getRawSlot() >= e.getView().getTopInventory().getSize()) {
            return;
        }

        if (!(e.getView().getTopInventory().getHolder() instanceof ProjectReviewHolder holder)) {
            deny(p, "Dossier introuvable.", "Le menu ne contient aucun dossier valide.");
            return;
        }

        TownSubmission submission = refresh(holder.getSubmission());

        if (submission == null) {
            deny(p, "Projet introuvable.", "Le dossier n'existe plus dans les registres.");
            return;
        }

        int slot = e.getRawSlot();

        if (slot == 20) {
            teleportToProject(p, submission);
            return;
        }

        if (slot == 22) {
            approve(p, submission);
            return;
        }

        if (slot == 24) {
            openStaffRate(p, submission);
            return;
        }

        if (slot == 26) {
            closeVotes(p, submission);
            return;
        }

        if (slot == 28) {
            reject(p, submission);
            return;
        }

        if (slot == 40) {
            MoodStyle.click(p);
            PendingProjectsGUI.open(p);
        }
    }

    private static TownSubmission refresh(TownSubmission submission) {

        if (submission == null) {
            return null;
        }

        TownSubmission stored = SubmissionStorage.get(submission.getId());

        if (stored == null) {
            return submission;
        }

        return stored;
    }

    private static void teleportToProject(Player p, TownSubmission submission) {

        World world = Bukkit.getWorld(submission.getWorld());

        if (world == null) {
            deny(p, "Monde introuvable.", "La zone du projet est inaccessible.");
            return;
        }

        Location location = new Location(
                world,
                submission.getX() + 0.5,
                submission.getY() + 1.0,
                submission.getZ() + 0.5
        );

        p.teleport(location);

        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);

        MoodStyle.send(
                p,
                MoodStyle.MODULE,
                MoodStyle.info("Inspection du projet ouverte."),
                MoodStyle.detail("Ville : §b" + submission.getTown()),
                MoodStyle.detail("Projet : §e" + submission.getBuildName()),
                MoodStyle.detail("Téléportation vers la zone déclarée")
        );
    }

    private static void approve(Player p, TownSubmission submission) {

        if (submission.getStatus() == SubmissionStatus.APPROVED) {
            deny(p, "Dossier déjà validé.", "La phase de notation est déjà ouverte.");
            return;
        }

        if (submission.getStatus() == SubmissionStatus.FINISHED) {
            deny(p, "Dossier clôturé.", "Les votes sont déjà terminés.");
            return;
        }

        submission.setStatus(SubmissionStatus.APPROVED);
        SubmissionStorage.save(submission);

        p.closeInventory();
        p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);

        broadcast(
                MoodStyle.success("Projet validé par la Commission."),
                submission,
                MoodStyle.info("Vote ouvert : utilisez §e/urbanisme§f puis §eVoter§f."),
                MoodStyle.detail("Votre vote aide le classement hebdomadaire")
        );
    }

    private static void openStaffRate(Player p, TownSubmission submission) {

        if (submission.getStatus() != SubmissionStatus.APPROVED) {
            deny(p, "Notation impossible.", "La demande doit d'abord être validée.");
            return;
        }

        TownGrade grade = GradeManager.get(submission.getTown());

        if (grade != null && grade.isLocked()) {
            denyLocked(p, submission);
            return;
        }

        p.playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1f, 1f);

        MoodStyle.send(
                p,
                MoodStyle.MODULE,
                MoodStyle.info("Notation staff ouverte."),
                MoodStyle.detail("Ville : §b" + submission.getTown()),
                MoodStyle.detail("Projet : §e" + submission.getBuildName()),
                MoodStyle.detail("Barème national chargé")
        );

        RateGUI.open(p, submission.getTown());
    }

    private static void closeVotes(Player p, TownSubmission submission) {

        if (submission.getStatus() != SubmissionStatus.APPROVED) {
            deny(p, "Clôture impossible.", "La demande doit d'abord être validée.");
            return;
        }

        TownGrade grade = GradeManager.get(submission.getTown());

        if (grade == null) {
            deny(p, "Dossier introuvable.", "La note nationale n'existe plus.");
            return;
        }

        if (grade.isLocked()) {
            denyLocked(p, submission);
            return;
        }

        double staffScore = NationalScoreCalculator.getStaffScore(submission.getTown());

        if (staffScore <= 0) {
            deny(p, "Clôture impossible.", "Aucune note staff enregistrée.");
            return;
        }

        double finalScore = NationalScoreCalculator.getFinalScore(submission.getTown());
        double mayorScore = NationalScoreCalculator.getMayorScore(submission.getTown());
        double citizenScore = NationalScoreCalculator.getCitizenScore(submission.getTown());

        grade.setFinished(true);
        grade.setLocked(true);
        grade.setFinalScore(finalScore);

        GradeManager.save(grade);

        submission.setStatus(SubmissionStatus.FINISHED);
        SubmissionStorage.save(submission);

        p.closeInventory();
        p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);

        broadcast(
                MoodStyle.success("Clôture des votes validée."),
                submission,
                MoodStyle.detail("Note finale : §e" + oneDecimal(finalScore) + "/50"),
                MoodStyle.detail("Staff §e" + oneDecimal(staffScore)
                        + " §8| §7Maires §e" + oneDecimal(mayorScore)
                        + " §8| §7Citoyens §e" + oneDecimal(citizenScore))
        );
    }

    private static void reject(Player p, TownSubmission submission) {

        if (submission.getStatus() == SubmissionStatus.FINISHED) {
            deny(p, "Dossier clôturé.", "Les votes sont déjà terminés.");
            return;
        }

        submission.setStatus(SubmissionStatus.REJECTED);
        SubmissionStorage.save(submission);

        p.closeInventory();
        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1f, 0.8f);

        broadcast(
                MoodStyle.error("Demande de projet refusée."),
                submission,
                MoodStyle.detail("Le dossier ne rejoint pas la notation publique"),
                MoodStyle.detail("La ville pourra déposer un nouveau dossier")
        );
    }

    private static void denyLocked(Player p, TownSubmission submission) {

        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);

        MoodStyle.send(
                p,
                MoodStyle.MODULE,
                MoodStyle.error("Votes déjà clôturés."),
                MoodStyle.detail("Ville : §b" + submission.getTown()),
                MoodStyle.detail("Projet : §e" + submission.getBuildName()),
                MoodStyle.detail("Ce dossier ne reçoit plus de notes")
        );
    }

    private static void deny(Player p, String message, String detail) {

        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);

        MoodStyle.send(
                p,
                MoodStyle.MODULE,
                MoodStyle.error(message),
                MoodStyle.detail(detail),
                MoodStyle.detail("Service officiel de §aMood§6Craft")
        );
    }

    private static void broadcast(
            String message,
            TownSubmission submission,
            String line1,
            String line2
    ) {

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(MoodStyle.header(MoodStyle.MODULE));
        Bukkit.broadcastMessage(message);
        Bukkit.broadcastMessage(MoodStyle.detail("Ville : §b" + submission.getTown()));
        Bukkit.broadcastMessage(MoodStyle.detail("Projet : §e" + submission.getBuildName()));
        Bukkit.broadcastMessage(line1);
        Bukkit.broadcastMessage(line2);
        Bukkit.broadcastMessage(MoodStyle.footer());
    }

    private static String oneDecimal(double value) {
        return String.format("%.1f", value);
    }
}
