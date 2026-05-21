package fr.moodcraft.townmenu.handler;

import fr.moodcraft.townmenu.gui.TownMenuGUI;

import fr.moodcraft.townmenu.util.SoundUtil;

import org.bukkit.entity.Player;

public class TownClaimsHandler {

    public static void handle(
            Player p,
            int slot
    ) {

        switch (slot) {

            case 10 -> {

                SoundUtil.click(p);

                p.closeInventory();
                p.performCommand("t claim");
            }

            case 12 -> {

                SoundUtil.click(p);

                p.closeInventory();
                p.performCommand("t unclaim");
            }

            case 14 -> {

                SoundUtil.click(p);

                p.closeInventory();
                p.performCommand("towny map");
            }

            case 16 -> {

                SoundUtil.click(p);

                p.closeInventory();
                p.performCommand("t claim auto");
            }

            case 31 -> {

                SoundUtil.back(p);

                TownMenuGUI.open(p);
            }
        }
    }
}