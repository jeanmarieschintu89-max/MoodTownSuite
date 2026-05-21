package fr.moodcraft.tgrade.listener;

import fr.moodcraft.tgrade.gui.MayorTownListGUI;
import fr.moodcraft.tgrade.gui.MayorVoteGUI;
import fr.moodcraft.tgrade.gui.holder.TownVoteHolder;
import fr.moodcraft.tgrade.manager.MayorVoteManager;
import fr.moodcraft.tgrade.model.MayorVote;
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

public class MayorVoteListener implements Listener {

    @EventHandler
    public void click(InventoryClickEvent e) {

        if (!MoodStyle.titleEquals(e.getView().getTitle(), MayorVoteGUI.TITLE)) {
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

        if (slot == MayorVoteGUI.BACK) {
            MayorTownListGUI.open(p);
            return;
        }

        TownSubmission project = getActiveProject(town);

        if (slot == MayorVoteGUI.TP_PROJECT) {
            teleportToProject(p, town, project, "vote des maires");
            return;
        }

        MayorVote vote = MayorVoteManager.getVote(p.getUniqueId(), town);

        if (vote == null) {
            vote = new MayorVote(p.getUniqueId(), town);
        }

        if (slot == MayorVoteGUI.BEAUTE) {
            vote.setBeaute(next(vote.getBeaute()));
            MayorVoteManager.saveVote(vote);
            MayorVoteGUI.open(p, town);
            return;
        }

        if (slot == MayorVoteGUI.AMBIANCE) {
            vote.setAmbiance(next(vote.getAmbiance()));
            MayorVoteManager.saveVote(vote);
            MayorVoteGUI.open(p, town);
            return;
        }

        if (slot == MayorVoteGUI.ACTIVITE) {
            vote.setActivite(next(vote.getActivite()));
            MayorVoteManager.saveVote(vote);
            MayorVoteGUI.open(p, town);
            return;
        }

        if (slot == MayorVoteGUI.ORIGINALITE) {
            vote.setOriginalite(next(vote.getOriginalite()));
            MayorVoteManager.saveVote(vote);
            MayorVoteGUI.open(p, town);
            return;
        }

        if (slot == MayorVoteGUI.POPULARITE) {
            vote.setPopularite(next(vote.getPopularite()));
            MayorVoteManager.saveVote(vote);
            MayorVoteGUI.open(p, town);
            return;
        }

        if (slot == MayorVoteGUI.SAVE) {
            vote.updateTimestamp();
            MayorVoteManager.saveVote(vote);
            MoodStyle.ok(p);
            MoodStyle.send(
                    p,
                    MoodStyle.MODULE,
                    "§a✔ §fVote municipal enregistré.",
                    "",
                    "§7Ville: §b" + town,
                    "§7Note: §e" + vote.getTotal() + "/" + MayorVoteGUI.MAX_TOTAL,
                    "§7Score compté: §e" + (vote.getTotal() * 2) + "/50",
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
        return current >= MayorVoteGUI.MAX_NOTE ? 1 : current + 1;
    }
}
