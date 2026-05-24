package fr.moodcraft.pubville.storage;

import fr.moodcraft.pubville.model.PubVilleCampaign;
import fr.moodcraft.tgrade.Main;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class PubVilleStorage {

    private static File file;
    private static FileConfiguration config;

    private PubVilleStorage() {}

    public static void init() {
        file = new File(Main.get().getDataFolder(), "publicites.yml");

        if (!file.exists()) {
            try {
                Main.get().getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public static Map<String, PubVilleCampaign> loadCampaigns() {
        Map<String, PubVilleCampaign> campaigns = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("campagnes");

        if (section == null) {
            return campaigns;
        }

        for (String key : section.getKeys(false)) {
            String path = "campagnes." + key + ".";
            String town = config.getString(path + "ville", key);
            String buyer = config.getString(path + "acheteur", "Inconnu");
            String level = config.getString(path + "niveau", "petite");
            long expiresAt = config.getLong(path + "expire", 0L);
            double price = config.getDouble(path + "prix", 0D);

            campaigns.put(town.toLowerCase(), new PubVilleCampaign(town, buyer, level, expiresAt, price));
        }

        return campaigns;
    }

    public static Map<String, Location> loadSpawns() {
        Map<String, Location> spawns = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("spawns");

        if (section == null) {
            return spawns;
        }

        for (String key : section.getKeys(false)) {
            String path = "spawns." + key + ".";
            World world = Bukkit.getWorld(config.getString(path + "world", "world"));

            if (world == null) {
                continue;
            }

            Location location = new Location(
                    world,
                    config.getDouble(path + "x"),
                    config.getDouble(path + "y"),
                    config.getDouble(path + "z"),
                    (float) config.getDouble(path + "yaw"),
                    (float) config.getDouble(path + "pitch")
            );

            spawns.put(key.toLowerCase(), location);
        }

        return spawns;
    }

    public static void saveCampaigns(Map<String, PubVilleCampaign> campaigns) {
        config.set("campagnes", null);

        for (PubVilleCampaign campaign : campaigns.values()) {
            String key = campaign.getTownName();
            String path = "campagnes." + key + ".";

            config.set(path + "ville", campaign.getTownName());
            config.set(path + "acheteur", campaign.getBuyerName());
            config.set(path + "niveau", campaign.getLevel());
            config.set(path + "expire", campaign.getExpiresAt());
            config.set(path + "prix", campaign.getPrice());
        }

        save();
    }

    public static void saveSpawns(Map<String, Location> spawns) {
        config.set("spawns", null);

        for (Map.Entry<String, Location> entry : spawns.entrySet()) {
            Location location = entry.getValue();

            if (location == null || location.getWorld() == null) {
                continue;
            }

            String path = "spawns." + entry.getKey() + ".";
            config.set(path + "world", location.getWorld().getName());
            config.set(path + "x", location.getX());
            config.set(path + "y", location.getY());
            config.set(path + "z", location.getZ());
            config.set(path + "yaw", location.getYaw());
            config.set(path + "pitch", location.getPitch());
        }

        save();
    }

    private static void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
