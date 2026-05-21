package fr.moodcraft.townmenu.handler;

import fr.moodcraft.townmenu.gui.TownMayorGUI;

import fr.moodcraft.townmenu.util.MessageUtil;
import fr.moodcraft.townmenu.util.SoundUtil;
import fr.moodcraft.townmenu.util.TownyUtil;

import org.bukkit.entity.Player;

public class TownDangerHandler {

    public static void handle(
            Player p,
            int slot
    ) {

        switch (slot) {

            case 11 -> {

                SoundUtil.fail(p);

                p.closeInventory();

                MessageUtil.send(
                        p,
                        "Zone sensible",
                        "§7Pour quitter votre ville, tapez §e/t leave§7."
                );
            }

            case 13 -> {

                if (!TownyUtil.isMayor(p)) {

                    SoundUtil.fail(p);

                    MessageUtil.error(
                            p,
                            "Seul le maire peut supprimer la ville."
                    );

                    return;
                }

                SoundUtil.fail(p);

                p.closeInventory();

                MessageUtil.send(
                        p,
                        "Zone sensible",
                        "§cSuppression de ville : tapez §e/t delete §cpour confirmer avec Towny."
                );
            }

            case 15 -> {

                SoundUtil.click(p);

                p.closeInventory();
                p.performCommand("t unclaim");
            }

            case 31 -> {

                SoundUtil.back(p);

                TownMayorGUI.open(p);
            }
        }
    }
}