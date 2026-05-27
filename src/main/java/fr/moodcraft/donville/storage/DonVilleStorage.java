package fr.moodcraft.donville.storage;

import fr.moodcraft.donville.model.DonVilleAdminDonation;
import fr.moodcraft.donville.model.DonVilleDonation;
import fr.moodcraft.tgrade.Main;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class DonVilleStorage {

    private static File file;
    private static FileConfiguration config;

    private DonVilleStorage() {}

    public static void init() {
        file = new File(Main.get().getDataFolder(), "dons-villes.yml");

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

    public static Set<String> loadEnabledTowns() {
        Set<String> towns = new HashSet<>();
        ConfigurationSection section = config.getConfigurationSection("boites");

        if (section == null) {
            return towns;
        }

        for (String key : section.getKeys(false)) {
            if (config.getBoolean("boites." + key + ".active", false)) {
                towns.add(key.toLowerCase());
            }
        }

        return towns;
    }

    public static List<DonVilleDonation> loadHistory(String townName) {
        List<DonVilleDonation> history = new ArrayList<>();
        String path = "boites." + key(townName) + ".historique";
        ConfigurationSection section = config.getConfigurationSection(path);

        if (section == null) {
            return history;
        }

        for (String id : section.getKeys(false)) {
            String base = path + "." + id + ".";
            history.add(new DonVilleDonation(
                    config.getString(base + "donateur", "Inconnu"),
                    config.getDouble(base + "montant", 0D),
                    config.getLong(base + "date", 0L)
            ));
        }

        history.sort((a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt()));
        return history;
    }

    public static List<DonVilleAdminDonation> loadAdminHistory() {
        List<DonVilleAdminDonation> history = new ArrayList<>();
        ConfigurationSection section = config.getConfigurationSection("admin-logs");

        if (section == null) {
            return history;
        }

        for (String id : section.getKeys(false)) {
            String base = "admin-logs." + id + ".";
            history.add(new DonVilleAdminDonation(
                    config.getString(base + "ville", "Inconnue"),
                    config.getString(base + "donateur", "Inconnu"),
                    config.getDouble(base + "montant", 0D),
                    config.getLong(base + "date", 0L)
            ));
        }

        history.sort((a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt()));
        return history;
    }

    public static Map<String, Double> loadTotalsByTown() {
        Map<String, Double> totals = new HashMap<>();

        for (DonVilleAdminDonation donation : loadAdminHistory()) {
            String town = donation.getTownName();
            totals.put(town, totals.getOrDefault(town, 0D) + donation.getAmount());
        }

        return totals;
    }

    public static void setEnabled(String townName, boolean enabled) {
        config.set("boites." + key(townName) + ".active", enabled);
        config.set("boites." + key(townName) + ".nom", townName);
        save();
    }

    public static void addDonation(String townName, DonVilleDonation donation, int maxHistory) {
        List<DonVilleDonation> history = loadHistory(townName);
        history.add(0, donation);

        while (history.size() > maxHistory) {
            history.remove(history.size() - 1);
        }

        String basePath = "boites." + key(townName) + ".historique";
        config.set(basePath, null);

        int index = 0;
        for (DonVilleDonation entry : history) {
            String path = basePath + ".don" + index + ".";
            config.set(path + "donateur", entry.getDonorName());
            config.set(path + "montant", entry.getAmount());
            config.set(path + "date", entry.getCreatedAt());
            index++;
        }

        save();
    }

    public static void addAdminDonation(String townName, DonVilleDonation donation, int maxAdminLogs) {
        List<DonVilleAdminDonation> history = loadAdminHistory();
        history.add(0, new DonVilleAdminDonation(
                townName,
                donation.getDonorName(),
                donation.getAmount(),
                donation.getCreatedAt()
        ));

        while (history.size() > maxAdminLogs) {
            history.remove(history.size() - 1);
        }

        config.set("admin-logs", null);

        int index = 0;
        for (DonVilleAdminDonation entry : history) {
            String path = "admin-logs.don" + index + ".";
            config.set(path + "ville", entry.getTownName());
            config.set(path + "donateur", entry.getDonorName());
            config.set(path + "montant", entry.getAmount());
            config.set(path + "date", entry.getCreatedAt());
            index++;
        }

        save();
    }

    public static void resetTown(String townName) {
        config.set("boites." + key(townName) + ".historique", null);
        save();
    }

    public static void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String key(String townName) {
        return townName == null ? "" : townName.toLowerCase();
    }
}
