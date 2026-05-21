package fr.moodcraft.tgrade.listener;

import fr.moodcraft.tgrade.gui.PendingProjectsGUI;
import fr.moodcraft.tgrade.gui.ProjectReviewGUI;
import fr.moodcraft.tgrade.gui.UrbanismeAdminGUI;
import fr.moodcraft.tgrade.model.SubmissionStatus;
import fr.moodcraft.tgrade.model.TownSubmission;
import fr.moodcraft.tgrade.storage.SubmissionStorage;
import fr.moodcraft.tgrade.util.MoodStyle;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PendingProjectsListener implements Listener {

    @EventHandler
    public void click(InventoryClickEvent e) {

        if (!MoodStyle.titleEquals(e.getView().getTitle(), PendingProjectsGUI.TITLE)) {
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
            UrbanismeAdminGUI.open(p);
            return;
        }

        String id = MoodStyle.tag(item, MoodStyle.TAG_SUBMISSION_ID);
        TownSubmission submission = id == null ? null : SubmissionStorage.get(id);

        if (submission == null) {
            submission = findByName(item);
        }

        if (submission == null) {
            return;
        }

        ProjectReviewGUI.open(p, submission);
    }

    private TownSubmission findByName(ItemStack item) {

        if (!item.hasItemMeta() || item.getItemMeta().getDisplayName() == null) {
            return null;
        }

        String name = MoodStyle.cleanButtonName(item.getItemMeta().getDisplayName());

        for (TownSubmission submission : SubmissionStorage.getAll()) {
            if (submission.getStatus() == SubmissionStatus.PENDING
                    && submission.getBuildName().equalsIgnoreCase(name)) {
                return submission;
            }
        }

        return null;
    }
}
