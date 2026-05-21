package fr.moodcraft.townmenu.handler;

import com.palmergames.bukkit.towny.TownyAPI;

import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

import fr.moodcraft.townmenu.gui.TownMenuGUI;

import fr.moodcraft.townmenu.util.SoundUtil;

import org.bukkit.Bukkit;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TownResidentsHandler {

    public static void handle(
            Player p,
            int slot
    ) {

        switch (slot) {

            case 11 -> {

                SoundUtil.click(p);

                p.closeInventory();

                sendResidentsList(p);
            }

            case 15 -> {

                SoundUtil.click(p);

                p.closeInventory();

                p.performCommand(
                        "t spawn"
                );
            }

            case 31 -> {

                SoundUtil.back(p);

                TownMenuGUI.open(p);
            }
        }
    }

    private static void sendResidentsList(
            Player p
    ) {

        Resident resident =
                TownyAPI.getInstance()
                        .getResident(p);

        if (resident == null || !resident.hasTown()) {

            SoundUtil.fail(p);

            header(p);
            p.sendMessage("§c✖ §fVous n'appartenez à aucune ville.");
            p.sendMessage("");
            p.sendMessage("§8• §7Impossible d'afficher les habitants");
            footer(p);

            return;
        }

        Town town;

        try {

            town =
                    resident.getTown();

        } catch (Exception e) {

            SoundUtil.fail(p);

            header(p);
            p.sendMessage("§c✖ §fImpossible de charger votre ville.");
            p.sendMessage("");
            p.sendMessage("§8• §7Réessayez dans quelques secondes");
            footer(p);

            return;
        }

        List<String> names =
                new ArrayList<>();

        int online =
                0;

        for (Resident r : town.getResidents()) {

            if (r == null) {
                continue;
            }

            String name =
                    r.getName();

            boolean isOnline =
                    Bukkit.getPlayerExact(name) != null;

            if (isOnline) {
                online++;
            }

            boolean isMayor =
                    town.getMayor() != null
                            && town.getMayor()
                            .equals(r);

            String display =
                    isOnline
                            ? "§a" + name
                            : "§7" + name;

            if (isMayor) {

                display =
                        display + " §6★";
            }

            names.add(display);
        }

        Collections.sort(
                names,
                String.CASE_INSENSITIVE_ORDER
        );

        header(p);
        p.sendMessage("§e➜ §fListe des habitants affichée.");
        p.sendMessage("");
        p.sendMessage("§8• §7Ville: §b" + town.getName());
        p.sendMessage("§8• §7Total: §e" + names.size() + " habitant(s)");
        p.sendMessage("§8• §7En ligne: §a" + online);
        p.sendMessage("§8• §7Maire: §6★");
        p.sendMessage("");

        if (names.isEmpty()) {

            p.sendMessage("§c✖ §fAucun habitant trouvé.");
            footer(p);

            SoundUtil.fail(p);

            return;
        }

        StringBuilder line =
                new StringBuilder();

        int count =
                0;

        for (String name : names) {

            if (line.length() > 0) {
                line.append("§8, ");
            }

            line.append(name);

            count++;

            if (count % 5 == 0) {

                p.sendMessage(
                        "§8• §7" + line
                );

                line.setLength(0);
            }
        }

        if (line.length() > 0) {

            p.sendMessage(
                    "§8• §7" + line
            );
        }

        footer(p);

        SoundUtil.success(p);
    }

    private static void header(
            Player p
    ) {

        p.sendMessage("");
        p.sendMessage("§8----- §6✦ Habitants §aMood§6Craft §6✦ §8-----");
        p.sendMessage("");
    }

    private static void footer(
            Player p
    ) {

        p.sendMessage("");
        p.sendMessage("§8-----------------------------");
        p.sendMessage("");
    }
}