package fr.moodcraft.townmenu.handler;

import fr.moodcraft.townmenu.gui.TownBankGUI;
import fr.moodcraft.townmenu.gui.TownClaimsGUI;
import fr.moodcraft.townmenu.gui.TownMayorGUI;
import fr.moodcraft.townmenu.gui.TownNationGUI;
import fr.moodcraft.townmenu.gui.TownPlotGUI;
import fr.moodcraft.townmenu.gui.TownResidentsGUI;
import fr.moodcraft.townmenu.gui.TownSettingsGUI;

import fr.moodcraft.townmenu.util.MoodStyle;
import fr.moodcraft.townmenu.util.SoundUtil;
import fr.moodcraft.townmenu.util.TownyUtil;

import org.bukkit.entity.Player;

public class TownMenuHandler {

    public static void handle(
            Player p,
            int slot
    ) {

        boolean hasTown =
                TownyUtil.hasTown(p);

        if (!hasTown) {

            handleNoTown(p, slot);
            return;
        }

        switch (slot) {

            case 4 -> {
                SoundUtil.click(p);
                p.closeInventory();
                p.performCommand("town");
            }

            case 10 -> {
                SoundUtil.click(p);
                TownResidentsGUI.open(p);
            }

            case 12 -> {
                SoundUtil.click(p);
                TownPlotGUI.open(p);
            }

            case 14 -> {
                SoundUtil.click(p);
                TownClaimsGUI.open(p);
            }

            case 16 -> {
                SoundUtil.click(p);
                TownNationGUI.open(p);
            }

            case 21 -> {
                SoundUtil.click(p);
                TownBankGUI.open(p);
            }

            case 23 -> {
                SoundUtil.click(p);
                p.closeInventory();
                p.performCommand("urbanisme");
            }

            case 25 -> {
                SoundUtil.click(p);
                p.closeInventory();
                p.performCommand("drapeau");
            }

            case 30 -> {
                if (!TownyUtil.canManageTown(p)) {
                    return;
                }

                SoundUtil.click(p);
                TownMayorGUI.open(p);
            }

            case 32 -> {
                if (!TownyUtil.canManageTown(p)) {
                    return;
                }

                SoundUtil.click(p);
                TownSettingsGUI.open(p);
            }

            case 49 -> {
                SoundUtil.back(p);
                p.closeInventory();
                p.performCommand("menu");
            }
        }
    }

    private static void handleNoTown(
            Player p,
            int slot
    ) {

        switch (slot) {

            case 20 -> {
                SoundUtil.click(p);
                p.closeInventory();
                p.performCommand("t join utopia");
            }

            case 22 -> {
                SoundUtil.click(p);
                p.closeInventory();
                p.performCommand("town list");
            }

            case 24 -> {
                SoundUtil.click(p);
                p.closeInventory();
                MoodStyle.send(
                        p,
                        "Menu Ville",
                        MoodStyle.info("Créer une ville."),
                        MoodStyle.detail("Gagne assez d'argent pour payer la création."),
                        MoodStyle.detail("Va à l'endroit où tu veux commencer."),
                        MoodStyle.detail("Commande : §e/t create <nom>"),
                        MoodStyle.detail("Tu peux aussi rejoindre Utopia pour débuter.")
                );
            }

            case 49 -> {
                SoundUtil.back(p);
                p.closeInventory();
                p.performCommand("menu");
            }

            default -> {
            }
        }
    }
}
