package fr.moodcraft.tgrade;

import fr.moodcraft.flag.command.DrapeauCommand;
import fr.moodcraft.flag.listener.FlagGUIListener;
import fr.moodcraft.flag.storage.FlagStorage;

import fr.moodcraft.pubville.command.PubVilleCommand;
import fr.moodcraft.pubville.listener.PubVilleListener;
import fr.moodcraft.pubville.manager.PubVilleManager;

import fr.moodcraft.tgrade.command.UrbanismeAdminCommand;
import fr.moodcraft.tgrade.command.UrbanismeCommand;
import fr.moodcraft.tgrade.command.VProjetsResetCommand;

import fr.moodcraft.tgrade.listener.CitizenTownListListener;
import fr.moodcraft.tgrade.listener.CitizenVoteListener;
import fr.moodcraft.tgrade.listener.ClassementListener;
import fr.moodcraft.tgrade.listener.EvaluationManagerListener;
import fr.moodcraft.tgrade.listener.GUIListener;
import fr.moodcraft.tgrade.listener.MayorTownListListener;
import fr.moodcraft.tgrade.listener.MayorVoteListener;
import fr.moodcraft.tgrade.listener.PendingProjectsListener;
import fr.moodcraft.tgrade.listener.ProjectDepositChatListener;
import fr.moodcraft.tgrade.listener.ProjectReviewListener;
import fr.moodcraft.tgrade.listener.RateGUIListener;
import fr.moodcraft.tgrade.listener.ReviewGUIListener;
import fr.moodcraft.tgrade.listener.UrbanismeAdminListener;
import fr.moodcraft.tgrade.listener.UrbanismeMainListener;

import fr.moodcraft.tgrade.manager.GradeManager;

import fr.moodcraft.tgrade.storage.GradeStorage;
import fr.moodcraft.tgrade.storage.SubmissionStorage;
import fr.moodcraft.tgrade.storage.VoteStorage;

import fr.moodcraft.tgrade.task.WeeklyResetTask;

import fr.moodcraft.townmenu.command.TownMenuCommand;
import fr.moodcraft.townmenu.listener.TownInputListener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    public static Main get() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        if (Bukkit.getPluginManager().getPlugin("Towny") == null) {
            getLogger().severe("");
            getLogger().severe("----- MoodTownSuite -----");
            getLogger().severe("Towny introuvable.");
            getLogger().severe("Plugin désactivé.");
            getLogger().severe("-------------------------");
            getLogger().severe("");

            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // =========================
        // 🏛 MoodTownGrade
        // =========================
        SubmissionStorage.init();
        GradeStorage.init();
        VoteStorage.init();
        GradeManager.loadAll();

        UrbanismeCommand urbanismeCommand = new UrbanismeCommand();
        registerCommand("urbanisme", urbanismeCommand);
        registerCommand("topville", urbanismeCommand);
        registerCommand("vprojetsreset", new VProjetsResetCommand());
        registerCommand("urbanismeadmin", new UrbanismeAdminCommand());

        registerEvents(
                new GUIListener(),
                new RateGUIListener(),
                new ReviewGUIListener(),
                new UrbanismeMainListener(),
                new UrbanismeAdminListener(),
                new EvaluationManagerListener(),
                new PendingProjectsListener(),
                new ProjectReviewListener(),
                new ClassementListener(),
                new CitizenVoteListener(),
                new CitizenTownListListener(),
                new MayorVoteListener(),
                new MayorTownListListener(),
                new ProjectDepositChatListener()
        );

        long week = 20L * 60L * 60L * 24L * 7L;
        Bukkit.getScheduler().runTaskTimer(this, new WeeklyResetTask(), week, week);

        // =========================
        // 🏙 MoodTownMenu
        // =========================
        registerCommand("menuville", new TownMenuCommand());
        registerEvents(
                new fr.moodcraft.townmenu.listener.GUIListener(),
                new TownInputListener()
        );

        // =========================
        // 📢 MoodPubVille
        // =========================
        PubVilleManager.init();
        registerCommand("pubville", new PubVilleCommand());
        registerEvents(new PubVilleListener());

        // =========================
        // 🚩 MoodTownFlag / Drapeaux
        // =========================
        FlagStorage.load();
        registerCommand("drapeau", new DrapeauCommand());
        registerEvents(new FlagGUIListener());

        getLogger().info("");
        getLogger().info("----- MoodTownSuite -----");
        getLogger().info("Commission urbaine chargée.");
        getLogger().info("Menu ville chargé.");
        getLogger().info("Publicités de ville chargées.");
        getLogger().info("Drapeaux ville/nation chargés.");
        getLogger().info("Towny détecté.");
        getLogger().info("Grades chargés: " + GradeManager.getAll().size());
        getLogger().info("Reset hebdomadaire actif.");
        getLogger().info("-------------------------");
        getLogger().info("");
    }

    @Override
    public void onDisable() {

        GradeManager.getAll().forEach(GradeManager::save);
        GradeManager.clearCache();
        FlagStorage.save();
        PubVilleManager.save();

        getLogger().info("");
        getLogger().info("----- MoodTownSuite -----");
        getLogger().info("Plugin arrêté.");
        getLogger().info("Sauvegarde terminée.");
        getLogger().info("-------------------------");
        getLogger().info("");
    }

    private void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    private void registerCommand(
            String name,
            org.bukkit.command.CommandExecutor executor
    ) {
        if (getCommand(name) != null) {
            getCommand(name).setExecutor(executor);
        }
    }
}
