package fr.moodcraft.townmenu.handler;

import fr.moodcraft.townmenu.gui.TownMenuGUI;

import fr.moodcraft.townmenu.manager.TownInputManager;

import fr.moodcraft.townmenu.util.MessageUtil;
import fr.moodcraft.townmenu.util.SoundUtil;

import org.bukkit.entity.Player;

public class TownPlotHandler {

    public static void handle(
            Player p,
            int slot
    ) {

        switch (slot) {

            case 10 -> {

                SoundUtil.click(p);

                p.closeInventory();
                p.performCommand("plot claim");
            }

            case 12 -> {

                SoundUtil.click(p);

                p.closeInventory();
                p.performCommand("plot unclaim");
            }

            case 14 -> {

                SoundUtil.click(p);

                p.closeInventory();

                TownInputManager.wait(
                        p,
                        TownInputManager.Type.PLOT_TRUST
                );

                MessageUtil.send(
                        p,
                        "Parcelle",
                        "§7Écrivez le pseudo du joueur à ajouter."
                );
            }

            case 16 -> {

                SoundUtil.click(p);

                p.closeInventory();

                TownInputManager.wait(
                        p,
                        TownInputManager.Type.PLOT_UNTRUST
                );

                MessageUtil.send(
                        p,
                        "Parcelle",
                        "§7Écrivez le pseudo du joueur à retirer."
                );
            }

            case 21 -> {

                SoundUtil.click(p);

                p.closeInventory();

                TownInputManager.wait(
                        p,
                        TownInputManager.Type.PLOT_FOR_SALE
                );

                MessageUtil.send(
                        p,
                        "Parcelle",
                        "§7Écrivez le prix de vente dans le chat."
                );
            }

            case 23 -> {

                SoundUtil.click(p);

                p.closeInventory();
                p.performCommand("plot nfs");
            }

            case 31 -> {

                SoundUtil.back(p);

                TownMenuGUI.open(p);
            }
        }
    }
}