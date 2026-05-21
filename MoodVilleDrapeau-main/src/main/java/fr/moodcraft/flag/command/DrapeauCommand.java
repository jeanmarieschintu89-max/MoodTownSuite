package fr.moodcraft.flag.command;

import com.palmergames.bukkit.towny.TownyAPI;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

import fr.moodcraft.flag.gui.FlagMainGUI;
import fr.moodcraft.flag.util.FlagMessages;

import org.bukkit.Bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;

public class DrapeauCommand
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
                    "Commande joueur uniquement."
            );

            return true;
        }

        //
        // 🎌 /drapeau
        //

        if (args.length == 0) {

            FlagMainGUI.open(p);

            return true;
        }

        //
        // 🌍 /drapeau nation
        //

        if (args[0]
                .equalsIgnoreCase(
                        "nation"
                )) {

            Resident resident =
                    TownyAPI.getInstance()
                            .getResident(
                                    p.getUniqueId()
                            );

            if (resident == null
                    || !resident.hasNation()) {

                FlagMessages.error(
                        p,
                        "Registre des Drapeaux",
                        "Vous devez appartenir à une nation."
                );

                return true;
            }

            Nation nation =
                    resident.getNationOrNull();

            if (nation == null) {

                FlagMessages.error(
                        p,
                        "Registre des Drapeaux",
                        "Impossible de charger votre nation."
                );

                return true;
            }

            //
            // 🌍 /drapeau nation
            //

            if (args.length == 1) {

                FlagMainGUI.openNation(
                        p,
                        nation
                );

                return true;
            }

            //
            // 🌍 /drapeau nation voir <joueur>
            //

            if (args.length >= 3
                    && args[1]
                    .equalsIgnoreCase(
                            "voir"
                    )) {

                if (!p.hasPermission(
                        "moodtownflag.admin"
                )) {

                    FlagMessages.error(
                            p,
                            "Inspection des Drapeaux",
                            "Accès réservé à l'administration."
                    );

                    return true;
                }

                Player target =
                        Bukkit.getPlayerExact(
                                args[2]
                        );

                if (target == null) {

                    FlagMessages.error(
                            p,
                            "Inspection des Drapeaux",
                            "Joueur introuvable ou hors ligne."
                    );

                    return true;
                }

                Resident targetResident =
                        TownyAPI.getInstance()
                                .getResident(
                                        target.getUniqueId()
                                );

                if (targetResident == null
                        || !targetResident.hasNation()) {

                    FlagMessages.error(
                            p,
                            "Inspection des Drapeaux",
                            "Ce joueur n'appartient à aucune nation."
                    );

                    return true;
                }

                Nation targetNation =
                        targetResident.getNationOrNull();

                if (targetNation == null) {

                    FlagMessages.error(
                            p,
                            "Inspection des Drapeaux",
                            "Impossible de charger cette nation."
                    );

                    return true;
                }

                FlagMessages.header(
                        p,
                        "Inspection des Drapeaux"
                );

                p.sendMessage(
                        "§a✔ §fAccès administratif autorisé."
                );

                p.sendMessage("");

                p.sendMessage(
                        "§7Nation: §e"
                                + targetNation.getName()
                );

                p.sendMessage("");

                FlagMessages.line(
                        p,
                        "Ouverture du registre national"
                );

                FlagMessages.line(
                        p,
                        "Consultation du drapeau officiel"
                );

                FlagMessages.footer(p);

                FlagMainGUI.openNation(
                        p,
                        targetNation
                );

                return true;
            }
        }

        //
        // 🏛 /drapeau voir <joueur>
        //

        if (args.length >= 2
                && args[0]
                .equalsIgnoreCase(
                        "voir"
                )) {

            if (!p.hasPermission(
                    "moodtownflag.admin"
            )) {

                FlagMessages.error(
                        p,
                        "Inspection des Drapeaux",
                        "Accès réservé à l'administration."
                );

                return true;
            }

            Player target =
                    Bukkit.getPlayerExact(
                            args[1]
                    );

            if (target == null) {

                FlagMessages.error(
                        p,
                        "Inspection des Drapeaux",
                        "Joueur introuvable ou hors ligne."
                );

                return true;
            }

            Resident targetResident =
                    TownyAPI.getInstance()
                            .getResident(
                                    target.getUniqueId()
                            );

            if (targetResident == null
                    || !targetResident.hasTown()) {

                FlagMessages.error(
                        p,
                        "Inspection des Drapeaux",
                        "Ce joueur n'appartient à aucune ville."
                );

                return true;
            }

            Town targetTown =
                    targetResident.getTownOrNull();

            if (targetTown == null) {

                FlagMessages.error(
                        p,
                        "Inspection des Drapeaux",
                        "Impossible de charger cette ville."
                );

                return true;
            }

            FlagMessages.header(
                    p,
                    "Inspection des Drapeaux"
            );

            p.sendMessage(
                    "§a✔ §fAccès administratif autorisé."
            );

            p.sendMessage("");

            p.sendMessage(
                    "§7Ville: §b"
                            + targetTown.getName()
            );

            p.sendMessage("");

            FlagMessages.line(
                    p,
                    "Ouverture du registre municipal"
            );

            FlagMessages.line(
                    p,
                    "Consultation du drapeau officiel"
            );

            FlagMessages.footer(p);

            FlagMainGUI.openTown(
                    p,
                    targetTown
            );

            return true;
        }

        //
        // 📜 HELP
        //

        FlagMessages.header(
                p,
                "Registre des Drapeaux"
        );

        p.sendMessage(
                "§fCommandes disponibles."
        );

        p.sendMessage("");

        p.sendMessage(
                "§8• §e/drapeau §7ouvrir votre drapeau de ville"
        );

        p.sendMessage(
                "§8• §e/drapeau nation §7ouvrir votre drapeau national"
        );

        if (p.hasPermission(
                "moodtownflag.admin"
        )) {

            p.sendMessage("");

            p.sendMessage(
                    "§8• §e/drapeau voir <joueur>"
            );

            p.sendMessage(
                    "§8• §e/drapeau nation voir <joueur>"
            );
        }

        FlagMessages.footer(p);

        return true;
    }
}