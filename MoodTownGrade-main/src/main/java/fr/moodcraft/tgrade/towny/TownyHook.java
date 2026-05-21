package fr.moodcraft.tgrade.towny;

import com.palmergames.bukkit.towny.TownyAPI;

import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

import org.bukkit.entity.Player;

public class TownyHook {

    //
    // 🏙 GET TOWN
    //

    public static Town getTown(Player p) {

        return TownyAPI.getInstance()
                .getTown(p);
    }

    //
    // 👤 GET RESIDENT
    //

    public static Resident getResident(
            Player p
    ) {

        return TownyAPI.getInstance()
                .getResident(p);
    }

    //
    // 👑 IS MAYOR
    //

    public static boolean isMayor(
            Player p
    ) {

        Town town =
                getTown(p);

        Resident resident =
                getResident(p);

        if (town == null
                || resident == null) {

            return false;
        }

        return town.getMayor()
                .equals(resident);
    }

    //
    // 🛡 IS ASSISTANT
    //

    public static boolean isAssistant(
            Player p
    ) {

        Resident resident =
                getResident(p);

        if (resident == null) {

            return false;
        }

        //
        // 🏛 TOWNY RANK
        //

        return resident.hasTownRank(
                "assistant"
        );
    }

    //
    // 🏛 CAN MANAGE
    //

    public static boolean canManage(
            Player p
    ) {

        return isMayor(p)
                || isAssistant(p);
    }
}