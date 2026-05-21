package fr.moodcraft.townmenu.handler;

import fr.moodcraft.townmenu.gui.TownMenuGUI;
import fr.moodcraft.townmenu.gui.TownPermissionsGUI;
import fr.moodcraft.townmenu.gui.TownTaxGUI;

import fr.moodcraft.townmenu.util.SoundUtil;

import org.bukkit.entity.Player;

public class TownSettingsHandler {

    public static void handle(
            Player p,
            int slot
    ) {

        switch (slot) {

            case 10 -> {

                SoundUtil.click(p);

                p.closeInventory();
                p.performCommand("t toggle open");
            }

            case 13 -> {

                SoundUtil.click(p);

                TownPermissionsGUI.open(p);
            }

            case 16 -> {

                SoundUtil.click(p);

                TownTaxGUI.open(p);
            }

            case 31 -> {

                SoundUtil.back(p);

                TownMenuGUI.open(p);
            }
        }
    }
}