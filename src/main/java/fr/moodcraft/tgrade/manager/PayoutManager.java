package fr.moodcraft.tgrade.manager;

import fr.moodcraft.tgrade.model.TownGrade;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

import org.bukkit.entity.Player;

public class PayoutManager {

    //
    // 📊 REPORT
    //

    public record PayoutReport(
            int paid,
            double total
    ) {}

    //
    // 💰 PAYOUT
    //

    public static PayoutReport payoutAll() {

        int paid = 0;

        double total = 0;

        for (TownGrade grade :
                GradeManager.getAll()) {

            if (!grade.isFinished()) {
                continue;
            }

            if (grade.isPayoutClaimed()) {
                continue;
            }

            double national =
                    grade.isLocked()
                            ? grade.getFinalScore()
                            : NationalScoreCalculator
                            .getFinalScore(
                                    grade.getTown()
                            );

            double staff =
                    NationalScoreCalculator
                            .getStaffScore(
                                    grade.getTown()
                            );

            double mayors =
                    NationalScoreCalculator
                            .getMayorScore(
                                    grade.getTown()
                            );

            double citizens =
                    NationalScoreCalculator
                            .getCitizenScore(
                                    grade.getTown()
                            );

            Town town =
                    TownyAPI.getInstance()
                            .getTown(
                                    grade.getTown()
                            );

            if (town == null) {
                continue;
            }

            int payout =
                    grade.getPayout();

            if (payout <= 0) {
                continue;
            }

            try {

                town.getAccount()
                        .deposit(

                                payout,

                                "Fonds Nationaux MoodCraft"
                        );

                grade.setPayoutClaimed(true);

                GradeManager.save(grade);

            } catch (Exception e) {

                Bukkit.getConsoleSender()
                        .sendMessage(
                                "§c[MoodTownGrade] Paiement impossible pour "
                                        + town.getName()
                        );

                e.printStackTrace();

                continue;
            }

            paid++;

            total += payout;

            //
            // 📢 GLOBAL
            //

            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(
                    "§8----- §6Commission Urbaine §8-----"
            );
            Bukkit.broadcastMessage(
                    "§fFinancement national attribué."
            );
            Bukkit.broadcastMessage(
                    "§7Ville: §b" + town.getName()
            );
            Bukkit.broadcastMessage(
                    "§7Subvention: §a+"
                            + format(payout)
                            + "€"
            );
            Bukkit.broadcastMessage(
                    "§7Note nationale: §e"
                            + national
                            + "§7/50"
            );
            Bukkit.broadcastMessage(
                    "§7Influences: §6Staff §e" + staff
                            + " §8| §6Maires §e" + mayors
                            + " §8| §6Citoyens §e" + citizens
            );
            Bukkit.broadcastMessage(
                    "§7Classe urbaine: "
                            + grade.getRank()
            );
            Bukkit.broadcastMessage(
                    grade.getAppreciation()
            );

            if (national >= 45) {

                Bukkit.broadcastMessage(
                        "§6👑 Distinction nationale accordée."
                );
            }

            Bukkit.broadcastMessage(
                    "§a✔ Fonds inscrits au registre national."
            );
            Bukkit.broadcastMessage("");

            //
            // 👑 MESSAGE MAIRE
            //

            Player mayor =
                    town.getMayor()
                            .getPlayer();

            if (mayor != null
                    && mayor.isOnline()) {

                mayor.sendMessage("");
                mayor.sendMessage(
                        "§8----- §6Commission Urbaine §8-----"
                );
                mayor.sendMessage(
                        "§fVotre ville a obtenu un financement national."
                );
                mayor.sendMessage(
                        "§7Ville: §b" + town.getName()
                );
                mayor.sendMessage(
                        "§7Subvention: §a+"
                                + format(payout)
                                + "€"
                );
                mayor.sendMessage(
                        "§7Note nationale: §e"
                                + national
                                + "§7/50"
                );
                mayor.sendMessage(
                        "§7Classe urbaine: "
                                + grade.getRank()
                );
                mayor.sendMessage(
                        "§a✔ Fonds transférés vers la banque municipale."
                );
                mayor.sendMessage("");

                mayor.playSound(

                        mayor.getLocation(),

                        Sound.UI_TOAST_CHALLENGE_COMPLETE,

                        1f,

                        1f
                );
            }

            //
            // 👥 MESSAGE HABITANTS
            //

            for (Player online :
                    Bukkit.getOnlinePlayers()) {

                try {

                    if (TownyAPI.getInstance()
                            .getResident(
                                    online.getName()
                            ) == null) {

                        continue;
                    }

                    Town playerTown =
                            TownyAPI.getInstance()
                                    .getResident(
                                            online.getName()
                                    )
                                    .getTownOrNull();

                    if (playerTown == null) {
                        continue;
                    }

                    if (!playerTown.getName()
                            .equalsIgnoreCase(
                                    town.getName()
                            )) {

                        continue;
                    }

                    if (mayor != null
                            && online.getUniqueId()
                            .equals(
                                    mayor.getUniqueId()
                            )) {

                        continue;
                    }

                    online.sendMessage("");
                    online.sendMessage(
                            "§8----- §6Commission Urbaine §8-----"
                    );
                    online.sendMessage(
                            "§fVotre ville a reçu un financement national."
                    );
                    online.sendMessage(
                            "§7Ville: §b" + town.getName()
                    );
                    online.sendMessage(
                            "§7Subvention municipale: §a+"
                                    + format(payout)
                                    + "€"
                    );
                    online.sendMessage(
                            "§7Note nationale: §e"
                                    + national
                                    + "§7/50"
                    );
                    online.sendMessage(
                            "§a✔ Développement urbain approuvé."
                    );
                    online.sendMessage("");

                    online.playSound(

                            online.getLocation(),

                            Sound.BLOCK_BEACON_ACTIVATE,

                            0.8f,

                            1.2f
                    );

                } catch (Exception ignored) {}
            }

            //
            // 🔊 SON GLOBAL
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

        return new PayoutReport(
                paid,
                total
        );
    }

    //
    // 💰 FORMAT
    //

    private static String format(
            double value
    ) {

        return String.format(
                "%,.0f",
                value
        ).replace(",", " ");
    }
}