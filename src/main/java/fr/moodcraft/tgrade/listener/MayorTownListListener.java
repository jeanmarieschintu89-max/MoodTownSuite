package fr.moodcraft.tgrade.listener;

import fr.moodcraft.tgrade.gui.MayorTownListGUI;
import fr.moodcraft.tgrade.gui.MayorVoteGUI;
import fr.moodcraft.tgrade.gui.UrbanismeMainGUI;
import fr.moodcraft.tgrade.util.MoodStyle;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MayorTownListListener implements Listener {

    @EventHandler
    public void click(InventoryClickEvent e) {

        if (!MoodStyle.titleEquals(e.getView().getTitle(), MayorTownListGUI.TITLE)) {
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

        ItemStack item = e.getCurrentItem();

        if (item == null || item.getType().isAir()) {
            return;
        }

        MoodStyle.click(p);

        if (e.getRawSlot() == 49) {
            UrbanismeMainGUI.open(p);
            return;
        }

        String town = MoodStyle.tag(item, MoodStyle.TAG_TOWN);

        if (town == null && item.hasItemMeta()) {
            town = MoodStyle.cleanButtonName(item.getItemMeta().getDisplayName());
        }

        if (town == null || town.isBlank()) {
            return;
        }

        MayorVoteGUI.open(p, town);
    }
}
