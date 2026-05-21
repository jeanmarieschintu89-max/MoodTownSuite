package fr.moodcraft.townmenu.handler;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

import fr.moodcraft.flag.api.MoodTownFlagAPI;
import fr.moodcraft.townmenu.gui.TownBankGUI;
import fr.moodcraft.townmenu.gui.TownDangerGUI;
import fr.moodcraft.townmenu.gui.TownMayorCitizensGUI;
import fr.moodcraft.townmenu.gui.TownMayorCityGUI;
import fr.moodcraft.townmenu.gui.TownMayorEconomyGUI;
import fr.moodcraft.townmenu.gui.TownMayorGUI;
import fr.moodcraft.townmenu.gui.TownMenuGUI;
import fr.moodcraft.townmenu.gui.TownSettingsGUI;
import fr.moodcraft.townmenu.gui.TownTaxGUI;
import fr.moodcraft.townmenu.manager.TownInputManager;
import fr.moodcraft.townmenu.util.MessageUtil;
import fr.moodcraft.townmenu.util.MoodStyle;
import fr.moodcraft.townmenu.util.SoundUtil;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TownMayorHandler {

    public static void handle(Player player, int slot) {

        switch (slot) {

            case 20 -> {
                SoundUtil.click(player);
                TownMayorCitizensGUI.open(player);
            }

            case 22 -> {
                SoundUtil.click(player);
                TownMayorCityGUI.open(player);
            }

            case 24 -> {
                SoundUtil.click(player);
                TownMayorEconomyGUI.open(player);
            }

            case 30 -> {
                SoundUtil.click(player);
                TownSettingsGUI.open(player);
            }

            case 32 -> openTownFlag(player);

            case 34 -> {
                SoundUtil.fail(player);
                TownDangerGUI.open(player);
            }

            case 49 -> {
                SoundUtil.back(player);
                TownMenuGUI.open(player);
            }

            default -> {
            }
        }
    }

    public static void handleCitizens(Player player, int slot) {

        switch (slot) {

            case 11 -> ask(
                    player,
                    TownInputManager.Type.TOWN_INVITE,
                    "Mairie Citoyens",
                    "§e➜ §fÉcrivez le pseudo du joueur à inviter.\n\n§8• §7Tapez §cannuler §7pour quitter."
            );

            case 13 -> ask(
                    player,
                    TownInputManager.Type.TOWN_KICK,
                    "Mairie Citoyens",
                    "§e➜ §fÉcrivez le pseudo du joueur à expulser.\n\n§8• §7Tapez §cannuler §7pour quitter."
            );

            case 15 -> ask(
                    player,
                    TownInputManager.Type.TOWN_ASSISTANT_ADD,
                    "Mairie Citoyens",
                    "§e➜ §fÉcrivez le pseudo du joueur à promouvoir assistant.\n\n§8• §7Tapez §cannuler §7pour quitter."
            );

            case 22 -> ask(
                    player,
                    TownInputManager.Type.TOWN_ASSISTANT_REMOVE,
                    "Mairie Citoyens",
                    "§e➜ §fÉcrivez le pseudo du joueur à retirer des assistants.\n\n§8• §7Tapez §cannuler §7pour quitter."
            );

            case 40 -> {
                SoundUtil.back(player);
                TownMayorGUI.open(player);
            }

            default -> {
            }
        }
    }

    public static void handleCity(Player player, int slot) {

        switch (slot) {

            case 11 -> {
                SoundUtil.click(player);
                player.closeInventory();
                player.performCommand("t set spawn");
            }

            case 13 -> ask(
                    player,
                    TownInputManager.Type.TOWN_RENAME,
                    "Mairie Ville",
                    "§e➜ §fÉcrivez le nouveau nom de la ville.\n\n§8• §7Tapez §cannuler §7pour quitter."
            );

            case 15 -> openTownFlag(player);

            case 40 -> {
                SoundUtil.back(player);
                TownMayorGUI.open(player);
            }

            default -> {
            }
        }
    }

    public static void handleEconomy(Player player, int slot) {

        switch (slot) {

            case 11 -> {
                SoundUtil.click(player);
                TownBankGUI.open(player);
            }

            case 13 -> openBusinessRegister(player);

            case 15 -> {
                SoundUtil.click(player);
                TownTaxGUI.open(player);
            }

            case 40 -> {
                SoundUtil.back(player);
                TownMayorGUI.open(player);
            }

            default -> {
            }
        }
    }

    private static void ask(
            Player player,
            TownInputManager.Type type,
            String module,
            String message
    ) {

        SoundUtil.click(player);
        player.closeInventory();
        TownInputManager.wait(player, type);
        MessageUtil.send(player, module, message);
    }

    private static void openTownFlag(Player player) {

        SoundUtil.click(player);
        player.closeInventory();

        Resident resident = getResident(player, "Drapeau municipal");

        if (resident == null) {
            return;
        }

        Town town = getTown(player, resident, "Drapeau municipal");

        if (town == null) {
            return;
        }

        if (!isMayor(player, resident, town)) {
            SoundUtil.fail(player);
            MessageUtil.send(
                    player,
                    "Drapeau municipal",
                    MoodStyle.error("Accès refusé.")
                            + "\n\n§8• §7Seul le maire peut modifier le drapeau municipal."
            );
            return;
        }

        MoodTownFlagAPI.openTownMenu(player, town);
    }

    private static void openBusinessRegister(Player player) {

        SoundUtil.click(player);
        player.closeInventory();

        Resident resident = getResident(player, "Bureau économique");

        if (resident == null) {
            return;
        }

        Town town = getTown(player, resident, "Bureau économique");

        if (town == null) {
            return;
        }

        if (!isMayor(player, resident, town)) {
            SoundUtil.fail(player);
            MessageUtil.send(
                    player,
                    "Bureau économique",
                    MoodStyle.error("Accès refusé.")
                            + "\n\n§8• §7Seul le maire peut ouvrir ce registre municipal."
            );
            return;
        }

        if (!Bukkit.getPluginManager().isPluginEnabled("MoodBusiness")) {
            SoundUtil.fail(player);
            MessageUtil.send(
                    player,
                    "Bureau économique",
                    MoodStyle.error("Module indisponible.")
                            + "\n\n§8• §7MoodBusiness n'est pas chargé."
                            + "\n§8• §7Service officiel de §aMood§6Craft§7."
            );
            return;
        }

        MessageUtil.send(
                player,
                "Bureau économique",
                MoodStyle.success("Ouverture du Bureau des Entreprises.")
                        + "\n\n§8• §7Ville : §b" + town.getName()
                        + "\n§8• §7Contrats municipaux"
                        + "\n§8• §7Entreprises et litiges"
        );

        player.performCommand("entreprise");
    }

    private static Resident getResident(Player player, String module) {

        try {
            Resident resident = TownyAPI.getInstance().getResident(player);

            if (resident == null || !resident.hasTown()) {
                SoundUtil.fail(player);
                MessageUtil.send(
                        player,
                        module,
                        MoodStyle.error("Action impossible.")
                                + "\n\n§8• §7Vous n'appartenez à aucune ville."
                );
                return null;
            }

            return resident;
        } catch (Exception exception) {
            SoundUtil.fail(player);
            MessageUtil.send(
                    player,
                    module,
                    MoodStyle.error("Impossible de charger votre profil Towny.")
            );
            return null;
        }
    }

    private static Town getTown(
            Player player,
            Resident resident,
            String module
    ) {

        try {
            return resident.getTown();
        } catch (Exception exception) {
            SoundUtil.fail(player);
            MessageUtil.send(
                    player,
                    module,
                    MoodStyle.error("Impossible de charger votre ville.")
            );
            return null;
        }
    }

    private static boolean isMayor(
            Player player,
            Resident resident,
            Town town
    ) {

        try {
            return town.getMayor().equals(resident)
                    || player.hasPermission("moodtownmenu.admin");
        } catch (Exception exception) {
            return player.hasPermission("moodtownmenu.admin");
        }
    }
}
