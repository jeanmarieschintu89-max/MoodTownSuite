package fr.moodcraft.tgrade.manager;

import fr.moodcraft.tgrade.model.TownGrade;

import fr.moodcraft.tgrade.storage.GradeStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GradeManager {

    //
    // 📊 CACHE GLOBAL
    //

    private static final Map<String, TownGrade>
            grades = new HashMap<>();

    //
    // 📌 GET
    //

    public static TownGrade get(String town) {

        if (town == null) {
            return null;
        }

        String key =
                town.toLowerCase();

        //
        // 📂 CACHE
        //

        if (grades.containsKey(key)) {

            return grades.get(key);
        }

        //
        // 💾 LOAD
        //

        TownGrade grade =
                GradeStorage.load(town);

        //
        // 📥 CACHE SAVE
        //

        grades.put(
                key,
                grade
        );

        return grade;
    }

    //
    // 💾 SAVE
    //

    public static void save(TownGrade grade) {

        if (grade == null
                || grade.getTown() == null) {

            return;
        }

        //
        // 📥 CACHE
        //

        grades.put(
                grade.getTown()
                        .toLowerCase(),
                grade
        );

        //
        // 💾 STORAGE
        //

        GradeStorage.save(grade);
    }

    //
    // 📚 GET ALL
    //

    public static Collection<TownGrade>
    getAll() {

        return grades.values();
    }

    //
    // 🔄 LOAD ALL
    //

    public static void loadAll() {

        grades.clear();

        for (String town :
                GradeStorage.getAllTowns()) {

            if (town == null) {
                continue;
            }

            TownGrade grade =
                    GradeStorage.load(town);

            grades.put(
                    town.toLowerCase(),
                    grade
            );
        }
    }

    //
    // 🧹 RESET WEEK
    //

    public static void resetWeek() {

        for (TownGrade grade :
                grades.values()) {

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

            save(grade);
        }
    }

    //
    // 🗑 CLEAR CACHE
    //

    public static void clearCache() {

        grades.clear();
    }

    //
    // 🏆 TOP CITY
    //

    public static TownGrade getBestTown() {

        TownGrade best = null;

        for (TownGrade grade :
                grades.values()) {

            if (!grade.isFinished()) {
                continue;
            }

            if (best == null) {

                best = grade;

                continue;
            }

            if (NationalScoreCalculator
                    .getFinalScore(grade.getTown())
                    > NationalScoreCalculator
                    .getFinalScore(best.getTown())) {

                best = grade;
            }
        }

        return best;
    }
}