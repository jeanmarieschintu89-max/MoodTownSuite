package fr.moodcraft.tgrade.manager;

import fr.moodcraft.tgrade.model.CitizenVote;

import fr.moodcraft.tgrade.storage.VoteStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CitizenVoteManager {

    //
    // 📦 CACHE
    //

    private static final Map<String,
            Map<UUID, CitizenVote>>
            votes = new HashMap<>();

    //
    // 🔑 NORMALIZE
    //

    private static String key(
            String town
    ) {

        if (town == null) {
            return "";
        }

        return town.toLowerCase()
                .trim();
    }

    //
    // 📥 LOAD
    //

    public static void loadTown(
            String town
    ) {

        String key =
                key(town);

        Map<UUID, CitizenVote> map =
                new HashMap<>();

        List<CitizenVote> loaded =
                VoteStorage.getCitizenVotes(
                        town
                );

        for (CitizenVote vote : loaded) {

            if (vote == null) {
                continue;
            }

            map.put(
                    vote.getVoter(),
                    vote
            );
        }

        votes.put(
                key,
                map
        );
    }

    //
    // ✅ ENSURE LOAD
    //

    private static void ensureLoaded(
            String town
    ) {

        String key =
                key(town);

        if (!votes.containsKey(key)) {

            loadTown(town);
        }
    }

    //
    // 👥 GET VOTE
    //

    public static CitizenVote getVote(
            UUID player,
            String town
    ) {

        ensureLoaded(town);

        Map<UUID, CitizenVote> map =
                votes.get(
                        key(town)
                );

        if (map == null) {
            return null;
        }

        return map.get(player);
    }

    //
    // 💾 SAVE
    //

    public static void saveVote(
            CitizenVote vote
    ) {

        if (vote == null) {
            return;
        }

        String town =
                key(
                        vote.getTown()
                );

        ensureLoaded(
                vote.getTown()
        );

        votes.putIfAbsent(
                town,
                new HashMap<>()
        );

        votes.get(town)
                .put(
                        vote.getVoter(),
                        vote
                );

        VoteStorage.saveCitizenVote(
                vote
        );
    }

    //
    // 📚 GET ALL
    //

    public static List<CitizenVote> getVotes(
            String town
    ) {

        ensureLoaded(town);

        Map<UUID, CitizenVote> map =
                votes.get(
                        key(town)
                );

        if (map == null) {
            return List.of();
        }

        return List.copyOf(
                map.values()
        );
    }

    //
    // ❓ HAS VOTED
    //

    public static boolean hasVoted(
            UUID player,
            String town
    ) {

        return getVote(
                player,
                town
        ) != null;
    }

    //
    // 🗑 CLEAR
    //

    public static void clear() {

        votes.clear();
    }
}