package fr.moodcraft.tgrade.listener;

import fr.moodcraft.tgrade.gui.ClassementGUI;
import fr.moodcraft.tgrade.gui.EvaluationManagerGUI;
import fr.moodcraft.tgrade.gui.PendingProjectsGUI;
import fr.moodcraft.tgrade.gui.UrbanismeAdminGUI;
import fr.moodcraft.tgrade.gui.UrbanismeMainGUI;
import fr.moodcraft.tgrade.manager.PayoutManager;
import fr.moodcraft.tgrade.util.MoodStyle;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class UrbanismeAdminListener implements Listener {

    @EventHandler
    public void click(InventoryClickEvent e) {

        if (!MoodStyle.titleEquals(e.getView().getTitle(), UrbanismeAdminGUI.TITLE)) {
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

        if (e.getCurrentItem() == null || e.getCurrentItem().getType().isAir()) {
            return;
        }

        int slot = e.getRawSlot();
        MoodStyle.click(p);

        if (slot == 13) {
            PendingProjectsGUI.open(p);
            return;
        }

        if (slot == 22) {
            EvaluationManagerGUI.open(p);
            return;
        }

        if (slot == 31) {
            PayoutManager.PayoutReport report = PayoutManager.payoutAll();

            MoodStyle.ok(p);
            MoodStyle.send(
                    p,
                    MoodStyle.MODULE,
                    "§a✔ §fSubventions urbaines versées.",
                    "",
                    "§7Villes financées: §e" + report.paid(),
                    "§7Budget total: §e" + String.format("%.0f€", report.total()),
                    "",
                    "§8• §7Seuls les projets clôturés sont payés"
            );
            return;
        }

        if (slot == 33) {
            ClassementGUI.open(p);
            return;
        }

        if (slot == 40) {
            UrbanismeMainGUI.open(p);
        }
    }
}
