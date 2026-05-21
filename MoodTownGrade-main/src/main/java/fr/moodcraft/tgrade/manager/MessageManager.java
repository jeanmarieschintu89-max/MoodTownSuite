package fr.moodcraft.tgrade.manager;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

import org.bukkit.entity.Player;

import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.List;

import java.util.Locale;

public class MessageManager {

    public static final String LINE =
            "§8-----------------------------";

    public static final String BIG_LINE =
            "§8-----------------------------";

    public static final String PREFIX =
            "§6✦ §eMoodCraft §8• ";

    public static final String GUI_MAIN =
            "§8✦ Commission Urbaine";

    public static final String GUI_ADMIN =
            "§8✦ Centre Administratif";

    public static final String GUI_PROJECTS =
            "§8✦ Dossiers Urbains";

    public static final String GUI_RATE =
            "§8⭐ Notation Urbaine";

    public static final String GUI_CLASSEMENT =
            "§8🏆 Classement National";

    public static final String GUI_REVIEW =
            "§8✦ Inspection Nationale";

    public static void send(
            Player p,
            String message
    ) {

        p.sendMessage(
                "§e➜ §7" + message
        );
    }

    public static void success(
            Player p,
            String message
    ) {

        p.sendMessage("");
        p.sendMessage(LINE);
        p.sendMessage("§a✔ §f" + message);
        p.sendMessage(LINE);
    }

    public static void error(
            Player p,
            String message
    ) {

        p.sendMessage("");
        p.sendMessage(LINE);
        p.sendMessage("§c✖ §f" + message);
        p.sendMessage(LINE);
    }

    public static void warning(
            Player p,
            String message
    ) {

        p.sendMessage("");
        p.sendMessage(LINE);
        p.sendMessage("§e➜ §7" + message);
        p.sendMessage(LINE);
    }

    public static void info(
            Player p,
            String title,
            String... lines
    ) {

        p.sendMessage("");
        p.sendMessage("§8----- §6✦ " + title + " §6✦ §8-----");

        for (String line : lines) {
            p.sendMessage("§e➜ §7" + line);
        }

        p.sendMessage(LINE);
    }

    public static void broadcast(
            String title,
            String... lines
    ) {

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("§8----- §6✦ " + title + " §6✦ §8-----");

        for (String line : lines) {
            Bukkit.broadcastMessage("§e➜ §7" + line);
        }

        Bukkit.broadcastMessage(LINE);
    }

    public static void national(
            String title,
            String... lines
    ) {

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("§8----- §6✦ Commission Urbaine Nationale ✦ §8-----");
        Bukkit.broadcastMessage("§e➜ §e" + title);

        for (String line : lines) {
            Bukkit.broadcastMessage("§e➜ §7" + line);
        }

        Bukkit.broadcastMessage(BIG_LINE);
    }

    public static String money(
            int amount
    ) {

        NumberFormat format =
                NumberFormat.getInstance(
                        Locale.FRANCE
                );

        return "§a"
                + format.format(amount)
                + "$";
    }

    public static String score(
            int value,
            int max
    ) {

        return "§e"
                + value
                + "§7/"
                + max;
    }

    public static String prestige(
            int score
    ) {

        if (score >= 45) {
            return "§6Métropole légendaire";
        }

        if (score >= 38) {
            return "§eCapitale prospère";
        }

        if (score >= 30) {
            return "§aVille développée";
        }

        if (score >= 25) {
            return "§bVille émergente";
        }

        return "§cVille fragile";
    }

    public static List<String> lore(
            String... lines
    ) {

        List<String> lore =
                new ArrayList<>();

        for (String line : lines) {
            lore.add("§7" + line);
        }

        return lore;
    }

    public static List<String> dividedLore(
            String... lines
    ) {

        List<String> lore =
                new ArrayList<>();

        lore.add("§8----------------");

        for (String line : lines) {
            lore.add("§e➜ §7" + line);
        }

        return lore;
    }

    public static void successSound(Player p) {
        p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
    }

    public static void errorSound(Player p) {
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
    }

    public static void warningSound(Player p) {
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.8f);
    }

    public static void clickSound(Player p) {
        p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1.2f);
    }

    public static void openSound(Player p) {
        p.playSound(p.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 0.8f, 1f);
    }

    public static void closeSound(Player p) {
        p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 0.8f, 1f);
    }
}
