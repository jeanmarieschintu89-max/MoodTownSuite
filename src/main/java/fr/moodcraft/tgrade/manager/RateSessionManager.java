package fr.moodcraft.tgrade.manager;

import fr.moodcraft.tgrade.model.RateSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RateSessionManager {

    private static final Map<UUID, RateSession> sessions =
            new HashMap<>();

    //
    // 🏛️ CRÉATION / RÉCUPÉRATION
    //

    public static RateSession create(
            UUID uuid,
            String town
    ) {

        RateSession existing =
                sessions.get(uuid);

        /*
         * Correction importante :
         *
         * Le menu de notation se rouvre à chaque clic sur un critère.
         * On garde donc la session seulement si l'admin note toujours
         * la même ville.
         *
         * Ancien bug : la session était réutilisée même quand l'admin
         * changeait de ville. La note partait alors sur l'ancienne ville.
         */

        if (existing != null
                && existing.getTown() != null
                && existing.getTown().equalsIgnoreCase(town)) {

            return existing;
        }

        RateSession session =
                new RateSession(town);

        sessions.put(
                uuid,
                session
        );

        return session;
    }

    //
    // 🔎 GET
    //

    public static RateSession get(
            UUID uuid
    ) {

        return sessions.get(uuid);
    }

    //
    // 🧹 REMOVE
    //

    public static void remove(
            UUID uuid
    ) {

        sessions.remove(uuid);
    }

    //
    // ✅ HAS
    //

    public static boolean has(
            UUID uuid
    ) {

        return sessions.containsKey(uuid);
    }
}
