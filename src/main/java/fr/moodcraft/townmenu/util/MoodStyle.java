package fr.moodcraft.townmenu.util;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class MoodStyle {

    private MoodStyle() {
    }

    public static final String BRAND = "§aMood§6Craft";
    public static final String FRAME = "§8----------- §6✦ §8-----------";

    public static String guiTitle(String title) {
        return "§6✦ §8§l" + cleanPrefix(title) + " §6✦";
    }

    public static String button(String name) {
        return "§6✦ §f" + name + " §6✦";
    }

    public static String dangerButton(String name) {
        return "§c✦ §f" + name + " §c✦";
    }

    public static String header(String module) {
        return "§8----- §6✦ " + cleanPrefix(module) + " ✦ §8-----";
    }

    public static String footer() {
        return FRAME;
    }

    public static String info(String text) {
        return "§e➜ §f" + cleanPrefix(text);
    }

    public static String success(String text) {
        return "§a✔ §f" + cleanPrefix(text);
    }

    public static String error(String text) {
        return "§c✖ §f" + cleanPrefix(text);
    }

    public static String detail(String text) {
        return "§8• §7" + cleanPrefix(text);
    }

    public static String bullet(String text) {
        return detail(text);
    }

    public static String value(String label, String value) {
        return "§8• §7" + cleanPrefix(label) + " : §e" + cleanPrefix(value);
    }

    public static List<String> lore(String... lines) {
        List<String> lore = new ArrayList<>();

        if (lines == null) {
            return lore;
        }

        for (String line : lines) {
            lore.add(normalizeLine(line));
        }

        return lore;
    }

    public static void send(
            Player player,
            String module,
            String... lines
    ) {

        if (player == null) {
            return;
        }

        player.sendMessage("");
        player.sendMessage(header(module));

        if (lines != null) {
            for (String line : lines) {
                player.sendMessage(normalizeLine(line));
            }
        }

        player.sendMessage(footer());
    }

    private static String normalizeLine(String line) {

        if (line == null || line.isBlank()) {
            return "";
        }

        String trimmed = line.trim().replace("✘", "✖");

        if (trimmed.startsWith("§e➜")
                || trimmed.startsWith("§a✔")
                || trimmed.startsWith("§c✖")
                || trimmed.startsWith("§8•")
                || trimmed.startsWith("§8-----")
                || trimmed.startsWith("§8-----------")) {
            return trimmed;
        }

        if (trimmed.startsWith("§a")) {
            return success(trimmed);
        }

        if (trimmed.startsWith("§c")) {
            return error(trimmed);
        }

        if (trimmed.startsWith("§7") || trimmed.startsWith("§8")) {
            return detail(trimmed);
        }

        return info(trimmed);
    }

    private static String cleanPrefix(String text) {

        if (text == null) {
            return "";
        }

        return text
                .replaceFirst("^§[0-9a-fk-or]", "")
                .replace("§l", "")
                .replaceFirst("^➜\\s*", "")
                .replaceFirst("^✔\\s*", "")
                .replaceFirst("^✘\\s*", "")
                .replaceFirst("^✖\\s*", "")
                .replaceFirst("^•\\s*", "")
                .trim();
    }
}
