package fr.moodcraft.tgrade.gui.holder;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class RateVoteHolder implements InventoryHolder {

    private final String town;

    public RateVoteHolder(String town) {
        this.town = town;
    }

    public String getTown() {
        return town;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
