package fr.moodcraft.townmenu.util;

import com.palmergames.bukkit.towny.TownyAPI;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

import org.bukkit.entity.Player;

public class TownyUtil {

    public static Resident getResident(
            Player p
    ) {

        return TownyAPI.getInstance()
                .getResident(
                        p.getUniqueId()
                );
    }

    public static Town getTown(
            Player p
    ) {

        Resident resident =
                getResident(p);

        if (resident == null
                || !resident.hasTown())
            return null;

        return resident.getTownOrNull();
    }

    public static Nation getNation(
            Player p
    ) {

        Resident resident =
                getResident(p);

        if (resident == null
                || !resident.hasNation())
            return null;

        return resident.getNationOrNull();
    }

    public static String townName(
            Player p
    ) {

        Town town =
                getTown(p);

        return town == null
                ? "Aucune ville"
                : town.getName();
    }

    public static String nationName(
            Player p
    ) {

        Nation nation =
                getNation(p);

        return nation == null
                ? "Aucune nation"
                : nation.getName();
    }

    public static boolean hasTown(
            Player p
    ) {

        return getTown(p) != null;
    }

    public static boolean canManageTown(
            Player p
    ) {

        Resident resident =
                getResident(p);

        if (resident == null)
            return false;

        return resident.isMayor()
                || resident.hasTownRank("assistant")
                || p.hasPermission(
                        "moodtownmenu.admin"
                );
    }

    public static boolean isMayor(
            Player p
    ) {

        Resident resident =
                getResident(p);

        if (resident == null)
            return false;

        return resident.isMayor()
                || p.hasPermission(
                        "moodtownmenu.admin"
                );
    }
}