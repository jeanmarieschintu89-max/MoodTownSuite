package fr.moodcraft.flag.manager;

import fr.moodcraft.flag.model.TownFlag;

import java.util.HashMap;
import java.util.Map;

public class FlagManager {

    //
    // 🏛 TOWN FLAGS
    //

    private static final Map<String, TownFlag> townFlags =
            new HashMap<>();

    //
    // 🌍 NATION FLAGS
    //

    private static final Map<String, TownFlag> nationFlags =
            new HashMap<>();

    //
    // 🏛 TOWN
    //

    public static void setTown(
            String town,
            TownFlag flag
    ) {

        townFlags.put(
                town.toLowerCase(),
                flag
        );
    }

    public static TownFlag getTown(
            String town
    ) {

        return townFlags.get(
                town.toLowerCase()
        );
    }

    public static void removeTown(
            String town
    ) {

        townFlags.remove(
                town.toLowerCase()
        );
    }

    public static Map<String, TownFlag> allTowns() {

        return townFlags;
    }

    //
    // 🌍 NATION
    //

    public static void setNation(
            String nation,
            TownFlag flag
    ) {

        nationFlags.put(
                nation.toLowerCase(),
                flag
        );
    }

    public static TownFlag getNation(
            String nation
    ) {

        return nationFlags.get(
                nation.toLowerCase()
        );
    }

    public static void removeNation(
            String nation
    ) {

        nationFlags.remove(
                nation.toLowerCase()
        );
    }

    public static Map<String, TownFlag> allNations() {

        return nationFlags;
    }
}