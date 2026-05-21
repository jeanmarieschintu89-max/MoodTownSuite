package fr.moodcraft.tgrade.manager;

import fr.moodcraft.tgrade.model.MayorVote;

import fr.moodcraft.tgrade.storage.VoteStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MayorVoteManager {

    //
    // 📦 CACHE
    //

    private static final Map<String,
            Map<UUID, MayorVote>>
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

        Map<UUID, MayorVote> map =
                new HashMap<>();

        List<MayorVote> loaded =
                VoteStorage.getMayorVotes(
                        town
                );

        for (MayorVote vote : loaded) {

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
    // 👑 GET VOTE
    //

    public static MayorVote getVote(
            UUID player,
            String town
    ) {

        ensureLoaded(town);

        Map<UUID, MayorVote> map =
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
            MayorVote vote
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

        VoteStorage.saveMayorVote(
                vote
        );
    }

    //
    // 📚 GET ALL
    //

    public static List<MayorVote> getVotes(
            String town
    ) {

        ensureLoaded(town);

        Map<UUID, MayorVote> map =
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