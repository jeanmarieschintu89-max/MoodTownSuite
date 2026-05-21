package fr.moodcraft.tgrade.listener;

import fr.moodcraft.tgrade.gui.ReviewGUI;
import fr.moodcraft.tgrade.gui.UrbanismeMainGUI;
import fr.moodcraft.tgrade.util.MoodStyle;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ReviewGUIListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!MoodStyle.titleEquals(e.getView().getTitle(), ReviewGUI.TITLE)) {
            return;
        }

        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player p)) {
            return;
        }

        if (e.getRawSlot() == 22) {
            MoodStyle.click(p);
            UrbanismeMainGUI.open(p);
        }
    }
}
