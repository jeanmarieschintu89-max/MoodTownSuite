package fr.moodcraft.tgrade.task;

import fr.moodcraft.tgrade.manager.GradeManager;
import fr.moodcraft.tgrade.manager.NationalScoreCalculator;
import fr.moodcraft.tgrade.manager.PayoutManager;
import fr.moodcraft.tgrade.manager.RankingManager;

import fr.moodcraft.tgrade.model.TownGrade;

import fr.moodcraft.tgrade.storage.SubmissionStorage;
import fr.moodcraft.tgrade.storage.VoteStorage;

import org.bukkit.Bukkit;

import org.bukkit.Sound;

import org.bukkit.entity.Player;

public class WeeklyResetTask
        implements Runnable {

    @Override
    public void run() {

        //
        // 👑 VILLE DOMINANTE
        //

        TownGrade best =
                RankingManager.getBest();

        //
        // 💰 DISTRIBUTION
        //

        PayoutManager.payoutAll();

        //
        // 📢 FIN DE SAISON
        //

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(
                "§8----- §6Commission Urbaine §8-----"
        );
        Bukkit.broadcastMessage(
                "§fFin de la saison urbaine nationale."
        );

        if (best != null) {

            double national =
                    best.isLocked()
                            ? best.getFinalScore()
                            : NationalScoreCalculator
                            .getFinalScore(
                                    best.getTown()
                            );

            Bukkit.broadcastMessage(
                    "§7Capitale dominante: §e"
                            + best.getTown()
            );

            Bukkit.broadcastMessage(
                    "§7Note nationale: §e"
                            + national
                            + "§7/50"
            );

            Bukkit.broadcastMessage(
                    "§7Classe urbaine: "
                            + best.getRank()
            );

            Bukkit.broadcastMessage(
                    best.getAppreciation()
            );
        }

        Bukkit.broadcastMessage(
                "§a✔ Financements nationaux distribués."
        );

        Bukkit.broadcastMessage(
                "§7Archivage des registres urbains en cours..."
        );

        Bukkit.broadcastMessage("");

        //
        // 🔄 RESET NOTES
        //

        for (TownGrade grade :
                GradeManager.getAll()) {

            grade.setArchitecture(0);
            grade.setStyle(0);
            grade.setActivite(0);
            grade.setBanque(0);
            grade.setRemarquable(0);
            grade.setRp(0);
            grade.setTaille(0);
            grade.setVotes(0);

            grade.setFinished(false);
            grade.setPayoutClaimed(false);

            grade.setLocked(false);
            grade.setFinalScore(0);

            GradeManager.save(grade);
        }

        //
        // 🗂 CLEANUP DOSSIERS
        //

        SubmissionStorage.cleanup();

        //
        // 🗳 RESET VOTES
        //

        VoteStorage.clearAll();

        //
        // 📢 NOUVELLE SAISON
        //

        Bukkit.broadcastMessage(
                "§8----- §6Commission Urbaine §8-----"
        );

        Bukkit.broadcastMessage(
                "§fNouvelle semaine urbaine ouverte."
        );

        Bukkit.broadcastMessage(
                "§7Les municipalités peuvent déposer"
        );

        Bukkit.broadcastMessage(
                "§7de nouveaux projets et inspections."
        );

        Bukkit.broadcastMessage(
                "§e▶ Votes citoyens et gouvernementaux réactivés."
        );

        Bukkit.broadcastMessage("");

        //
        // 🔊 SOUND
        //

        for (Player online :
                Bukkit.getOnlinePlayers()) {

            online.playSound(

                    online.getLocation(),

                    Sound.UI_TOAST_CHALLENGE_COMPLETE,

                    0.8f,

                    1f
            );
        }
    }
}