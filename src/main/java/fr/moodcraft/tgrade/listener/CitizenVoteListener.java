package fr.moodcraft.tgrade.listener;

import fr.moodcraft.tgrade.gui.CitizenTownListGUI;
import fr.moodcraft.tgrade.gui.CitizenVoteGUI;
import fr.moodcraft.tgrade.gui.holder.TownVoteHolder;
import fr.moodcraft.tgrade.manager.CitizenVoteManager;
import fr.moodcraft.tgrade.model.CitizenVote;
import fr.moodcraft.tgrade.model.SubmissionStatus;
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

public class CitizenVoteListener implements Listener {

    @EventHandler
    public void click(InventoryClickEvent e) {

        if (!MoodStyle.titleEquals(e.getView().getTitle(), CitizenVoteGUI.TITLE)) {
            return;
        }

        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player p)) {
            return;
        }

        if (e.getClickedInventory() == null) {
            return;
        }

        if (e.getRawSlot() < 0 || e.getRawSlot() >= e.getView().getTopInventory().getSize()) {
            return;
        }

        String town = getTown(e);

        if (town == null || town.isBlank()) {
            return;
        }

        int slot = e.getRawSlot();
        MoodStyle.click(p);

        if (slot == CitizenVoteGUI.BACK) {
            CitizenTownListGUI.open(p);
            return;
        }

        TownSubmission project = getActiveProject(town);

        if (slot == CitizenVoteGUI.TP_PROJECT) {
            teleportToProject(p, town, project, "vote citoyen");
            return;
        }

        CitizenVote vote = CitizenVoteManager.getVote(p.getUniqueId(), town);

        if (vote == null) {
            vote = new CitizenVote(p.getUniqueId(), town);
        }

        if (slot == CitizenVoteGUI.BEAUTE) {
            vote.setBeaute(next(vote.getBeaute()));
            CitizenVoteManager.saveVote(vote);
            CitizenVoteGUI.open(p, town);
            return;
        }

        if (slot == CitizenVoteGUI.AMBIANCE) {
            vote.setAmbiance(next(vote.getAmbiance()));
            CitizenVoteManager.saveVote(vote);
            CitizenVoteGUI.open(p, town);
            return;
        }

        if (slot == CitizenVoteGUI.ACTIVITE) {
            vote.setActivite(next(vote.getActivite()));
            CitizenVoteManager.saveVote(vote);
            CitizenVoteGUI.open(p, town);
            return;
        }

        if (slot == CitizenVoteGUI.ORIGINALITE) {
            vote.setOriginalite(next(vote.getOriginalite()));
            CitizenVoteManager.saveVote(vote);
            CitizenVoteGUI.open(p, town);
            return;
        }

        if (slot == CitizenVoteGUI.POPULARITE) {
            vote.setPopularite(next(vote.getPopularite()));
            CitizenVoteManager.saveVote(vote);
            CitizenVoteGUI.open(p, town);
            return;
        }

        if (slot == CitizenVoteGUI.SAVE) {
            vote.updateTimestamp();
            CitizenVoteManager.saveVote(vote);
            MoodStyle.ok(p);
            MoodStyle.send(
                    p,
                    MoodStyle.MODULE,
                    "§a✔ §fVote citoyen enregistré.",
                    "",
                    "§7Ville: §b" + town,
                    "§7Note: §e" + vote.getTotal() + "/" + CitizenVoteGUI.MAX_TOTAL,
                    "§7Score compté: §e" + String.format("%.1f", (vote.getTotal() / 15.0) * 50.0) + "/50",
                    "§7État: §aValidé",
                    "",
                    "§8• §7Le classement hebdo est actualisé"
            );
            p.closeInventory();
        }
    }

    private void teleportToProject(
            Player p,
            String town,
            TownSubmission project,
            String reason
    ) {

        if (project == null) {
            MoodStyle.deny(
                    p,
                    "Projet introuvable.",
                    "Aucun dossier actif pour " + town + "."
            );
            return;
        }

        World world = Bukkit.getWorld(project.getWorld());

        if (world == null) {
            MoodStyle.deny(
                    p,
                    "Monde introuvable.",
                    "La position du projet ne peut pas être ouverte."
            );
            return;
        }

        Location location = new Location(
                world,
                project.getX() + 0.5,
                project.getY() + 1,
                project.getZ() + 0.5
        );

        p.teleport(location);
        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);

        MoodStyle.send(
                p,
                MoodStyle.MODULE,
                "§fTéléportation au projet.",
                "",
                "§7Ville: §b" + town,
                "§7Projet: §e" + project.getBuildName(),
                "§7Motif: §e" + reason,
                "",
                "§8• §7Inspectez avant de voter"
        );
    }

    private String getTown(InventoryClickEvent e) {

        if (e.getView().getTopInventory().getHolder() instanceof TownVoteHolder holder) {
            return holder.getTown();
        }

        return null;
    }

    private TownSubmission getActiveProject(String town) {

        for (TownSubmission submission : SubmissionStorage.getAll()) {
            if (submission.getTown().equalsIgnoreCase(town)
                    && submission.getStatus() == SubmissionStatus.APPROVED) {
                return submission;
            }
        }

        return null;
    }

    private int next(int current) {
        return current >= CitizenVoteGUI.MAX_NOTE ? 1 : current + 1;
    }
}
