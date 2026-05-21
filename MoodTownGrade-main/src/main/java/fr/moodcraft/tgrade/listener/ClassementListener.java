package fr.moodcraft.tgrade.listener;

import fr.moodcraft.tgrade.gui.ClassementGUI;
import fr.moodcraft.tgrade.gui.UrbanismeMainGUI;
import fr.moodcraft.tgrade.util.MoodStyle;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClassementListener implements Listener {

    @EventHandler
    public void click(InventoryClickEvent e) {

        if (!MoodStyle.titleEquals(e.getView().getTitle(), ClassementGUI.TITLE)) {
            return;
        }

        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player p)) {
            return;
        }

        if (e.getClickedInventory() == null) {
            return;
        }

        if (e.getRawSlot() >= e.getView().getTopInventory().getSize()) {
            return;
        }

        if (e.getRawSlot() == 49) {
            MoodStyle.click(p);
            UrbanismeMainGUI.open(p);
        }
    }
}
