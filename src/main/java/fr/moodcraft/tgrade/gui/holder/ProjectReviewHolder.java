package fr.moodcraft.tgrade.gui.holder;

import fr.moodcraft.tgrade.model.TownSubmission;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ProjectReviewHolder implements InventoryHolder {

    private final TownSubmission submission;

    public ProjectReviewHolder(TownSubmission submission) {
        this.submission = submission;
    }

    public TownSubmission getSubmission() {
        return submission;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
