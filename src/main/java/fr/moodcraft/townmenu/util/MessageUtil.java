package fr.moodcraft.townmenu.util;

import org.bukkit.entity.Player;

public class MessageUtil {

    public static void send(
            Player player,
            String module,
            String message
    ) {

        if (player == null) {
            return;
        }

        player.sendMessage("");
        player.sendMessage("§8----- §6✦ " + module + " ✦ §8-----");
        player.sendMessage("");

        if (message != null && !message.isBlank()) {
            String[] lines = message.split("\\n");

            for (String line : lines) {
                player.sendMessage(normalize(line));
            }
        }

        player.sendMessage("");
        player.sendMessage("§8-----------------------------");
    }

    public static void error(
            Player player,
            String message
    ) {

        send(
                player,
                "Menu Ville",
                "§c✖ §fAction impossible.\n\n§8• §7" + cleanPrefix(message)
        );
    }

    public static void success(
            Player player,
            String message
    ) {

        send(
                player,
                "Menu Ville",
                "§a✔ §fAction confirmée.\n\n§8• §7" + cleanPrefix(message)
        );
    }

    public static void info(
            Player player,
            String message
    ) {

        send(
                player,
                "Menu Ville",
                "§e➜ §f" + cleanPrefix(message)
        );
    }

    private static String normalize(String line) {

        if (line == null || line.isBlank()) {
            return "";
        }

        String trimmed = line.trim();

        if (trimmed.startsWith("§e➜")
                || trimmed.startsWith("§a✔")
                || trimmed.startsWith("§c✖")
                || trimmed.startsWith("§c✘")
                || trimmed.startsWith("§8•")
                || trimmed.startsWith("§8-----")
                || trimmed.startsWith("§8----------------")) {

            return trimmed.replace("§c✘", "§c✖");
        }

        if (trimmed.startsWith("§a")) {
            return "§a✔ §f" + cleanPrefix(trimmed);
        }

        if (trimmed.startsWith("§c")) {
            return "§c✖ §f" + cleanPrefix(trimmed);
        }

        if (trimmed.startsWith("§7")
                || trimmed.startsWith("§8")) {
            return "§8• §7" + cleanPrefix(trimmed);
        }

        return "§e➜ §f" + cleanPrefix(trimmed);
    }

    private static String cleanPrefix(String text) {

        if (text == null) {
            return "";
        }

        return text
                .replaceFirst("^§[0-9a-fk-or]", "")
                .replaceFirst("^➜\\s*", "")
                .replaceFirst("^✔\\s*", "")
                .replaceFirst("^✘\\s*", "")
                .replaceFirst("^✖\\s*", "")
                .replaceFirst("^•\\s*", "")
                .trim();
    }
}
