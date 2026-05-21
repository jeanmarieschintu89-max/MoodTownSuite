package fr.moodcraft.tgrade.manager;

import fr.moodcraft.tgrade.model.TownGrade;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RankingManager {

    //
    // 🏆 TOP NATIONAL
    //

    public static List<TownGrade> getTop() {

        List<TownGrade> list =
                new ArrayList<>();

        for (TownGrade grade :
                GradeManager.getAll()) {

            if (!grade.isFinished()) {
                continue;
            }

            list.add(grade);
        }

        list.sort(

                Comparator.comparingDouble(

                        (TownGrade grade) ->
                                NationalScoreCalculator
                                        .getFinalScore(
                                                grade.getTown()
                                        )
                ).reversed()
        );

        return list;
    }

    //
    // 🥇 POSITION
    //

    public static int getPosition(
            String town
    ) {

        if (town == null) {
            return -1;
        }

        List<TownGrade> top =
                getTop();

        for (int i = 0;
             i < top.size();
             i++) {

            if (top.get(i)
                    .getTown()
                    .equalsIgnoreCase(town)) {

                return i + 1;
            }
        }

        return -1;
    }

    //
    // 👑 CAPITALE NATIONALE
    //

    public static TownGrade getBest() {

        List<TownGrade> top =
                getTop();

        if (top.isEmpty()) {
            return null;
        }

        return top.get(0);
    }

    //
    // 📊 SCORE MOYEN
    //

    public static double getAverageScore() {

        List<TownGrade> top =
                getTop();

        if (top.isEmpty()) {
            return 0;
        }

        double total = 0;

        for (TownGrade grade : top) {

            total +=
                    NationalScoreCalculator
                            .getFinalScore(
                                    grade.getTown()
                            );
        }

        return round(
                total / top.size()
        );
    }

    //
    // 🏙 VILLES CLASSÉES
    //

    public static int getFinishedTowns() {

        return getTop().size();
    }

    //
    // 🏛 PRESTIGE NATIONAL TOTAL
    //

    public static int getTotalPrestige() {

        int total = 0;

        for (TownGrade grade :
                getTop()) {

            total +=
                    NationalScoreCalculator
                            .getFinalScore(
                                    grade.getTown()
                            );
        }

        return total;
    }

    //
    // 👑 TITRE RP
    //

    public static String getNationalTitle(
            double score
    ) {

        if (score >= 47) {
            return "§6👑 Capitale Impériale";
        }

        if (score >= 42) {
            return "§e✦ Métropole Nationale";
        }

        if (score >= 36) {
            return "§a✦ Grande Ville Prestigieuse";
        }

        if (score >= 30) {
            return "§b✦ Ville Reconnue";
        }

        if (score >= 22) {
            return "§2✦ Ville Émergente";
        }

        if (score >= 15) {
            return "§e✦ Commune en Développement";
        }

        return "§7✦ Zone Rurale";
    }

    //
    // 🏆 TITRE D'UNE VILLE
    //

    public static String getTownTitle(
            String town
    ) {

        return getNationalTitle(

                NationalScoreCalculator
                        .getFinalScore(
                                town
                        )
        );
    }

    //
    // 🔢 ROUND
    //

    private static double round(
            double value
    ) {

        return Math.round(
                value * 10.0
        ) / 10.0;
    }
}