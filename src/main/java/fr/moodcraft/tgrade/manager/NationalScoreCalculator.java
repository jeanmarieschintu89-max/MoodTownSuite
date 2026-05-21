package fr.moodcraft.tgrade.manager;

import fr.moodcraft.tgrade.model.CitizenVote;
import fr.moodcraft.tgrade.model.MayorVote;
import fr.moodcraft.tgrade.model.StaffVote;

import fr.moodcraft.tgrade.storage.VoteStorage;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NationalScoreCalculator {

    //
    // 🏛️ COEFFICIENTS
    //

    private static final double
            STAFF_WEIGHT = 0.70;

    private static final double
            MAYOR_WEIGHT = 0.20;

    private static final double
            CITIZEN_WEIGHT = 0.10;

    //
    // ⭐ SCORE FINAL
    //

    public static double getFinalScore(
            String town
    ) {

        if (town == null) {
            return 0;
        }

        double staff =
                getStaffScore(town);

        double mayor =
                getMayorScore(town);

        double citizen =
                getCitizenScore(town);

        return round(
                (staff * STAFF_WEIGHT)
                        + (mayor * MAYOR_WEIGHT)
                        + (citizen * CITIZEN_WEIGHT)
        );
    }

    //
    // 🏛️ STAFF SCORE /50
    //

    public static double getStaffScore(
            String town
    ) {

        if (town == null) {
            return 0;
        }

        List<StaffVote> rawVotes =
                VoteStorage
                        .getStaffVotes(town);

        Map<UUID, StaffVote> votes =
                new LinkedHashMap<>();

        for (StaffVote vote : rawVotes) {

            if (vote == null
                    || vote.getVoter() == null) {
                continue;
            }

            votes.put(
                    vote.getVoter(),
                    vote
            );
        }

        if (votes.isEmpty()) {
            return 0;
        }

        double total =
                0;

        for (StaffVote vote :
                votes.values()) {

            total +=
                    vote.getTotal();
        }

        return round(
                total / votes.size()
        );
    }

    //
    // 👑 MAYOR SCORE /50
    //

    public static double getMayorScore(
            String town
    ) {

        if (town == null) {
            return 0;
        }

        List<MayorVote> rawVotes =
                VoteStorage
                        .getMayorVotes(town);

        Map<UUID, MayorVote> votes =
                new LinkedHashMap<>();

        for (MayorVote vote : rawVotes) {

            if (vote == null
                    || vote.getVoter() == null) {
                continue;
            }

            votes.put(
                    vote.getVoter(),
                    vote
            );
        }

        if (votes.isEmpty()) {
            return 0;
        }

        double total =
                0;

        for (MayorVote vote :
                votes.values()) {

            //
            // 📊 /25 -> /50
            //

            total +=
                    vote.getTotal() * 2.0;
        }

        return round(
                total / votes.size()
        );
    }

    //
    // 👥 CITIZEN SCORE /50
    //

    public static double getCitizenScore(
            String town
    ) {

        if (town == null) {
            return 0;
        }

        List<CitizenVote> rawVotes =
                VoteStorage
                        .getCitizenVotes(town);

        Map<UUID, CitizenVote> votes =
                new LinkedHashMap<>();

        for (CitizenVote vote : rawVotes) {

            if (vote == null
                    || vote.getVoter() == null) {
                continue;
            }

            votes.put(
                    vote.getVoter(),
                    vote
            );
        }

        if (votes.isEmpty()) {
            return 0;
        }

        double total =
                0;

        for (CitizenVote vote :
                votes.values()) {

            //
            // 📊 /15 -> /50
            //

            total +=
                    (vote.getTotal() / 15.0) * 50.0;
        }

        return round(
                total / votes.size()
        );
    }

    //
    // 👥 NB CITOYENS
    //

    public static int getCitizenCount(
            String town
    ) {

        if (town == null) {
            return 0;
        }

        Map<UUID, CitizenVote> votes =
                new LinkedHashMap<>();

        for (CitizenVote vote :
                VoteStorage.getCitizenVotes(town)) {

            if (vote == null
                    || vote.getVoter() == null) {
                continue;
            }

            votes.put(
                    vote.getVoter(),
                    vote
            );
        }

        return votes.size();
    }

    //
    // 👑 NB MAIRES
    //

    public static int getMayorCount(
            String town
    ) {

        if (town == null) {
            return 0;
        }

        Map<UUID, MayorVote> votes =
                new LinkedHashMap<>();

        for (MayorVote vote :
                VoteStorage.getMayorVotes(town)) {

            if (vote == null
                    || vote.getVoter() == null) {
                continue;
            }

            votes.put(
                    vote.getVoter(),
                    vote
            );
        }

        return votes.size();
    }

    //
    // 🏛️ NB STAFF
    //

    public static int getStaffCount(
            String town
    ) {

        if (town == null) {
            return 0;
        }

        Map<UUID, StaffVote> votes =
                new LinkedHashMap<>();

        for (StaffVote vote :
                VoteStorage.getStaffVotes(town)) {

            if (vote == null
                    || vote.getVoter() == null) {
                continue;
            }

            votes.put(
                    vote.getVoter(),
                    vote
            );
        }

        return votes.size();
    }

    //
    // 🔢 PUBLIC ROUND
    //

    public static double roundScore(
            double value
    ) {

        return round(value);
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