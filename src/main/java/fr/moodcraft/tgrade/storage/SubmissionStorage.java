package fr.moodcraft.tgrade.storage;

import fr.moodcraft.tgrade.Main;
import fr.moodcraft.tgrade.model.SubmissionStatus;
import fr.moodcraft.tgrade.model.TownSubmission;

import org.bukkit.Bukkit;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SubmissionStorage {

    //
    // 📂 FILE
    //

    private static File file;

    //
    // 📄 CONFIG
    //

    private static YamlConfiguration config;

    //
    // 🚀 INIT
    //

    public static void init() {

        file = new File(
                Main.get().getDataFolder(),
                "submissions.yml"
        );

        if (!file.exists()) {

            try {

                file.getParentFile().mkdirs();

                file.createNewFile();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        config =
                YamlConfiguration.loadConfiguration(file);
    }

    //
    // 💾 SAVE SUBMISSION
    //

    public static void save(TownSubmission sub) {

        if (sub == null
                || sub.getId() == null) {

            return;
        }

        String path =
                "submissions." + sub.getId();

        config.set(path + ".town",
                sub.getTown());

        config.set(path + ".build",
                sub.getBuildName());

        config.set(path + ".world",
                sub.getWorld());

        config.set(path + ".x",
                sub.getX());

        config.set(path + ".y",
                sub.getY());

        config.set(path + ".z",
                sub.getZ());

        config.set(path + ".player",
                sub.getSubmittedBy().toString());

        config.set(path + ".timestamp",
                sub.getTimestamp());

        config.set(path + ".status",
                sub.getStatus().name());

        saveFile();
    }

    //
    // ❌ DELETE
    //

    public static void delete(String id) {

        if (id == null) {
            return;
        }

        config.set(
                "submissions." + id,
                null
        );

        saveFile();
    }

    //
    // 🧹 CLEANUP
    //

    public static void cleanup() {

        for (TownSubmission sub :
                getAll()) {

            if (sub.getStatus()
                    != SubmissionStatus.PENDING) {

                delete(sub.getId());
            }
        }
    }

    //
    // 🗑 CLEAR ALL
    //

    public static void clearAll() {

        config.set(
                "submissions",
                null
        );

        saveFile();
    }

    //
    // 📚 GET ALL
    //

    public static List<TownSubmission> getAll() {

        List<TownSubmission> list =
                new ArrayList<>();

        if (config == null
                || !config.contains("submissions")
                || config.getConfigurationSection("submissions") == null) {

            return list;
        }

        for (String id :
                config.getConfigurationSection("submissions")
                        .getKeys(false)) {

            String path =
                    "submissions." + id;

            try {

                String town =
                        config.getString(path + ".town");

                String build =
                        config.getString(path + ".build");

                String world =
                        config.getString(path + ".world");

                String player =
                        config.getString(path + ".player");

                String status =
                        config.getString(path + ".status");

                if (town == null
                        || build == null
                        || world == null
                        || player == null
                        || status == null) {

                    Bukkit.getConsoleSender()
                            .sendMessage(
                                    "§c[MoodTownGrade] Dossier urbain ignoré: " + id
                            );

                    continue;
                }

                TownSubmission sub =
                        new TownSubmission(

                                id,

                                town,

                                build,

                                world,

                                config.getInt(path + ".x"),

                                config.getInt(path + ".y"),

                                config.getInt(path + ".z"),

                                UUID.fromString(player),

                                config.getLong(path + ".timestamp"),

                                SubmissionStatus.valueOf(status)
                        );

                list.add(sub);

            } catch (Exception ex) {

                Bukkit.getConsoleSender()
                        .sendMessage(
                                "§c[MoodTownGrade] Dossier urbain corrompu ignoré: " + id
                        );
            }
        }

        return list;
    }

    //
    // 🏙 GET TOWN SUBMISSIONS
    //

    public static List<TownSubmission> getTown(String town) {

        List<TownSubmission> list =
                new ArrayList<>();

        if (town == null) {
            return list;
        }

        for (TownSubmission sub : getAll()) {

            if (sub.getTown()
                    .equalsIgnoreCase(town)) {

                list.add(sub);
            }
        }

        return list;
    }

    //
    // 🔍 GET BY ID
    //

    public static TownSubmission get(String id) {

        if (id == null) {
            return null;
        }

        for (TownSubmission sub : getAll()) {

            if (sub.getId()
                    .equalsIgnoreCase(id)) {

                return sub;
            }
        }

        return null;
    }

    //
    // 💾 SAVE FILE
    //

    private static void saveFile() {

        try {

            config.save(file);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}