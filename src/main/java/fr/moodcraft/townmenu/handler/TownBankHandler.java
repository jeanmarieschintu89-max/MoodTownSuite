package fr.moodcraft.townmenu.handler;

import fr.moodcraft.townmenu.gui.TownMenuGUI;

import fr.moodcraft.townmenu.manager.TownInputManager;

import fr.moodcraft.townmenu.util.SoundUtil;
import fr.moodcraft.townmenu.util.TownyUtil;
import fr.moodcraft.townmenu.util.MessageUtil;

import org.bukkit.entity.Player;

public class TownBankHandler {

    public static void handle(
            Player p,
            int slot
    ) {

        switch (slot) {

            case 11 -> {

                SoundUtil.click(p);

                p.closeInventory();

                TownInputManager.wait(
                        p,
                        TownInputManager.Type.TOWN_DEPOSIT
                );

                MessageUtil.send(
                        p,
                        "Banque ville",
                        "§7Écrivez le montant à déposer dans le chat."
                );
            }

            case 13 -> {

                if (!TownyUtil.canManageTown(p)) {

                    SoundUtil.click(p);

                    p.closeInventory();
                    p.performCommand("t");

                    return;
                }

                SoundUtil.click(p);

                p.closeInventory();

                TownInputManager.wait(
                        p,
                        TownInputManager.Type.TOWN_WITHDRAW
                );

                MessageUtil.send(
                        p,
                        "Banque ville",
                        "§7Écrivez le montant à retirer dans le chat."
                );
            }

            case 15 -> {

                SoundUtil.click(p);

                p.closeInventory();
                p.performCommand("t");
            }

            case 31 -> {

                SoundUtil.back(p);

                TownMenuGUI.open(p);
            }
        }
    }
}