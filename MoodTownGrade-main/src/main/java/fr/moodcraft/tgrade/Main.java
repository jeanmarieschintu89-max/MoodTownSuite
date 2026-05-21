package fr.moodcraft.tgrade;

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

import org.bukkit.Bukkit;

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
            getLogger().severe("----- MoodTownGrade -----");
            getLogger().severe("Towny introuvable.");
            getLogger().severe("Plugin désactivé.");
            getLogger().severe("-------------------------");
            getLogger().severe("");

            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        SubmissionStorage.init();
        GradeStorage.init();
        VoteStorage.init();

        GradeManager.loadAll();

        UrbanismeCommand urbanismeCommand = new UrbanismeCommand();

        registerCommand("urbanisme", urbanismeCommand);
        registerCommand("topville", urbanismeCommand);
        registerCommand("vprojetsreset", new VProjetsResetCommand());
        registerCommand("urbanismeadmin", new UrbanismeAdminCommand());

        getServer().getPluginManager().registerEvents(new GUIListener(), this);
        getServer().getPluginManager().registerEvents(new RateGUIListener(), this);
        getServer().getPluginManager().registerEvents(new ReviewGUIListener(), this);
        getServer().getPluginManager().registerEvents(new UrbanismeMainListener(), this);
        getServer().getPluginManager().registerEvents(new UrbanismeAdminListener(), this);
        getServer().getPluginManager().registerEvents(new EvaluationManagerListener(), this);
        getServer().getPluginManager().registerEvents(new PendingProjectsListener(), this);
        getServer().getPluginManager().registerEvents(new ProjectReviewListener(), this);
        getServer().getPluginManager().registerEvents(new ClassementListener(), this);
        getServer().getPluginManager().registerEvents(new CitizenVoteListener(), this);
        getServer().getPluginManager().registerEvents(new CitizenTownListListener(), this);
        getServer().getPluginManager().registerEvents(new MayorVoteListener(), this);
        getServer().getPluginManager().registerEvents(new MayorTownListListener(), this);
        getServer().getPluginManager().registerEvents(new ProjectDepositChatListener(), this);

        long week = 20L * 60L * 60L * 24L * 7L;

        Bukkit.getScheduler().runTaskTimer(this, new WeeklyResetTask(), week, week);

        getLogger().info("");
        getLogger().info("----- MoodTownGrade -----");
        getLogger().info("Commission urbaine chargée.");
        getLogger().info("Centre national opérationnel.");
        getLogger().info("Votes citoyens actifs.");
        getLogger().info("Conseil des maires actif.");
        getLogger().info("Classement national actif.");
        getLogger().info("Commande vprojetsreset active.");
        getLogger().info("Commande urbanismeadmin active.");
        getLogger().info("Système de dépôt immersif actif.");
        getLogger().info("Grades chargés: " + GradeManager.getAll().size());
        getLogger().info("Towny détecté.");
        getLogger().info("Reset hebdomadaire actif.");
        getLogger().info("-------------------------");
        getLogger().info("");
    }

    @Override
    public void onDisable() {

        GradeManager.getAll().forEach(GradeManager::save);
        GradeManager.clearCache();

        getLogger().info("");
        getLogger().info("----- MoodTownGrade -----");
        getLogger().info("Plugin arrêté.");
        getLogger().info("Sauvegarde terminée.");
        getLogger().info("-------------------------");
        getLogger().info("");
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
