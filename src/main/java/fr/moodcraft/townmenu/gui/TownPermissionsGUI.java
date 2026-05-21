package fr.moodcraft.townmenu.gui;

import fr.moodcraft.townmenu.util.MoodStyle;
import fr.moodcraft.townmenu.util.SafeGUI;
import fr.moodcraft.townmenu.util.TownyUtil;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TownPermissionsGUI {

    public static final String TITLE =
            MoodStyle.guiTitle("Permissions Ville");

    public static void open(Player player) {

        Inventory inv = Bukkit.createInventory(
                null,
                54,
                TITLE
        );

        SafeGUI.fill(inv, Material.BLACK_STAINED_GLASS_PANE, " ");

        SafeGUI.safeSet(
                inv,
                4,
                SafeGUI.glow(SafeGUI.item(
                        Material.IRON_DOOR,
                        MoodStyle.button("Permissions ville"),
                        "§7Ville: §b" + TownyUtil.townName(player),
                        "",
                        MoodStyle.bullet("Extérieurs"),
                        MoodStyle.bullet("Autoriser"),
                        MoodStyle.bullet("Bloquer")
                ))
        );

        SafeGUI.safeSet(inv, 10, allow("Construire"));
        SafeGUI.safeSet(inv, 19, deny("Construire"));

        SafeGUI.safeSet(inv, 12, allow("Casser"));
        SafeGUI.safeSet(inv, 21, deny("Casser"));

        SafeGUI.safeSet(inv, 14, allow("Interagir"));
        SafeGUI.safeSet(inv, 23, deny("Interagir"));

        SafeGUI.safeSet(inv, 16, allow("Utiliser items"));
        SafeGUI.safeSet(inv, 25, deny("Utiliser items"));

        SafeGUI.safeSet(
                inv,
                49,
                SafeGUI.item(
                        Material.ARROW,
                        MoodStyle.button("Retour"),
                        "§7Retour aux paramètres.",
                        "",
                        MoodStyle.bullet("Menu ville")
                )
        );

        player.openInventory(inv);
    }

    private static org.bukkit.inventory.ItemStack allow(String name) {

        return SafeGUI.item(
                Material.LIME_DYE,
                MoodStyle.button("Autoriser " + name),
                "§7Autorise cette action",
                "§7aux extérieurs.",
                "",
                MoodStyle.bullet("Action directe")
        );
    }

    private static org.bukkit.inventory.ItemStack deny(String name) {

        return SafeGUI.item(
                Material.RED_DYE,
                MoodStyle.dangerButton("Bloquer " + name),
                "§7Bloque cette action",
                "§7aux extérieurs.",
                "",
                MoodStyle.bullet("Action directe")
        );
    }
}
