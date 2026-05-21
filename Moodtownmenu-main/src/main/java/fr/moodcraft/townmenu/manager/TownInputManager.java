package fr.moodcraft.townmenu.manager;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TownInputManager {

    public enum Type {

        TOWN_INVITE,
        TOWN_KICK,

        TOWN_ASSISTANT_ADD,
        TOWN_ASSISTANT_REMOVE,

        TOWN_RENAME,

        TOWN_DEPOSIT,
        TOWN_WITHDRAW,

        PLOT_TRUST,
        PLOT_UNTRUST,

        PLOT_FOR_SALE,

        TAX_TOWN,
        TAX_PLOT,
        TAX_SHOP,
        TAX_EMBASSY
    }

    private static final Map<UUID, Type> INPUTS =
            new HashMap<>();

    public static void wait(
            Player p,
            Type type
    ) {

        INPUTS.put(
                p.getUniqueId(),
                type
        );
    }

    public static boolean has(
            Player p
    ) {

        return INPUTS.containsKey(
                p.getUniqueId()
        );
    }

    public static Type get(
            Player p
    ) {

        return INPUTS.get(
                p.getUniqueId()
        );
    }

    public static void clear(
            Player p
    ) {

        INPUTS.remove(
                p.getUniqueId()
        );
    }
}