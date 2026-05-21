package fr.moodcraft.tgrade.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        // Ancien listener général conservé pour compatibilité.
        // Les menus MoodCraft actuels sont gérés par leurs listeners dédiés.
    }
}
