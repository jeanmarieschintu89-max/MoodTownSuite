package fr.moodcraft.flag.util;

import org.bukkit.command.CommandSender;

public final class FlagMessages {

    private FlagMessages() {}

    //
    // 🟢 BRAND
    //

    public static String brand() {

        return "§aMood§6Craft";
    }

    //
    // 🎨 HEADER
    //

    public static void header(
            CommandSender sender,
            String title
    ) {

        sender.sendMessage("");
        sender.sendMessage(
                "§8----- §6✦ "
                        + clean(title)
                        + " ✦ §8-----"
        );
        sender.sendMessage("");
    }

    //
    // 🎨 FOOTER
    //

    public static void footer(
            CommandSender sender
    ) {

        sender.sendMessage("");
        sender.sendMessage(
                "§8-----------------------------"
        );
        sender.sendMessage("");
    }

    //
    // ➜ INFO LINE
    //

    public static String infoLine(String message) {
        return "§e➜ §f" + cleanPrefix(message);
    }

    //
    // ✅ SUCCESS LINE
    //

    public static String successLine(String message) {
        return "§a✔ §f" + cleanPrefix(message);
    }

    //
    // ❌ ERROR LINE
    //

    public static String errorLine(String message) {
        return "§c✖ §f" + cleanPrefix(message);
    }

    //
    // • DETAIL LINE
    //

    public static String detailLine(String message) {
        return "§8• §7" + cleanPrefix(message);
    }

    //
    // ✅ SUCCESS
    //

    public static void success(
            CommandSender sender,
            String title,
            String message
    ) {

        header(
                sender,
                title
        );

        sender.sendMessage(successLine(message));

        footer(sender);
    }

    //
    // ❌ ERROR
    //

    public static void error(
            CommandSender sender,
            String title,
            String message
    ) {

        header(
                sender,
                title
        );

        sender.sendMessage(errorLine("Action refusée."));

        sender.sendMessage("");

        sender.sendMessage(detailLine(message));

        footer(sender);
    }

    //
    // ℹ INFO
    //

    public static void info(
            CommandSender sender,
            String title,
            String message
    ) {

        header(
                sender,
                title
        );

        sender.sendMessage(infoLine(message));

        footer(sender);
    }

    //
    // • LINE
    //

    public static void line(
            CommandSender sender,
            String message
    ) {

        sender.sendMessage(detailLine(message));
    }

    //
    // 🏳️ DRAPEAU ENREGISTRÉ
    //

    public static void flagSaved(
            CommandSender sender,
            String townName
    ) {

        header(
                sender,
                "Registre des Drapeaux"
        );

        sender.sendMessage(successLine("Drapeau enregistré."));

        sender.sendMessage("");

        sender.sendMessage(infoLine("Ville : §b" + townName));

        sender.sendMessage("");

        line(
                sender,
                "Identité visuelle mise à jour"
        );

        line(
                sender,
                "Visible dans les menus"
        );

        footer(sender);
    }

    //
    // 🛡️ BOUCLIER DONNÉ
    //

    public static void shieldGiven(
            CommandSender sender,
            String townName
    ) {

        header(
                sender,
                "Armurerie des Blasons"
        );

        sender.sendMessage(successLine("Bouclier distribué."));

        sender.sendMessage("");

        sender.sendMessage(infoLine("Ville : §b" + townName));

        sender.sendMessage("");

        line(
                sender,
                "Blason appliqué"
        );

        line(
                sender,
                "Équipement remis au maire"
        );

        footer(sender);
    }

    //
    // 🧼 CLEAN TITLE
    //

    private static String clean(
            String title
    ) {

        if (title == null || title.isBlank()) {
            return "Registre des Drapeaux";
        }

        return title
                .replace("§6", "")
                .replace("§f", "")
                .replace("§a", "")
                .replace("§b", "")
                .replace("§c", "")
                .replace("§7", "")
                .replace("§8", "")
                .replace("✦", "")
                .trim();
    }

    private static String cleanPrefix(
            String text
    ) {

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