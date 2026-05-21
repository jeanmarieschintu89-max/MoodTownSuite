package fr.moodcraft.townmenu.util;

import org.bukkit.Sound;

import org.bukkit.entity.Player;

public class SoundUtil {

    public static void click(Player p) {

        premium(
                p,
                Sound.UI_BUTTON_CLICK,
                0.9f,
                Sound.BLOCK_NOTE_BLOCK_CHIME,
                1.25f
        );
    }

    public static void back(Player p) {

        premium(
                p,
                Sound.UI_BUTTON_CLICK,
                0.8f,
                Sound.BLOCK_CHEST_CLOSE,
                1.2f
        );
    }

    public static void success(Player p) {

        premium(
                p,
                Sound.BLOCK_AMETHYST_BLOCK_CHIME,
                1.25f,
                Sound.ENTITY_EXPERIENCE_ORB_PICKUP,
                1.4f
        );
    }

    public static void fail(Player p) {

        p.playSound(
                p.getLocation(),
                Sound.ENTITY_VILLAGER_NO,
                1f,
                0.85f
        );
    }

    public static void premium(
            Player p,
            Sound main,
            float mainPitch,
            Sound second,
            float secondPitch
    ) {

        p.playSound(
                p.getLocation(),
                main,
                0.75f,
                mainPitch
        );

        p.playSound(
                p.getLocation(),
                second,
                0.35f,
                secondPitch
        );
    }
}