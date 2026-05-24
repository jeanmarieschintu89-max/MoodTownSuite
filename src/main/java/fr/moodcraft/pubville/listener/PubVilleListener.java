package fr.moodcraft.pubville.listener;

import fr.moodcraft.pubville.gui.PubVilleGUI;
import fr.moodcraft.pubville.manager.PubVilleManager;
import fr.moodcraft.pubville.model.PubVilleCampaign;
import fr.moodcraft.townmenu.util.SoundUtil;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PubVilleListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!PubVilleGUI.TITLE.equals(event.getView().getTitle())) {
            return;
        }

        event.setCancelled(true);

        HumanEntity clicked = event.getWhoClicked();

        if (!(clicked instanceof Player player)) {
            return;
        }

        if (event.getClickedInventory() == null) {
            return;
        }

        if (event.getRawSlot() >= event.getView().getTopInventory().getSize()) {
            return;
        }

        ItemStack current = event.getCurrentItem();

        if (current == null || current.getType() == Material.AIR) {
            return;
        }

        if (event.getRawSlot() == 40) {
            SoundUtil.back(player);
            player.closeInventory();
            return;
        }

        ItemMeta meta = current.getItemMeta();

        if (meta == null || !meta.hasDisplayName()) {
            return;
        }

        String townName = meta.getDisplayName()
                .replaceAll("§.", "")
                .replace("✦", "")
                .trim();

        PubVilleCampaign campaign = PubVilleManager.getCampaign(townName);

        if (campaign == null) {
            return;
        }

        SoundUtil.click(player);
        PubVilleManager.teleport(player, campaign.getTownName());
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (PubVilleGUI.TITLE.equals(event.getView().getTitle())) {
            event.setCancelled(true);
        }
    }
}
