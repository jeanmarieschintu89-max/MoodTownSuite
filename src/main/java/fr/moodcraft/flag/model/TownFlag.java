package fr.moodcraft.flag.model;

import org.bukkit.inventory.ItemStack;

public class TownFlag {

    private final String ownerName;
    private final String ownerType;
    private final ItemStack banner;

    public TownFlag(
            String ownerName,
            String ownerType,
            ItemStack banner
    ) {

        this.ownerName = ownerName;
        this.ownerType = ownerType;
        this.banner = banner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public ItemStack getBanner() {
        return banner;
    }
}