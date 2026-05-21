package fr.moodcraft.townmenu.handler;

import fr.moodcraft.townmenu.gui.TownSettingsGUI;

import fr.moodcraft.townmenu.util.MessageUtil;
import fr.moodcraft.townmenu.util.SoundUtil;

import org.bukkit.entity.Player;

public class TownPermissionsHandler {

    public static void handle(
            Player p,
            int slot
    ) {

        switch (slot) {

            case 10 -> setPermission(
                    p,
                    "build",
                    true,
                    "Construction autorisée pour les extérieurs."
            );

            case 19 -> setPermission(
                    p,
                    "build",
                    false,
                    "Construction interdite pour les extérieurs."
            );

            case 12 -> setPermission(
                    p,
                    "destroy",
                    true,
                    "Destruction autorisée pour les extérieurs."
            );

            case 21 -> setPermission(
                    p,
                    "destroy",
                    false,
                    "Destruction interdite pour les extérieurs."
            );

            case 14 -> setPermission(
                    p,
                    "switch",
                    true,
                    "Interactions autorisées pour les extérieurs."
            );

            case 23 -> setPermission(
                    p,
                    "switch",
                    false,
                    "Interactions interdites pour les extérieurs."
            );

            case 16 -> setPermission(
                    p,
                    "itemuse",
                    true,
                    "Utilisation autorisée pour les extérieurs."
            );

            case 25 -> setPermission(
                    p,
                    "itemuse",
                    false,
                    "Utilisation interdite pour les extérieurs."
            );

            case 49 -> {

                SoundUtil.back(p);

                TownSettingsGUI.open(p);
            }
        }
    }

    private static void setPermission(
            Player p,
            String permission,
            boolean enabled,
            String message
    ) {

        SoundUtil.click(p);

        p.closeInventory();

        p.performCommand(
                "t set perm outsider "
                        + permission
                        + " "
                        + (enabled ? "on" : "off")
        );

        MessageUtil.send(
                p,
                "Permissions ville",
                "§a✔ " + message + "\n" +
                        "§8Conseil: §7utilisez §e/t set perm reset §7si certaines parcelles gardent d'anciens réglages."
        );
    }
}