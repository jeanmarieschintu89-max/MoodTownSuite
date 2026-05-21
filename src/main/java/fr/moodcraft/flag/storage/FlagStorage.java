package fr.moodcraft.flag.storage;

import fr.moodcraft.flag.manager.FlagManager;
import fr.moodcraft.flag.model.TownFlag;
import fr.moodcraft.tgrade.Main;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class FlagStorage {

    private static File file;
    private static YamlConfiguration config;

    public static void load() {

        file = new File(
                Main.get().getDataFolder(),
                "flags.yml"
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
                YamlConfiguration.loadConfiguration(
                        file
                );

        if (config.contains("towns")) {

            for (String town :
                    config.getConfigurationSection(
                            "towns"
                    ).getKeys(false)) {

                ItemStack banner =
                        config.getItemStack(
                                "towns."
                                        + town
                                        + ".banner"
                        );

                if (banner == null)
                    continue;

                FlagManager.setTown(
                        town,
                        new TownFlag(
                                town,
                                "town",
                                banner
                        )
                );
            }
        }

        if (config.contains("nations")) {

            for (String nation :
                    config.getConfigurationSection(
                            "nations"
                    ).getKeys(false)) {

                ItemStack banner =
                        config.getItemStack(
                                "nations."
                                        + nation
                                        + ".banner"
                        );

                if (banner == null)
                    continue;

                FlagManager.setNation(
                        nation,
                        new TownFlag(
                                nation,
                                "nation",
                                banner
                        )
                );
            }
        }
    }

    public static void save() {

        if (config == null || file == null) return;

        for (String town :
                FlagManager.allTowns()
                        .keySet()) {

            TownFlag flag =
                    FlagManager.getTown(
                            town
                    );

            if (flag == null)
                continue;

            config.set(
                    "towns."
                            + town
                            + ".banner",

                    flag.getBanner()
            );
        }

        for (String nation :
                FlagManager.allNations()
                        .keySet()) {

            TownFlag flag =
                    FlagManager.getNation(
                            nation
                    );

            if (flag == null)
                continue;

            config.set(
                    "nations."
                            + nation
                            + ".banner",

                    flag.getBanner()
            );
        }

        try {

            config.save(file);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
