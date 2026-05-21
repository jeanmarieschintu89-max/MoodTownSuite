package fr.moodcraft.tgrade.command;

import fr.moodcraft.tgrade.manager.GradeManager;
import fr.moodcraft.tgrade.manager.NationalScoreCalculator;
import fr.moodcraft.tgrade.model.TownGrade;
import fr.moodcraft.tgrade.storage.GradeStorage;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Locale;

public class UrbanismeAdminCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("moodtowngrade.admin")) {
            error(sender, "Accès réservé à l'administration urbaine.");
            return true;
        }

        if (args.length == 0) {
            help(sender);
            return true;
        }

        String sub = args[0].toLowerCase(Locale.ROOT);

        switch (sub) {
            case "help", "aide" -> help(sender);
            case "status", "etat", "état" -> status(sender);
            case "info" -> info(sender, args);
            case "set" -> set(sender, args);
            case "resetville" -> resetTown(sender, args);
            case "deleteville" -> deleteTown(sender, args);
            case "finish", "terminer" -> finish(sender, args, true);
            case "unfinish", "unterminer" -> finish(sender, args, false);
            case "lock" -> lock(sender, args, true);
            case "unlock" -> lock(sender, args, false);
            case "resetall" -> resetAll(sender);
            case "reload" -> reload(sender);
            default -> help(sender);
        }

        return true;
    }

    private void status(CommandSender sender) {
        GradeManager.loadAll();

        header(sender, "Admin Urbanisme");
        sender.sendMessage("§e➜ §7Villes chargées : §e" + GradeManager.getAll().size());

        TownGrade best = GradeManager.getBestTown();
        if (best != null) {
            sender.sendMessage("§e➜ §7Meilleure ville : §e" + best.getTown() + " §8• " + best.getFormattedScore());
        } else {
            sender.sendMessage("§e➜ §7Meilleure ville : §caucune ville terminée");
        }

        footer(sender);
    }

    private void info(CommandSender sender, String[] args) {
        if (args.length < 2) {
            usage(sender, "/urbanismeadmin info <ville>");
            return;
        }

        TownGrade grade = GradeManager.get(join(args, 1));

        header(sender, "Dossier Urbain");
        sender.sendMessage("§e➜ §7Ville : §e" + grade.getTown());
        sender.sendMessage("§e➜ §7Score : " + grade.getFormattedScore());
        sender.sendMessage("§e➜ §7Rang : §f" + grade.getRank());
        sender.sendMessage("§e➜ §7Terminé : " + (grade.isFinished() ? "§aoui" : "§cnon"));
        sender.sendMessage("§e➜ §7Verrouillé : " + (grade.isLocked() ? "§aoui" : "§cnon"));
        sender.sendMessage("§e➜ §7Bourse : §e" + grade.getPayout() + "$");
        sender.sendMessage("§e➜ §7Architecture : §e" + grade.getArchitecture() + "§7/10");
        sender.sendMessage("§e➜ §7Style : §e" + grade.getStyle() + "§7/6");
        sender.sendMessage("§e➜ §7Activité : §e" + grade.getActivite() + "§7/8");
        sender.sendMessage("§e➜ §7Banque : §e" + grade.getBanque() + "§7/4");
        sender.sendMessage("§e➜ §7Remarquable : §e" + grade.getRemarquable() + "§7/8");
        sender.sendMessage("§e➜ §7RP : §e" + grade.getRp() + "§7/6");
        sender.sendMessage("§e➜ §7Taille : §e" + grade.getTaille() + "§7/3");
        sender.sendMessage("§e➜ §7Votes : §e" + grade.getVotes() + "§7/5");
        footer(sender);
    }

    private void set(CommandSender sender, String[] args) {
        if (args.length < 4) {
            usage(sender, "/urbanismeadmin set <ville> <critere> <valeur>");
            return;
        }

        Integer value = parseInt(args[args.length - 1]);
        if (value == null) {
            error(sender, "Valeur invalide.");
            return;
        }

        String criteria = args[args.length - 2].toLowerCase(Locale.ROOT);
        TownGrade grade = GradeManager.get(join(args, 1, args.length - 2));

        boolean ok = switch (criteria) {
            case "architecture", "archi" -> { grade.setArchitecture(value); yield true; }
            case "style" -> { grade.setStyle(value); yield true; }
            case "activite", "activité" -> { grade.setActivite(value); yield true; }
            case "banque" -> { grade.setBanque(value); yield true; }
            case "remarquable" -> { grade.setRemarquable(value); yield true; }
            case "rp" -> { grade.setRp(value); yield true; }
            case "taille" -> { grade.setTaille(value); yield true; }
            case "votes" -> { grade.setVotes(value); yield true; }
            default -> false;
        };

        if (!ok) {
            error(sender, "Critère inconnu.");
            return;
        }

        GradeManager.save(grade);
        success(sender, "Note modifiée : §e" + grade.getTown() + " §8• §e" + criteria + " §8= §e" + value);
    }

    private void resetTown(CommandSender sender, String[] args) {
        if (args.length < 2) {
            usage(sender, "/urbanismeadmin resetville <ville>");
            return;
        }

        TownGrade grade = GradeManager.get(join(args, 1));
        resetGrade(grade);
        GradeManager.save(grade);
        success(sender, "Dossier ville réinitialisé : §e" + grade.getTown());
    }

    private void deleteTown(CommandSender sender, String[] args) {
        if (args.length < 2) {
            usage(sender, "/urbanismeadmin deleteville <ville>");
            return;
        }

        String town = join(args, 1);
        GradeStorage.delete(town);
        GradeManager.clearCache();
        GradeManager.loadAll();
        success(sender, "Dossier ville supprimé : §e" + town);
    }

    private void finish(CommandSender sender, String[] args, boolean finished) {
        if (args.length < 2) {
            usage(sender, finished ? "/urbanismeadmin finish <ville>" : "/urbanismeadmin unfinish <ville>");
            return;
        }

        TownGrade grade = GradeManager.get(join(args, 1));
        grade.setFinished(finished);
        GradeManager.save(grade);
        success(sender, "État terminé modifié : §e" + grade.getTown() + " §8= " + (finished ? "§aoui" : "§cnon"));
    }

    private void lock(CommandSender sender, String[] args, boolean locked) {
        if (args.length < 2) {
            usage(sender, locked ? "/urbanismeadmin lock <ville>" : "/urbanismeadmin unlock <ville>");
            return;
        }

        TownGrade grade = GradeManager.get(join(args, 1));
        grade.setLocked(locked);
        grade.setFinalScore(NationalScoreCalculator.getFinalScore(grade.getTown()));
        GradeManager.save(grade);
        success(sender, "Verrouillage modifié : §e" + grade.getTown() + " §8= " + (locked ? "§aoui" : "§cnon"));
    }

    private void resetAll(CommandSender sender) {
        GradeManager.loadAll();
        GradeManager.resetWeek();
        success(sender, "Tous les dossiers urbains ont été réinitialisés.");
    }

    private void reload(CommandSender sender) {
        GradeStorage.reload();
        GradeManager.clearCache();
        GradeManager.loadAll();
        success(sender, "Données urbanisme rechargées.");
    }

    private void resetGrade(TownGrade grade) {
        grade.setArchitecture(0);
        grade.setStyle(0);
        grade.setActivite(0);
        grade.setBanque(0);
        grade.setRemarquable(0);
        grade.setRp(0);
        grade.setTaille(0);
        grade.setVotes(0);
        grade.setFinished(false);
        grade.setPayoutClaimed(false);
        grade.setLocked(false);
        grade.setFinalScore(0);
    }

    private Integer parseInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (Exception e) {
            return null;
        }
    }

    private String join(String[] args, int start) {
        return join(args, start, args.length);
    }

    private String join(String[] args, int start, int end) {
        return String.join(" ", java.util.Arrays.copyOfRange(args, start, end));
    }

    private void help(CommandSender sender) {
        header(sender, "Admin Urbanisme");
        sender.sendMessage("§e➜ §7/urbanismeadmin status");
        sender.sendMessage("§e➜ §7/urbanismeadmin info <ville>");
        sender.sendMessage("§e➜ §7/urbanismeadmin set <ville> <critere> <valeur>");
        sender.sendMessage("§e➜ §7/urbanismeadmin resetville <ville>");
        sender.sendMessage("§e➜ §7/urbanismeadmin deleteville <ville>");
        sender.sendMessage("§e➜ §7/urbanismeadmin finish <ville>");
        sender.sendMessage("§e➜ §7/urbanismeadmin unfinish <ville>");
        sender.sendMessage("§e➜ §7/urbanismeadmin lock <ville>");
        sender.sendMessage("§e➜ §7/urbanismeadmin unlock <ville>");
        sender.sendMessage("§e➜ §7/urbanismeadmin resetall");
        sender.sendMessage("§e➜ §7/urbanismeadmin reload");
        footer(sender);
    }

    private void usage(CommandSender sender, String usage) {
        header(sender, "Admin Urbanisme");
        sender.sendMessage("§c✖ §fCommande incomplète.");
        sender.sendMessage("§e➜ §7Utilisation : §e" + usage);
        footer(sender);
    }

    private void success(CommandSender sender, String message) {
        header(sender, "Admin Urbanisme");
        sender.sendMessage("§a✔ §f" + message);
        footer(sender);
    }

    private void error(CommandSender sender, String message) {
        header(sender, "Admin Urbanisme");
        sender.sendMessage("§c✖ §f" + message);
        footer(sender);
    }

    private void header(CommandSender sender, String title) {
        sender.sendMessage("");
        sender.sendMessage("§8----- §6✦ " + title + " ✦ §8-----");
    }

    private void footer(CommandSender sender) {
        sender.sendMessage("§8-----------------------------");
    }
}
