package fr.moodcraft.pubville.command;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;

import fr.moodcraft.pubville.gui.PubVilleGUI;
import fr.moodcraft.pubville.manager.PubVilleManager;
import fr.moodcraft.pubville.model.PubVilleCampaign;
import fr.moodcraft.tgrade.towny.TownyHook;
import fr.moodcraft.townmenu.util.MoodStyle;
import fr.moodcraft.townmenu.util.SoundUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PubVilleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Commande joueur uniquement.");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("ouvrir")) {
            PubVilleGUI.open(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {
            sendInfo(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("liste")) {
            PubVilleGUI.open(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("visiter")) {
            if (args.length < 2) {
                MoodStyle.send(player, "PubVille", "§c/pubville visiter <ville>");
                return true;
            }

            PubVilleManager.teleport(player, args[1]);
            return true;
        }

        if (args[0].equalsIgnoreCase("acheter")) {
            buy(player, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("annuler")) {
            cancel(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("admin")) {
            admin(player, args);
            return true;
        }

        help(player);
        return true;
    }

    private void buy(Player player, String[] args) {
        if (!TownyHook.canManage(player)) {
            MoodStyle.send(player, "PubVille", "§cVous devez être maire ou assistant de ville.");
            SoundUtil.fail(player);
            return;
        }

        if (args.length < 2) {
            MoodStyle.send(player, "PubVille", "§c/pubville acheter <petite|moyenne|grande>");
            return;
        }

        PubVilleManager.BuyResult result = PubVilleManager.buy(player, args[1]);

        MoodStyle.send(player, "PubVille", result.message());

        if (result.success()) {
            SoundUtil.success(player);
        } else {
            SoundUtil.fail(player);
        }
    }

    private void cancel(Player player) {
        if (!TownyHook.canManage(player)) {
            MoodStyle.send(player, "PubVille", "§cVous devez être maire ou assistant de ville.");
            SoundUtil.fail(player);
            return;
        }

        Town town = TownyAPI.getInstance().getTown(player);

        if (town == null) {
            MoodStyle.send(player, "PubVille", "§cVous devez être dans une ville.");
            return;
        }

        if (PubVilleManager.removeCampaign(town.getName())) {
            MoodStyle.send(player, "PubVille", "§aPublicité annulée pour §b" + town.getName() + "§a.");
            SoundUtil.success(player);
        } else {
            MoodStyle.send(player, "PubVille", "§cVotre ville n'a aucune publicité active.");
            SoundUtil.fail(player);
        }
    }

    private void admin(Player player, String[] args) {
        if (!player.hasPermission("moodtownsuite.pubville.admin")) {
            MoodStyle.send(player, "PubVille", "§cAccès refusé.");
            SoundUtil.fail(player);
            return;
        }

        if (args.length < 2) {
            adminHelp(player);
            return;
        }

        if (args[1].equalsIgnoreCase("definirspawn")) {
            if (args.length < 3) {
                MoodStyle.send(player, "PubVille", "§c/pubville admin definirspawn <ville>");
                return;
            }

            String townName = args[2];
            Town town = TownyAPI.getInstance().getTown(townName);

            if (town == null) {
                MoodStyle.send(player, "PubVille", "§cVille introuvable : §e" + townName);
                SoundUtil.fail(player);
                return;
            }

            PubVilleManager.setSpawn(town.getName(), player.getLocation());
            MoodStyle.send(player, "PubVille", "§aPoint publicitaire défini pour §b" + town.getName() + "§a.");
            SoundUtil.success(player);
            return;
        }

        if (args[1].equalsIgnoreCase("definirpnj")) {
            MoodStyle.send(
                    player,
                    "PubVille",
                    "§aCommande PNJ à utiliser : §e/pubville ouvrir",
                    "§7Configurez votre PNJ Citizens pour exécuter cette commande au clic.",
                    "§7Les visiteurs n'auront rien à taper."
            );
            SoundUtil.success(player);
            return;
        }

        if (args[1].equalsIgnoreCase("retirer")) {
            if (args.length < 3) {
                MoodStyle.send(player, "PubVille", "§c/pubville admin retirer <ville>");
                return;
            }

            if (PubVilleManager.removeCampaign(args[2])) {
                MoodStyle.send(player, "PubVille", "§aCampagne retirée pour §b" + args[2] + "§a.");
                SoundUtil.success(player);
            } else {
                MoodStyle.send(player, "PubVille", "§cAucune campagne active pour §e" + args[2] + "§c.");
                SoundUtil.fail(player);
            }
            return;
        }

        if (args[1].equalsIgnoreCase("recharger")) {
            PubVilleManager.reload();
            player.getServer().getPluginManager().getPlugin("MoodTownSuite").reloadConfig();
            MoodStyle.send(player, "PubVille", "§aConfiguration et publicités rechargées.");
            SoundUtil.success(player);
            return;
        }

        adminHelp(player);
    }

    private void sendInfo(Player player) {
        MoodStyle.send(
                player,
                "PubVille",
                "§7Petite : §e" + PubVilleManager.format(PubVilleManager.price("petite")) + "€ §8/ §a" + PubVilleManager.hours("petite") + "h",
                "§7Moyenne : §e" + PubVilleManager.format(PubVilleManager.price("moyenne")) + "€ §8/ §a" + PubVilleManager.hours("moyenne") + "h",
                "§7Grande : §e" + PubVilleManager.format(PubVilleManager.price("grande")) + "€ §8/ §a" + PubVilleManager.hours("grande") + "h"
        );
    }

    private void help(Player player) {
        MoodStyle.send(
                player,
                "PubVille",
                "§e/pubville ouvrir §7- Ouvrir le menu",
                "§e/pubville acheter <petite|moyenne|grande> §7- Acheter une publicité",
                "§e/pubville annuler §7- Annuler la publicité de votre ville",
                "§e/pubville info §7- Voir les prix"
        );
    }

    private void adminHelp(Player player) {
        MoodStyle.send(
                player,
                "PubVille Admin",
                "§e/pubville admin definirspawn <ville>",
                "§e/pubville admin definirpnj",
                "§e/pubville admin retirer <ville>",
                "§e/pubville admin recharger"
        );
    }
}
