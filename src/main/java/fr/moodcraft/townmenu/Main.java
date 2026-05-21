package fr.moodcraft.townmenu;

import fr.moodcraft.townmenu.command.TownMenuCommand;

import fr.moodcraft.townmenu.listener.GUIListener;
import fr.moodcraft.townmenu.listener.TownInputListener;

import org.bukkit.Bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class Main
        extends JavaPlugin {

    private static Main instance;

    public static Main get() {

        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        if (Bukkit.getPluginManager()
                .getPlugin("Towny") == null) {

            getLogger().severe("");
            getLogger().severe("----- MoodTownMenu -----");
            getLogger().severe("Towny introuvable.");
            getLogger().severe("Plugin désactivé.");
            getLogger().severe("------------------------");
            getLogger().severe("");

            Bukkit.getPluginManager()
                    .disablePlugin(this);

            return;
        }

        if (Bukkit.getPluginManager()
                .getPlugin("MoodTownFlag") == null) {

            getLogger().severe("");
            getLogger().severe("----- MoodTownMenu -----");
            getLogger().severe("MoodTownDrapeau introuvable.");
            getLogger().severe("Le menu maire utilise l'API des drapeaux.");
            getLogger().severe("Plugin désactivé.");
            getLogger().severe("------------------------");
            getLogger().severe("");

            Bukkit.getPluginManager()
                    .disablePlugin(this);

            return;
        }

        if (getCommand("menuville") != null) {

            getCommand("menuville")
                    .setExecutor(
                            new TownMenuCommand()
                    );
        }

        getServer()
                .getPluginManager()
                .registerEvents(
                        new GUIListener(),
                        this
                );

        getServer()
                .getPluginManager()
                .registerEvents(
                        new TownInputListener(),
                        this
                );

        getLogger().info("");
        getLogger().info("----- MoodTownMenu -----");
        getLogger().info("Interface urbaine chargée.");
        getLogger().info("Towny détecté.");
        getLogger().info("MoodTownDrapeau détecté.");
        getLogger().info("Menus MoodCraft actifs.");
        getLogger().info("Saisie chat active.");
        getLogger().info("------------------------");
        getLogger().info("");
    }

    @Override
    public void onDisable() {

        getLogger().info("");
        getLogger().info("----- MoodTownMenu -----");
        getLogger().info("Plugin arrêté.");
        getLogger().info("------------------------");
        getLogger().info("");
    }
}