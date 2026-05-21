package fr.moodcraft.townmenu.listener;

import fr.moodcraft.townmenu.manager.TownInputManager;

import fr.moodcraft.townmenu.util.MessageUtil;
import fr.moodcraft.townmenu.util.SoundUtil;

import org.bukkit.Bukkit;

import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.AsyncPlayerChatEvent;

public class TownInputListener
        implements Listener {

    private static final double MAX_TAX =
            100000.0;

    @EventHandler
    public void chat(
            AsyncPlayerChatEvent e
    ) {

        Player p =
                e.getPlayer();

        if (!TownInputManager.has(p)) {
            return;
        }

        e.setCancelled(true);

        String message =
                e.getMessage()
                        .trim();

        TownInputManager.Type type =
                TownInputManager.get(p);

        if (message.equalsIgnoreCase("annuler")
                || message.equalsIgnoreCase("cancel")) {

            TownInputManager.clear(p);

            Bukkit.getScheduler()
                    .runTask(
                            Bukkit.getPluginManager()
                                    .getPlugin(
                                            "MoodTownSuite"
                                    ),

                            () -> {

                                SoundUtil.fail(p);

                                MessageUtil.send(
                                        p,
                                        "Saisie annulée",
                                        "§7Aucune modification n'a été appliquée."
                                );
                            }
                    );

            return;
        }

        TownInputManager.clear(p);

        Bukkit.getScheduler()
                .runTask(
                        Bukkit.getPluginManager()
                                .getPlugin(
                                        "MoodTownSuite"
                                ),

                        () -> execute(
                                p,
                                type,
                                message
                        )
                );
    }

    private void execute(
            Player p,
            TownInputManager.Type type,
            String value
    ) {

        switch (type) {

            //
            // 👥 INVITE
            //

            case TOWN_INVITE -> {

                p.performCommand(
                        "t invite " + value
                );

                SoundUtil.success(p);
            }

            //
            // ❌ KICK
            //

            case TOWN_KICK -> {

                p.performCommand(
                        "t kick " + value
                );

                SoundUtil.success(p);
            }

            //
            // 👑 ADD ASSISTANT
            //

            case TOWN_ASSISTANT_ADD -> {

                p.performCommand(
                        "t rank add "
                                + value
                                + " assistant"
                );

                SoundUtil.success(p);
            }

            //
            // ❌ REMOVE ASSISTANT
            //

            case TOWN_ASSISTANT_REMOVE -> {

                p.performCommand(
                        "t rank remove "
                                + value
                                + " assistant"
                );

                SoundUtil.success(p);
            }

            //
            // 🏷 RENAME
            //

            case TOWN_RENAME -> {

                p.performCommand(
                        "t set name " + value
                );

                SoundUtil.success(p);
            }

            //
            // 💰 DEPOSIT
            //

            case TOWN_DEPOSIT -> {

                if (!isNumber(value)) {

                    SoundUtil.fail(p);

                    MessageUtil.error(
                            p,
                            "Montant invalide."
                    );

                    return;
                }

                p.performCommand(
                        "t deposit " + normalizeNumber(value)
                );

                SoundUtil.success(p);
            }

            //
            // 💸 WITHDRAW
            //

            case TOWN_WITHDRAW -> {

                if (!isNumber(value)) {

                    SoundUtil.fail(p);

                    MessageUtil.error(
                            p,
                            "Montant invalide."
                    );

                    return;
                }

                p.performCommand(
                        "t withdraw " + normalizeNumber(value)
                );

                SoundUtil.success(p);
            }

            //
            // 🏡 AJOUTER AMI PARCELLE
            //

            case PLOT_TRUST -> {

                p.performCommand(
                        "plot trust add " + value
                );

                SoundUtil.success(p);

                MessageUtil.send(
                        p,
                        "Parcelle",
                        "§a✔ Ami ajouté à la parcelle.\n" +
                                "§7Joueur : §e" + value
                );
            }

            //
            // ❌ RETIRER AMI PARCELLE
            //

            case PLOT_UNTRUST -> {

                p.performCommand(
                        "plot trust remove " + value
                );

                SoundUtil.success(p);

                MessageUtil.send(
                        p,
                        "Parcelle",
                        "§a✔ Ami retiré de la parcelle.\n" +
                                "§7Joueur : §e" + value
                );
            }

            //
            // 💰 FOR SALE
            //

            case PLOT_FOR_SALE -> {

                if (!isNumber(value)) {

                    SoundUtil.fail(p);

                    MessageUtil.error(
                            p,
                            "Prix invalide."
                    );

                    return;
                }

                p.performCommand(
                        "plot fs " + normalizeNumber(value)
                );

                SoundUtil.success(p);

                MessageUtil.send(
                        p,
                        "Parcelle",
                        "§a✔ Parcelle mise en vente.\n" +
                                "§7Prix : §e" + normalizeNumber(value) + "€"
                );
            }

            //
            // 🏛 TAXE VILLE
            //

            case TAX_TOWN -> {

                if (!isValidTax(p, value)) {
                    return;
                }

                p.performCommand(
                        "t set taxes " + normalizeNumber(value)
                );

                SoundUtil.success(p);

                MessageUtil.send(
                        p,
                        "Fiscalité urbaine",
                        "§a✔ Taxe de ville mise à jour.\n" +
                                "§7Nouveau montant: §e" + normalizeNumber(value) + "€"
                );
            }

            //
            // 🏘 TAXE PARCELLE
            //

            case TAX_PLOT -> {

                if (!isValidTax(p, value)) {
                    return;
                }

                p.performCommand(
                        "t set plottax " + normalizeNumber(value)
                );

                SoundUtil.success(p);

                MessageUtil.send(
                        p,
                        "Fiscalité urbaine",
                        "§a✔ Taxe de parcelle mise à jour.\n" +
                                "§7Nouveau montant: §e" + normalizeNumber(value) + "€"
                );
            }

            //
            // 🧰 TAXE COMMERCE
            //

            case TAX_SHOP -> {

                if (!isValidTax(p, value)) {
                    return;
                }

                p.performCommand(
                        "t set shoptax " + normalizeNumber(value)
                );

                SoundUtil.success(p);

                MessageUtil.send(
                        p,
                        "Fiscalité urbaine",
                        "§a✔ Taxe commerce mise à jour.\n" +
                                "§7Nouveau montant: §e" + normalizeNumber(value) + "€"
                );
            }

            //
            // 🏳 TAXE AMBASSADE
            //

            case TAX_EMBASSY -> {

                if (!isValidTax(p, value)) {
                    return;
                }

                p.performCommand(
                        "t set embassytax " + normalizeNumber(value)
                );

                SoundUtil.success(p);

                MessageUtil.send(
                        p,
                        "Fiscalité urbaine",
                        "§a✔ Taxe ambassade mise à jour.\n" +
                                "§7Nouveau montant: §e" + normalizeNumber(value) + "€"
                );
            }
        }
    }

    private boolean isValidTax(
            Player p,
            String value
    ) {

        if (!isNumber(value)) {

            SoundUtil.fail(p);

            MessageUtil.send(
                    p,
                    "Fiscalité urbaine",
                    "§cMontant invalide.\n" +
                            "§7Écrivez un nombre simple.\n" +
                            "§8Exemple: §e250"
            );

            return false;
        }

        double amount =
                Double.parseDouble(
                        normalizeNumber(value)
                );

        if (amount < 0) {

            SoundUtil.fail(p);

            MessageUtil.send(
                    p,
                    "Fiscalité urbaine",
                    "§cLa taxe ne peut pas être négative.\n" +
                            "§7Entrez un montant supérieur ou égal à §e0§7."
            );

            return false;
        }

        if (amount > MAX_TAX) {

            SoundUtil.fail(p);

            MessageUtil.send(
                    p,
                    "Fiscalité urbaine",
                    "§cMontant trop élevé.\n" +
                            "§7Limite actuelle: §e" + MAX_TAX + "€§7."
            );

            return false;
        }

        return true;
    }

    private boolean isNumber(
            String s
    ) {

        try {

            Double.parseDouble(
                    normalizeNumber(s)
            );

            return true;

        } catch (Exception ignored) {

            return false;
        }
    }

    private String normalizeNumber(
            String s
    ) {

        return s.trim()
                .replace(",", ".");
    }
}