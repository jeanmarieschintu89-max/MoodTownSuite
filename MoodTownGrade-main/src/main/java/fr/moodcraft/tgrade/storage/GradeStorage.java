package fr.moodcraft.tgrade.storage;

import fr.moodcraft.tgrade.Main;

import fr.moodcraft.tgrade.model.TownGrade;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class GradeStorage {

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
                "grades.yml"
        );

        //
        // 📂 CREATE
        //

        if (!file.exists()) {

            try {

                file.getParentFile()
                        .mkdirs();

                file.createNewFile();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        //
        // 📄 LOAD CONFIG
        //

        config =
                YamlConfiguration
                        .loadConfiguration(file);
    }

    //
    // 💾 SAVE
    //

    public static void save(TownGrade grade) {

        String path =
                "grades."
                        + grade.getTown();

        //
        // 🏛️ NOTES
        //

        config.set(
                path + ".architecture",
                grade.getArchitecture()
        );

        config.set(
                path + ".style",
                grade.getStyle()
        );

        config.set(
                path + ".activite",
                grade.getActivite()
        );

        config.set(
                path + ".banque",
                grade.getBanque()
        );

        config.set(
                path + ".remarquable",
                grade.getRemarquable()
        );

        config.set(
                path + ".rp",
                grade.getRp()
        );

        config.set(
                path + ".taille",
                grade.getTaille()
        );

        config.set(
                path + ".votes",
                grade.getVotes()
        );

        //
        // ✅ FINISHED
        //

        config.set(
                path + ".finished",
                grade.isFinished()
        );

        //
        // 💰 STATS
        //

        config.set(
                path + ".total",
                grade.getTotal()
        );

        config.set(
                path + ".payout",
                grade.getPayout()
        );

        //
        // 💾 SAVE FILE
        //

        saveFile();
    }

    //
    // 📥 LOAD
    //

    public static TownGrade load(String town) {

        String path =
                "grades."
                        + town;

        //
        // 🏙️ CREATE
        //

        TownGrade grade =
                new TownGrade(town);

        //
        // 📊 LOAD NOTES
        //

        grade.setArchitecture(
                config.getInt(
                        path + ".architecture"
                )
        );

        grade.setStyle(
                config.getInt(
                        path + ".style"
                )
        );

        grade.setActivite(
                config.getInt(
                        path + ".activite"
                )
        );

        grade.setBanque(
                config.getInt(
                        path + ".banque"
                )
        );

        grade.setRemarquable(
                config.getInt(
                        path + ".remarquable"
                )
        );

        grade.setRp(
                config.getInt(
                        path + ".rp"
                )
        );

        grade.setTaille(
                config.getInt(
                        path + ".taille"
                )
        );

        grade.setVotes(
                config.getInt(
                        path + ".votes"
                )
        );

        //
        // ✅ FINISHED
        //

        grade.setFinished(
                config.getBoolean(
                        path + ".finished"
                )
        );

        return grade;
    }

    //
    // 📚 GET ALL TOWNS
    //

    public static List<String>
    getAllTowns() {

        List<String> towns =
                new ArrayList<>();

        //
        // 📂 SECTION
        //

        ConfigurationSection section =
                config.getConfigurationSection(
                        "grades"
                );

        if (section == null) {
            return towns;
        }

        //
        // 📚 KEYS
        //

        towns.addAll(
                section.getKeys(false)
        );

        return towns;
    }

    //
    // 🗑️ DELETE
    //

    public static void delete(String town) {

        config.set(
                "grades." + town,
                null
        );

        saveFile();
    }

    //
    // 🔄 RELOAD
    //

    public static void reload() {

        config =
                YamlConfiguration
                        .loadConfiguration(file);
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