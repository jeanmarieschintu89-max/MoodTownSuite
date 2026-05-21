package fr.moodcraft.flag;

import fr.moodcraft.flag.command.DrapeauCommand;
import fr.moodcraft.flag.listener.FlagGUIListener;
import fr.moodcraft.flag.storage.FlagStorage;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        FlagStorage.load();

        getCommand("drapeau")
                .setExecutor(new DrapeauCommand());

        getServer()
                .getPluginManager()
                .registerEvents(
                        new FlagGUIListener(),
                        this
                );

        getLogger().info(
                "MoodTownFlag enabled."
        );
    }

    @Override
    public void onDisable() {

        FlagStorage.save();

        getLogger().info(
                "MoodTownFlag disabled."
        );
    }

    public static Main get() {
        return instance;
    }
}