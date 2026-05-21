package fr.moodcraft.townmenu.handler;

import fr.moodcraft.townmenu.gui.TownMenuGUI;

import fr.moodcraft.townmenu.util.SoundUtil;

import org.bukkit.entity.Player;

public class TownNationHandler {

    public static void handle(
            Player p,
            int slot
    ) {

        switch (slot) {

            case 11 -> {

                SoundUtil.click(p);

                p.closeInventory();
                p.performCommand("nation");
            }

            case 13 -> {

                SoundUtil.click(p);

                p.closeInventory();
                p.performCommand("n spawn");
            }

            case 15 -> {

                SoundUtil.click(p);

                p.closeInventory();
                p.performCommand("drapeau nation");
            }

            case 31 -> {

                SoundUtil.back(p);

                TownMenuGUI.open(p);
            }
        }
    }
}