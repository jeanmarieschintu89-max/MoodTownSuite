package fr.moodcraft.tgrade.listener;

import fr.moodcraft.tgrade.manager.ProjectDepositSessionManager;
import fr.moodcraft.tgrade.manager.ProjectDepositSessionManager.Session;
import fr.moodcraft.tgrade.manager.ProjectDepositSessionManager.Step;

import fr.moodcraft.tgrade.model.SubmissionStatus;
import fr.moodcraft.tgrade.model.TownSubmission;

import fr.moodcraft.tgrade.storage.SubmissionStorage;
import fr.moodcraft.tgrade.util.MoodStyle;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class ProjectDepositChatListener
        implements Listener {

    private static final int MAX_NAME_LENGTH = 32;
    private static final int MAX_DESCRIPTION_LENGTH = 120;

    @EventHandler
    public void onChat(
            AsyncPlayerChatEvent e
    ) {

        Player p =
                e.getPlayer();

        if (!ProjectDepositSessionManager.has(p)) {
            return;
        }

        e.setCancelled(true);

        String message =
                e.getMessage().trim();

        Bukkit.getScheduler().runTask(
                Bukkit.getPluginManager()
                        .getPlugin("MoodTownGrade"),
                () -> handle(p, message)
        );
    }

    private void handle(
            Player p,
            String message
    ) {

        Session session =
                ProjectDepositSessionManager.get(p);

        if (session == null) {
            return;
        }

        if (message.equalsIgnoreCase("annuler")
                || message.equalsIgnoreCase("cancel")) {

            ProjectDepositSessionManager.remove(p);

            MoodStyle.send(
                    p,
                    MoodStyle.MODULE,
                    MoodStyle.error("Dépôt annulé."),
                    MoodStyle.detail("Aucun dossier n'a été transmis")
            );

            return;
        }

        if (session.getStep() == Step.NAME) {

            if (message.length() < 3) {

                MoodStyle.send(
                        p,
                        MoodStyle.MODULE,
                        MoodStyle.error("Nom trop court."),
                        MoodStyle.detail("Minimum : §e3 caractères"),
                        MoodStyle.detail("Exemple : §eGare Centrale")
                );

                return;
            }

            if (message.length() > MAX_NAME_LENGTH) {

                MoodStyle.send(
                        p,
                        MoodStyle.MODULE,
                        MoodStyle.error("Nom trop long."),
                        MoodStyle.detail("Maximum : §e" + MAX_NAME_LENGTH + " caractères")
                );

                return;
            }

            session.setProjectName(message);
            session.setStep(Step.DESCRIPTION);

            MoodStyle.send(
                    p,
                    MoodStyle.MODULE,
                    MoodStyle.info("Nom enregistré."),
                    MoodStyle.detail("Projet : §e" + message),
                    MoodStyle.detail("Écrivez maintenant la description"),
                    MoodStyle.detail("Maximum : §e" + MAX_DESCRIPTION_LENGTH + " caractères"),
                    MoodStyle.detail("Tapez §cannuler §7pour quitter")
            );

            return;
        }

        if (session.getStep() == Step.DESCRIPTION) {

            if (message.length() < 10) {

                MoodStyle.send(
                        p,
                        MoodStyle.MODULE,
                        MoodStyle.error("Description trop courte."),
                        MoodStyle.detail("Minimum : §e10 caractères"),
                        MoodStyle.detail("Explique le style, le lieu ou l'objectif")
                );

                return;
            }

            if (message.length() > MAX_DESCRIPTION_LENGTH) {

                MoodStyle.send(
                        p,
                        MoodStyle.MODULE,
                        MoodStyle.error("Description trop longue."),
                        MoodStyle.detail("Maximum : §e" + MAX_DESCRIPTION_LENGTH + " caractères"),
                        MoodStyle.detail("Actuel : §c" + message.length() + " caractères")
                );

                return;
            }

            Location loc =
                    p.getLocation();

            String id =
                    UUID.randomUUID()
                            .toString()
                            .substring(0, 4)
                            .toUpperCase();

            TownSubmission sub =
                    new TownSubmission(
                            id,
                            session.getTown(),
                            session.getProjectName(),
                            message,
                            p.getWorld().getName(),
                            loc.getBlockX(),
                            loc.getBlockY(),
                            loc.getBlockZ(),
                            p.getUniqueId(),
                            System.currentTimeMillis(),
                            SubmissionStatus.PENDING
                    );

            SubmissionStorage.save(sub);
            ProjectDepositSessionManager.remove(p);

            MoodStyle.send(
                    p,
                    MoodStyle.MODULE,
                    MoodStyle.success("Projet envoyé à la Commission Urbaine."),
                    MoodStyle.detail("Ville : §b" + sub.getTown()),
                    MoodStyle.detail("Projet : §e" + sub.getBuildName()),
                    MoodStyle.detail("État : §eInspection staff"),
                    MoodStyle.detail("Si le dossier est validé, les joueurs pourront voter")
            );

            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(MoodStyle.header(MoodStyle.MODULE));
            Bukkit.broadcastMessage(MoodStyle.info("Nouveau projet déposé."));
            Bukkit.broadcastMessage(MoodStyle.detail("Ville : §b" + sub.getTown()));
            Bukkit.broadcastMessage(MoodStyle.detail("Projet : §e" + sub.getBuildName()));
            Bukkit.broadcastMessage(MoodStyle.detail("En attente de validation staff"));
            Bukkit.broadcastMessage(MoodStyle.detail("Les votes ouvriront après validation"));
            Bukkit.broadcastMessage(MoodStyle.footer());
        }
    }
}