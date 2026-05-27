package fr.moodcraft.donville.command;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;

import fr.moodcraft.donville.manager.DonVilleManager;
import fr.moodcraft.donville.model.DonVilleDonation;
import fr.moodcraft.tgrade.towny.TownyHook;
import fr.moodcraft.townmenu.util.MoodStyle;
import fr.moodcraft.townmenu.util.SoundUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DonVilleCommand implements CommandExecutor {

    private static final SimpleDateFormat DATE = new SimpleDateFormat("dd/MM HH:mm");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Commande joueur uniquement.");
            return true;
        }

        if (args.length == 0) {
            help(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("creer") || args[0].equalsIgnoreCase("créer")) {
            create(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("supprimer") || args[0].equalsIgnoreCase("delete")) {
            delete(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("donner") || args[0].equalsIgnoreCase("don")) {
            donate(player, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("historique") || args[0].equalsIgnoreCase("hist")) {
            history(player, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("liste") || args[0].equalsIgnoreCase("list")) {
            list(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("recharger") || args[0].equalsIgnoreCase("reload")) {
            reload(player);
            return true;
        }

        if (args.length >= 2) {
            donateDirect(player, args[0], args[1]);
            return true;
        }

        help(player);
        return true;
    }

    private void create(Player player) {
        if (!TownyHook.canManage(player)) {
            MoodStyle.send(player, "DonVille", "§cVous devez être maire ou assistant de ville.");
            SoundUtil.fail(player);
            return;
        }

        Town town = TownyAPI.getInstance().getTown(player);

        if (town == null) {
            MoodStyle.send(player, "DonVille", "§cVous devez avoir une ville.");
            SoundUtil.fail(player);
            return;
        }

        String townName = DonVilleManager.safeTownName(town);

        if (DonVilleManager.isEnabled(townName)) {
            MoodStyle.send(player, "DonVille", "§cVotre ville possède déjà une boîte à dons.");
            SoundUtil.fail(player);
            return;
        }

        DonVilleManager.createBox(town);
        MoodStyle.send(
                player,
                "DonVille",
                "§aBoîte à dons créée pour §b" + townName + "§a.",
                "§7Les joueurs peuvent donner avec §e/don " + townName + " <montant>§7."
        );
        SoundUtil.success(player);
    }

    private void delete(Player player) {
        if (!TownyHook.canManage(player)) {
            MoodStyle.send(player, "DonVille", "§cVous devez être maire ou assistant de ville.");
            SoundUtil.fail(player);
            return;
        }

        Town town = TownyAPI.getInstance().getTown(player);

        if (town == null) {
            MoodStyle.send(player, "DonVille", "§cVous devez avoir une ville.");
            SoundUtil.fail(player);
            return;
        }

        String townName = DonVilleManager.safeTownName(town);

        if (DonVilleManager.removeBox(town)) {
            MoodStyle.send(player, "DonVille", "§aBoîte à dons supprimée pour §b" + townName + "§a.");
            SoundUtil.success(player);
        } else {
            MoodStyle.send(player, "DonVille", "§cVotre ville n'avait pas de boîte à dons active.");
            SoundUtil.fail(player);
        }
    }

    private void donate(Player player, String[] args) {
        if (args.length < 3) {
            MoodStyle.send(player, "DonVille", "§c/donville donner <ville> <montant>", "§7Version courte : §e/don <ville> <montant>");
            SoundUtil.fail(player);
            return;
        }

        donateDirect(player, args[1], args[2]);
    }

    private void donateDirect(Player player, String townName, String amountText) {
        double amount;

        try {
            amount = Double.parseDouble(amountText.replace(",", "."));
        } catch (NumberFormatException e) {
            MoodStyle.send(player, "DonVille", "§cMontant invalide : §e" + amountText);
            SoundUtil.fail(player);
            return;
        }

        DonVilleManager.DonateResult result = DonVilleManager.donate(player, townName, amount);
        MoodStyle.send(player, "DonVille", result.message());

        if (result.success()) {
            SoundUtil.success(player);
        } else {
            SoundUtil.fail(player);
        }
    }

    private void history(Player player, String[] args) {
        if (args.length < 2) {
            MoodStyle.send(player, "DonVille", "§c/donville historique <ville>");
            SoundUtil.fail(player);
            return;
        }

        Town town = TownyAPI.getInstance().getTown(args[1]);

        if (town == null) {
            MoodStyle.send(player, "DonVille", "§cVille introuvable : §e" + args[1]);
            SoundUtil.fail(player);
            return;
        }

        String townName = DonVilleManager.safeTownName(town);
        List<DonVilleDonation> history = DonVilleManager.getHistory(townName);

        if (history.isEmpty()) {
            MoodStyle.send(player, "DonVille", "§7Aucun don enregistré pour §b" + townName + "§7.");
            return;
        }

        String[] lines = new String[Math.min(history.size(), 6) + 1];
        lines[0] = "§7Derniers dons pour §b" + townName;

        int index = 1;
        for (DonVilleDonation donation : history) {
            if (index >= lines.length) {
                break;
            }

            lines[index] = "§e" + DonVilleManager.format(donation.getAmount()) + "€ §7par §f" + donation.getDonorName() + " §8(" + DATE.format(new Date(donation.getCreatedAt())) + ")";
            index++;
        }

        MoodStyle.send(player, "DonVille", lines);
    }

    private void list(Player player) {
        List<Town> towns = DonVilleManager.getEnabledTowns();

        if (towns.isEmpty()) {
            MoodStyle.send(player, "DonVille", "§cAucune ville n'a encore de boîte à dons active.");
            return;
        }

        String[] lines = new String[Math.min(towns.size(), 8) + 1];
        lines[0] = "§7Villes acceptant les dons :";

        int index = 1;
        for (Town town : towns) {
            if (index >= lines.length) {
                break;
            }

            String townName = DonVilleManager.safeTownName(town);
            lines[index] = "§b" + townName + " §8- §e/don " + townName + " <montant>";
            index++;
        }

        MoodStyle.send(player, "DonVille", lines);
    }

    private void reload(Player player) {
        if (!player.hasPermission("moodtownsuite.donville.admin")) {
            MoodStyle.send(player, "DonVille", "§cAccès refusé.");
            SoundUtil.fail(player);
            return;
        }

        DonVilleManager.reload();
        MoodStyle.send(player, "DonVille", "§aBoîtes à dons rechargées.");
        SoundUtil.success(player);
    }

    private void help(Player player) {
        MoodStyle.send(
                player,
                "DonVille",
                "§e/don <ville> <montant> §7- Donner à une ville",
                "§e/donville creer §7- Créer la boîte de sa ville",
                "§e/donville supprimer §7- Supprimer la boîte de sa ville",
                "§e/donville liste §7- Voir les villes qui acceptent les dons",
                "§e/donville historique <ville> §7- Voir les derniers dons"
        );
    }
}
