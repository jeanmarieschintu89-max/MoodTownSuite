package fr.moodcraft.tgrade.command;

import fr.moodcraft.tgrade.gui.CitizenTownListGUI;
import fr.moodcraft.tgrade.gui.ClassementGUI;
import fr.moodcraft.tgrade.gui.PendingProjectsGUI;
import fr.moodcraft.tgrade.gui.RateGUI;
import fr.moodcraft.tgrade.gui.ReviewGUI;
import fr.moodcraft.tgrade.gui.UrbanismeMainGUI;

import fr.moodcraft.tgrade.manager.GradeManager;
import fr.moodcraft.tgrade.manager.PayoutManager;
import fr.moodcraft.tgrade.manager.ProjectDepositSessionManager;

import fr.moodcraft.tgrade.model.TownGrade;
import fr.moodcraft.tgrade.model.TownSubmission;

import fr.moodcraft.tgrade.storage.SubmissionStorage;
import fr.moodcraft.tgrade.storage.VoteStorage;

import fr.moodcraft.tgrade.task.WeeklyResetTask;

import fr.moodcraft.tgrade.towny.TownyHook;

import com.palmergames.bukkit.towny.object.Town;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;

public class UrbanismeCommand
        implements CommandExecutor {

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player p)) {

            sender.sendMessage(
                    "§cCommande joueur uniquement."
            );

            return true;
        }

        if (command.getName().equalsIgnoreCase("topville")) {

            ClassementGUI.open(p);
            return true;
        }

        if (command.getName().equalsIgnoreCase("vprojet")) {

            CitizenTownListGUI.open(p);
            return true;
        }

        if (command.getName().equalsIgnoreCase("projet")) {

            UrbanismeMainGUI.open(p);
            return true;
        }

        if (args.length == 0) {

            UrbanismeMainGUI.open(p);
            return true;
        }

        if (args[0].equalsIgnoreCase("classement")) {

            ClassementGUI.open(p);
            return true;
        }

        if (args[0].equalsIgnoreCase("review")
                || args[0].equalsIgnoreCase("noter")
                || args[0].equalsIgnoreCase("validation")
                || args[0].equalsIgnoreCase("payout")
                || args[0].equalsIgnoreCase("delete")
                || args[0].equalsIgnoreCase("resetweek")
                || args[0].equalsIgnoreCase("resetville")) {

            if (!p.hasPermission("moodtowngrade.staff")) {

                p.sendMessage("");
                p.sendMessage("§8----- §6Commission Urbaine §8-----");
                p.sendMessage("§cAccès refusé.");
                p.sendMessage("§7Registre réservé à l'administration urbaine.");
                p.sendMessage("");

                return true;
            }
        }

        if (args[0].equalsIgnoreCase("review")) {

            if (args.length < 2) {

                p.sendMessage("§c/urbanisme review <ville>");
                return true;
            }

            ReviewGUI.open(p, args[1]);
            return true;
        }

        if (args[0].equalsIgnoreCase("noter")) {

            if (args.length < 2) {

                p.sendMessage("§c/urbanisme noter <ville>");
                return true;
            }

            RateGUI.open(p, args[1]);
            return true;
        }

        if (args[0].equalsIgnoreCase("payout")) {

            PayoutManager.payoutAll();

            p.sendMessage("");
            p.sendMessage("§8----- §6Commission Urbaine §8-----");
            p.sendMessage("§aSubventions urbaines versées.");
            p.sendMessage("§7Les villes éligibles ont reçu");
            p.sendMessage("§7leur financement hebdomadaire.");
            p.sendMessage("");

            return true;
        }

        if (args[0].equalsIgnoreCase("delete")) {

            if (args.length < 2) {

                p.sendMessage("§c/urbanisme delete <id>");
                return true;
            }

            TownSubmission sub =
                    SubmissionStorage.get(args[1]);

            if (sub == null) {

                p.sendMessage("");
                p.sendMessage("§8----- §6Commission Urbaine §8-----");
                p.sendMessage("§cDossier introuvable.");
                p.sendMessage("§7Aucune demande ne correspond à cet identifiant.");
                p.sendMessage("");

                return true;
            }

            SubmissionStorage.delete(sub.getId());

            p.sendMessage("");
            p.sendMessage("§8----- §6Commission Urbaine §8-----");
            p.sendMessage("§cDemande supprimée.");
            p.sendMessage("§7Ville : §b" + sub.getTown());
            p.sendMessage("§7Projet : §f" + sub.getBuildName());
            p.sendMessage("§7ID : §f" + sub.getId());
            p.sendMessage("");

            return true;
        }

        if (args[0].equalsIgnoreCase("resetweek")) {

            new WeeklyResetTask().run();

            p.sendMessage("");
            p.sendMessage("§8----- §6Commission Urbaine §8-----");
            p.sendMessage("§aSemaine urbaine réinitialisée.");
            p.sendMessage("§7Les registres hebdomadaires");
            p.sendMessage("§7sont prêts pour une nouvelle session.");
            p.sendMessage("");

            return true;
        }

        if (args[0].equalsIgnoreCase("resetville")) {

            if (args.length < 2) {

                p.sendMessage("§c/urbanisme resetville <ville>");
                return true;
            }

            String townName =
                    args[1];

            TownGrade grade =
                    GradeManager.get(townName);

            grade.setArchitecture(0);
            grade.setStyle(0);
            grade.setActivite(0);
            grade.setBanque(0);
            grade.setRemarquable(0);
            grade.setRp(0);
            grade.setTaille(0);
            grade.setVotes(0);

            grade.setLocked(false);
            grade.setFinished(false);
            grade.setFinalScore(0);
            grade.setPayoutClaimed(false);

            VoteStorage.clearTown(townName);

            GradeManager.save(grade);

            for (TownSubmission sub : SubmissionStorage.getAll()) {

                if (sub.getTown().equalsIgnoreCase(townName)) {

                    SubmissionStorage.delete(sub.getId());
                }
            }

            p.sendMessage("");
            p.sendMessage("§8----- §6Commission Urbaine §8-----");
            p.sendMessage("§aVille réinitialisée.");
            p.sendMessage("§7Ville : §b" + townName);
            p.sendMessage("§7Notes, votes, projets et financements");
            p.sendMessage("§7ont été remis à zéro.");
            p.sendMessage("");

            return true;
        }

        if (!TownyHook.canManage(p)) {

            p.sendMessage("");
            p.sendMessage("§8----- §6Commission Urbaine §8-----");
            p.sendMessage("§cAccès refusé.");
            p.sendMessage("§7Seuls les maires et assistants");
            p.sendMessage("§7peuvent gérer les projets urbains.");
            p.sendMessage("");

            return true;
        }

        Town town =
                TownyHook.getTown(p);

        if (town == null) {

            p.sendMessage("");
            p.sendMessage("§8----- §6Commission Urbaine §8-----");
            p.sendMessage("§cAucune ville détectée.");
            p.sendMessage("§7Votre dossier municipal est introuvable.");
            p.sendMessage("");

            return true;
        }

        if (args[0].equalsIgnoreCase("projet")) {

            if (SubmissionStorage.getTown(town.getName()).size() >= 5) {

                p.sendMessage("");
                p.sendMessage("§8----- §6Commission Urbaine §8-----");
                p.sendMessage("§cLimite atteinte.");
                p.sendMessage("§7Une ville peut déposer au maximum");
                p.sendMessage("§e5 projets §7dans le registre.");
                p.sendMessage("");

                return true;
            }

            ProjectDepositSessionManager.start(
                    p,
                    town.getName()
            );

            return true;
        }

        if (args[0].equalsIgnoreCase("projets")) {

            PendingProjectsGUI.open(p);
            return true;
        }

        if (args[0].equalsIgnoreCase("retirer")) {

            if (args.length < 2) {

                p.sendMessage("§c/urbanisme retirer <id>");
                return true;
            }

            TownSubmission sub =
                    SubmissionStorage.get(args[1]);

            if (sub == null) {

                p.sendMessage("");
                p.sendMessage("§8----- §6Commission Urbaine §8-----");
                p.sendMessage("§cDossier introuvable.");
                p.sendMessage("§7Aucune demande ne correspond à cet identifiant.");
                p.sendMessage("");

                return true;
            }

            if (!sub.getTown().equalsIgnoreCase(town.getName())) {

                p.sendMessage("");
                p.sendMessage("§8----- §6Commission Urbaine §8-----");
                p.sendMessage("§cDemande invalide.");
                p.sendMessage("§7Ce projet n'appartient pas à votre ville.");
                p.sendMessage("");

                return true;
            }

            SubmissionStorage.delete(sub.getId());

            p.sendMessage("");
            p.sendMessage("§8----- §6Commission Urbaine §8-----");
            p.sendMessage("§cDemande retirée.");
            p.sendMessage("§7Ville : §b" + sub.getTown());
            p.sendMessage("§7Projet : §f" + sub.getBuildName());
            p.sendMessage("§7ID : §f" + sub.getId());
            p.sendMessage("");

            return true;
        }

        UrbanismeMainGUI.open(p);

        return true;
    }
}